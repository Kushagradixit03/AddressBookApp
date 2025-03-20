package com.example.addressBook.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "Lock"; // Move this to application.properties
    private static final long EXPIRATION_TIME = 60000*60; // 24 hours in milliseconds (you can change this to whatever fits your needs)

    // Generate JWT Token
    public String createToken(Long userId) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME); // Set expiration time

        return JWT.create()
                .withClaim("user_id", userId)
                .withExpiresAt(expirationDate) // Set expiration
                .sign(algorithm);
    }

    // Verify Token and get User ID
    public Long verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);

        // Check if token is expired
        if (decodedJWT.getExpiresAt().before(new Date())) {
            throw new RuntimeException("Token has expired");
        }

        return decodedJWT.getClaim("user_id").asLong();
    }
}
