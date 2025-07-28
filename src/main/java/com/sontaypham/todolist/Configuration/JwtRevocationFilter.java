package com.sontaypham.todolist.Configuration;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRevocationFilter extends OncePerRequestFilter {

  private final TokenRevocationValidator validator;

  public JwtRevocationFilter(TokenRevocationValidator validator) {
    this.validator = validator;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      try {
        SignedJWT jwt = SignedJWT.parse(token);
        validator.assertNotRevoked(jwt);
      } catch (Exception e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Token revoked or invalid\"}");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
