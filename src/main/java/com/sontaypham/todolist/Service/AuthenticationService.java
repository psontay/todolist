package com.sontaypham.todolist.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sontaypham.todolist.DTO.Request.AuthenticationRequest;
import com.sontaypham.todolist.DTO.Request.IntrospectRequest;
import com.sontaypham.todolist.DTO.Response.AuthenticationResponse;
import com.sontaypham.todolist.DTO.Response.IntrospectResponse;
import com.sontaypham.todolist.Entities.Permission;
import com.sontaypham.todolist.Entities.User;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Repository.InvalidatedTokenRepository;
import com.sontaypham.todolist.Repository.UserRepository;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Service
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${app.jwt.secret}")
    protected String signerKey;
    @NonFinal
    @Value("${app.jwt.validDuration}")
    protected long validDuration;
    @NonFinal
    @Value("${app.jwt.refreshableDuration}")
    protected long refreshableDuration;
    public AuthenticationResponse authenticate (@RequestBody AuthenticationRequest request ) {
        var user = userRepository.findByName(request.getUsername()).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean userPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if ( userPassword ) throw new ApiException(ErrorCode.UNAUTHENTICATED);
        String token = generateToken(user);
        return AuthenticationResponse.builder().success(true).token(token).build();
    }
    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getName())
                .issuer("sontaypham")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(refreshableDuration , ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope" , buildScope(user))
                .claim("permission" , buildPermissions(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey));
            return jwsObject.serialize();
        }catch(Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect(@RequestBody IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid;
        try {
            verifyToken(token , false);
            isValid = true;
        }catch(Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder().success(isValid).build();
    }

    public SignedJWT verifyToken(String token , boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes()); // get signerKey by verifier
        SignedJWT signedJWT = SignedJWT.parse(token); // get token by signedJWT parse
        Date expTime = (isRefresh) ?
                new Date( signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(refreshableDuration ,
                                                                                      ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if ( !verified || expTime.after(new Date())) throw new ApiException(ErrorCode.TOKEN_INVALID);
        if ( invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) throw new ApiException(ErrorCode.TOKEN_INVALID);
        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add(role.getName()));
        }
        return stringJoiner.toString();
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

}
