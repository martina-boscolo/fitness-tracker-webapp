package it.unipd.dei.cyclek.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenJWT {

    private TokenJWT(){};
    private static final String SECRET_KEY = "S463hkj5G56783bjGkbFSAgvllPtt789YGUTFStuqhdbuiywe874632";

    // Method to generate JWT token
    public static String generateToken(String userId) {
        long expirationTimeInMillis = 3600000; // Token expiration time: 1 hour
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTimeInMillis);

        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Method to extract user ID from JWT token
    public static String extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}
