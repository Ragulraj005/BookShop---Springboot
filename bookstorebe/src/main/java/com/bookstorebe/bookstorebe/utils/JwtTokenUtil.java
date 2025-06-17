package com.bookstorebe.bookstorebe.utils;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {

    // IMPORTANT: In a real application, this secret key should be:
    // 1. Stored securely (e.g., in environment variables, Vault, or a secure configuration server).
    // 2. A long, random, and cryptographically strong string.
    // 3. NOT hardcoded in your source code.
    // For demonstration purposes, we're generating a secure key once.
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token expiration time in milliseconds (e.g., 1 hour)
    public static final long JWT_TOKEN_VALIDITY = 60 * 60 * 1000; // 1 hour


    public static String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
    }


    public static String extractSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

  
    public static Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            // Check if token is expired
            Date expirationDate = Jwts.parserBuilder()
                                      .setSigningKey(SECRET_KEY)
                                      .build()
                                      .parseClaimsJws(token)
                                      .getBody()
                                      .getExpiration();
            return !expirationDate.before(new Date());
        } catch (Exception e) {
            // Token is invalid (e.g., expired, malformed, or signature mismatch)
            System.err.println("JWT Validation Error: " + e.getMessage());
            return false;
        }
    }
}
