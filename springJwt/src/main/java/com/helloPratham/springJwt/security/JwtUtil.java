package com.helloPratham.springJwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String secretKey = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

    // Generate JWT token
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token expires in 10 hours
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Extract user ID from token
    public String extractUserIdFromToken(String token) {
        return parseToken(token).getSubject();
    }

    // Validate the JWT token
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Parse the JWT token to extract claims
    private Claims parseToken(String token) {
        return Jwts.parserBuilder() // Use parserBuilder() instead of parser()
                .setSigningKey(secretKey) // Set the signing key
                .build() // Call build() on the parserBuilder
                .parseClaimsJws(token.replace("Bearer ", "")) // Parse the token
                .getBody(); // Extract the claims
    }


    public String extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Extract the user ID or subject from the token
    }

}
