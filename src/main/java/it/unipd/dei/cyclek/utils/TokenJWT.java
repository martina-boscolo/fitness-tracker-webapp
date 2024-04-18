package it.unipd.dei.cyclek.utils;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.unipd.dei.cyclek.resources.AbstractResource;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class TokenJWT extends AbstractResource {

    @JsonIgnore
    private static final String SECRET_KEY = "S463hkj5G56783bjGkbFSAgvllPtt789YGUTFStuqhdbuiywe874632";

    private String token;

    public TokenJWT(){}

    public TokenJWT(Integer id) {
        this.token = generateToken(id.toString());
    }

    public String getToken() {
        return token;
    }

    // Method to generate JWT token
    public static String generateToken(String userId) {
        long expirationTimeInMillis = 3600000; // Token expiration time: 1 hour
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTimeInMillis);
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    // Method to extract user ID from JWT token
    public static String extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }
}
