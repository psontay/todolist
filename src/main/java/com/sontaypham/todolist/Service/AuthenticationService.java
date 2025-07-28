package com.sontaypham.todolist.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sontaypham.todolist.DTO.Request.*;
import com.sontaypham.todolist.DTO.Response.AuthenticationResponse;
import com.sontaypham.todolist.DTO.Response.IntrospectResponse;
import com.sontaypham.todolist.DTO.Response.RefreshTokenResponse;
import com.sontaypham.todolist.DTO.Response.ResetPasswordResponse;
import com.sontaypham.todolist.Entities.*;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Repository.EmailRepository;
import com.sontaypham.todolist.Repository.InvalidatedTokenRepository;
import com.sontaypham.todolist.Repository.UserRepository;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class AuthenticationService {
  @Autowired UserRepository userRepository;
  @Autowired InvalidatedTokenRepository invalidatedTokenRepository;
  @Autowired EmailRepository emailRepository;
  @Autowired PasswordEncoder passwordEncoder;
  private static final SecureRandom RANDOM = new SecureRandom();
  private static final String CHARACTERS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  @NonFinal
  @Value("${app.jwt.secret}")
  public String signerKey;

  @NonFinal
  @Value("${app.jwt.validDuration}")
  public long validDuration;

  @NonFinal
  @Value("${app.jwt.refreshableDuration}")
  public long refreshableDuration;

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var user =
        userRepository
            .findByName(request.getUsername())
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    PasswordEncoder encoder = new BCryptPasswordEncoder(10);
    boolean userPassword = encoder.matches(request.getPassword(), user.getPassword());
    if (!userPassword) throw new ApiException(ErrorCode.UNAUTHENTICATED);
    String token = generateToken(user);
    log.info("Generated token: {}", token);
    return AuthenticationResponse.builder().success(true).token(token).build();
  }

  private String generateToken(User user) {
    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet jwtClaimsSet =
        new JWTClaimsSet.Builder()
            .subject(user.getName())
            .issuer("sontaypham")
            .issueTime(new Date())
            .expirationTime(
                new Date(
                    Instant.now().plus(refreshableDuration, ChronoUnit.SECONDS).toEpochMilli()))
            .claim("scope", buildScope(user))
            .claim("permission", buildPermissions(user))
            .claim("userId", user.getId())
            .claim("jti", UUID.randomUUID().toString())
            .build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(jwsHeader, payload);
    try {
      jwsObject.sign(new MACSigner(signerKey));
      return jwsObject.serialize();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public IntrospectResponse introspect(IntrospectRequest request) {
    var token = request.getToken();
    boolean isValid;
    try {
      verifyToken(token, false);
      isValid = true;
    } catch (Exception e) {
      log.error(e.getMessage());
      isValid = false;
    }
    return IntrospectResponse.builder().success(isValid).build();
  }

  public SignedJWT verifyToken(String token, boolean isRefresh)
      throws ParseException, JOSEException {
    JWSVerifier verifier = new MACVerifier(signerKey.getBytes()); // get signerKey by verifier
    SignedJWT signedJWT = SignedJWT.parse(token); // get token by signedJWT parse
    Date expTime =
        (isRefresh)
            ? new Date(
                signedJWT
                    .getJWTClaimsSet()
                    .getIssueTime()
                    .toInstant()
                    .plus(refreshableDuration, ChronoUnit.SECONDS)
                    .toEpochMilli())
            : signedJWT.getJWTClaimsSet().getExpirationTime();
    boolean verified = signedJWT.verify(verifier);
    if (!verified || expTime.before(new Date())) throw new ApiException(ErrorCode.TOKEN_INVALID);
    String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
    if (jwtId != null && invalidatedTokenRepository.existsById(jwtId))
      throw new ApiException(ErrorCode.TOKEN_INVALID);
    return signedJWT;
  }

  public void logout(LogoutRequest request) throws ParseException, JOSEException {
    try {
      var signToken = verifyToken(request.getToken(), true);
      String jti = signToken.getJWTClaimsSet().getJWTID();
      Date expTime = signToken.getJWTClaimsSet().getExpirationTime();
      InvalidatedToken invalidatedToken =
          InvalidatedToken.builder().id(jti).expTime(expTime).build();
      invalidatedTokenRepository.save(invalidatedToken);
    } catch (ApiException e) {
      log.error(e.getMessage());
    }
  }

  public RefreshTokenResponse refreshToken(RefreshTokenRequest request)
      throws ParseException, JOSEException {
    SignedJWT signedJWT = verifyToken(request.getToken(), true);
    String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
    Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
    InvalidatedToken invalidatedToken =
        InvalidatedToken.builder().expTime(expTime).id(jwtId).build();
    invalidatedTokenRepository.save(invalidatedToken);
    String username = signedJWT.getJWTClaimsSet().getSubject();
    User user =
        userRepository
            .findByName(username)
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    String token = generateToken(user);
    return RefreshTokenResponse.builder().success(true).token(token).build();
  }

  private List<String> buildScope(User user) {
    if (CollectionUtils.isEmpty(user.getRoles())) {
      return Collections.emptyList();
    }
    return user.getRoles().stream().map(Role::getName).toList();
  }

  private List<String> buildPermissions(User user) {
    if (CollectionUtils.isEmpty(user.getRoles())) {
      return Collections.emptyList();
    }
    return user.getRoles().stream()
        .flatMap(role -> role.getPermissions().stream())
        .map(Permission::getName)
        .distinct()
        .toList();
  }

  public void assertTokenNotRevoked(SignedJWT jwt) throws ParseException {
    String jwtId = jwt.getJWTClaimsSet().getJWTID();
    if (jwtId != null && invalidatedTokenRepository.existsById(jwtId)) {
      throw new ApiException(ErrorCode.TOKEN_INVALID);
    }
  }

  public ResetPasswordResponse resetPasswordEmail(ResetPasswordRequest request) {
    User user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    String newPassword = generateRandomString(12);
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    emailRepository.sendSimpleMail(
        EmailDetails.builder()
            .to(user.getEmail())
            .messageBody("Your new password : " + newPassword)
            .subject("Password Reset Request for Your TodoList Account")
            .build());
    return ResetPasswordResponse.builder()
        .message("New password has been sent to your email! Pls check it.")
        .build();
  }

  public static String generateRandomString(int length) {
    StringBuilder result = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int index = RANDOM.nextInt(CHARACTERS.length());
      result.append(CHARACTERS.charAt(index));
    }
    return result.toString();
  }
}
