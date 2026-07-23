package com.sontaypham.todolist.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.sontaypham.todolist.dto.response.ApiResponse;
import com.sontaypham.todolist.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRevocationFilter extends OncePerRequestFilter {

    private final TokenRevocationValidator validator;
    private final ObjectMapper objectMapper;

    public JwtRevocationFilter(TokenRevocationValidator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper;
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
                response.setCharacterEncoding("UTF-8");
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                                                           .status(ErrorCode.UNAUTHORIZED.getCode())
                                                           .message("Token revoked or invalid")
                                                           .build();
                String json = objectMapper.writeValueAsString(apiResponse);
                response.getWriter()
                        .write(json);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
