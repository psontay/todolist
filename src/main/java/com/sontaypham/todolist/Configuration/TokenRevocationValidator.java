package com.sontaypham.todolist.Configuration;

import com.nimbusds.jwt.SignedJWT;
import com.sontaypham.todolist.Exception.ApiException;
import com.sontaypham.todolist.Exception.ErrorCode;
import com.sontaypham.todolist.Repository.InvalidatedTokenRepository;
import org.springframework.stereotype.Component;

import java.text.ParseException;

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
