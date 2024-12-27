package com.example.Practice.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final long TOKEN_VALIDITY = 60 * 60 * 1000 * 10; // 10 hours
    private static String secretKey;

    // Constructor to initialize the secret key
    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(HMAC_SHA256);
            keyGen.init(256); // Ensures a key length suitable for HS256
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error creating the secret key", e);
        }
    }

    // Generate a JWT token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Retrieve the secret key as a Key object
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract claims from a token
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token signature", e);
        }
    }

    // Extract username from a token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Validate a token against user details
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    // Check if a token is expired
    private boolean isExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract the expiration date from a token
    private Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }
}
