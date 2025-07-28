package com.sontaypham.todolist.Configuration;

import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
  @Value("${app.jwt.secret}")
  private String signerKey;

  //  private AuthenticationService authenticationService;
  private NimbusJwtDecoder nimbusJwtDecoder = null;

  //  public CustomJwtDecoder(ObjectProvider<AuthenticationService> provider) {
  //    this.authenticationService = provider.getIfAvailable();
  //  }

  @Override
  public Jwt decode(String token) throws JwtException {
    if (Objects.isNull(nimbusJwtDecoder)) {
      SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HmacSHA512");
      nimbusJwtDecoder =
          NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }
    return nimbusJwtDecoder.decode(token);
  }
}
