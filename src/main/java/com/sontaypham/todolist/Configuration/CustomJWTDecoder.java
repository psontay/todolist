package com.sontaypham.todolist.Configuration;

import com.sontaypham.todolist.DTO.Request.IntrospectRequest;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Service.AuthenticationService;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomJWTDecoder implements JwtDecoder {
  @Value("${app.jwt.secret}")
  String signerKey;

  @Autowired private AuthenticationService authenticationService;
  private NimbusJwtDecoder nimbusJwtDecoder = null;

  @Override
  public Jwt decode(String token) throws JwtException {
    try {
      var verify =
          authenticationService.introspect(IntrospectRequest.builder().token(token).build());
      if (!verify.isSuccess()) throw new ApiException(ErrorCode.TOKEN_INVALID);
      if (Objects.isNull(nimbusJwtDecoder)) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        nimbusJwtDecoder =
            NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
      }
      log.info(nimbusJwtDecoder.toString());
      return nimbusJwtDecoder.decode(token);
    } catch (Exception e) {
      throw new JwtException(e.getMessage());
    }
  }
}
