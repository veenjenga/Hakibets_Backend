package com.hakibets.menu_dashboard.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;//Holds the data inside a token (like the username).
import io.jsonwebtoken.SignatureAlgorithm;//The main JWT library for creating and parsing tokens.
import org.springframework.beans.factory.annotation.Value;//Defines the signing method (e.g., HS256).
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;//Helps convert the secret into bytes safely.

@Component
public class JwtUtil {
    @Value("${jwt.secret}")//pulls the secret from application.properties
    private String secret;

    public String generateToken(String username) {//This method creates a new JWT token for a given username (e.g., "admin"). It returns the token as a string.
        System.out.println("Generating token for username: " + username + " with secret: " + secret);//Prints a message to the console showing who the token is for and the secret used, helping in debugging.
        return Jwts.builder()//starts building a new token, like starting a new pass.
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes(StandardCharsets.UTF_8)) // signs the token with the HS256 algorithm (a secure way to lock it) using the secret converted to bytes.
                .compact();//finishes the token, turning it into a single string
    }

    public String extractUsername(String token) {
        System.out.println("Extracting username from token: " + token);
        return Jwts.parser() //starts reading the token.
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))//provides the secret in bytes to unlock and verify the token.
                .parseClaimsJws(token)//breaks the token into its parts (header, payload, signature) and checks the signature.
                .getBody()//gets the payload (the data part), which contains claims like the subject.
                .getSubject();//extracts the username from the payload.
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("Validating token: " + token);
            Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}