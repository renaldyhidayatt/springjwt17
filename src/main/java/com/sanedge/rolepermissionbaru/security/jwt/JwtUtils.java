package com.sanedge.rolepermissionbaru.security.jwt;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sanedge.rolepermissionbaru.security.services.UserDetailsImpl;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  static final String issuer = "MyApp";

  @Value("${springjwt.app.jwtSecret}")
  private String jwtSecret;

  @Value("${springjwt.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  private Algorithm accessTokenAlgorithm;

  private JWTVerifier jwtTokenVerifier;

  public JwtUtils(@Value("${springjwt.app.jwtSecret}") String accessTokenSecret,
      @Value("${springjwt.app.jwtExpirationMs}") int jwtExpirationMs) {
    jwtExpirationMs = (int) jwtExpirationMs * 60 * 1000;
    accessTokenAlgorithm = Algorithm.HMAC512(accessTokenSecret);
    jwtTokenVerifier = JWT.require(accessTokenAlgorithm)
        .withIssuer(issuer)
        .build();
  }

  public String generateAccessToken(Authentication authentication) {
    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return JWT.create()
        .withIssuer(issuer)
        .withSubject(userPrincipal.getUsername())
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(new Date().getTime() + jwtExpirationMs))
        .sign(accessTokenAlgorithm);
  }

  private Optional<DecodedJWT> decodeAccessToken(String token) {
    try {
      return Optional.of(jwtTokenVerifier.verify(token));
    } catch (JWTVerificationException e) {
      logger.error("invalid access token", e);
    }
    return Optional.empty();
  }

  public boolean validateAccessToken(String token) {
    return decodeAccessToken(token).isPresent();
  }

  public String getUsernameAccessToken(String token) {
    return decodeAccessToken(token).get().getSubject();
  }

}
