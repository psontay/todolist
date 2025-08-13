package com.sontaypham.todolist.configuration;

import com.nimbusds.jwt.SignedJWT;
import com.sontaypham.todolist.exception.ApiException;
import com.sontaypham.todolist.exception.ErrorCode;
import com.sontaypham.todolist.repository.InvalidatedTokenRepository;
import java.text.ParseException;
import org.springframework.stereotype.Component;

@Component
public class TokenRevocationValidator {

  private final InvalidatedTokenRepository invalidatedTokenRepository;

  public TokenRevocationValidator(InvalidatedTokenRepository invalidatedTokenRepository) {
    this.invalidatedTokenRepository = invalidatedTokenRepository;
  }

  public void assertNotRevoked(SignedJWT jwt) throws ParseException {
    String jwtId = jwt.getJWTClaimsSet().getJWTID();
    if (jwtId != null && invalidatedTokenRepository.existsById(jwtId)) {
      throw new ApiException(ErrorCode.TOKEN_INVALID);
    }
  }
}
