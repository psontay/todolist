package com.sontaypham.todolist.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final String[] PUBLIC_ENDPOINTS = {
    "/users/create",
    "/auth/login",
    "/auth/logout",
    "/auth/introspect",
    "/email/sendSimpleMail",
    "/auth/reset-password",
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/swagger-resources/**",
          "/swagger-ui.html",
          "/swagger-resources",
          "/configuration/ui",
          "/configuration/security",
          "/api/public/**",
          "/api/public/authenticate",
          "/actuator/*",
          "/webjars/**",
          "/api-docs/**",
  };

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  @Autowired private CustomJwtDecoder customJwtDecoder;
  @Autowired private JwtRevocationFilter jwtRevocationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        request ->
            request
                .requestMatchers( PUBLIC_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated());
    http.oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(
                    jwt ->
                        jwt.decoder(customJwtDecoder)
                            .jwtAuthenticationConverter(jwtAuthenticationConverter())))
        .addFilterBefore(jwtRevocationFilter, BearerTokenAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(
        jwt -> {
          Collection<GrantedAuthority> auths = new ArrayList<>();
          jwt.getClaimAsStringList("scope").stream()
              .map(r -> "ROLE_" + r)
              .map(SimpleGrantedAuthority::new)
              .forEach(auths::add);
          jwt.getClaimAsStringList("permission").stream()
              .map(SimpleGrantedAuthority::new)
              .forEach(auths::add);
          return auths;
        });
    return converter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
