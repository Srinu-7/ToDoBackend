package com.example.ToDo.JWT_PACKAGE;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import org.springframework.stereotype.Service;


@Service
public class JwtService {


    private final JwtParser jwtParser;

    public JwtService(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    public boolean ValidateToken(String token) { // Validate token
        try {
            parseToken(token); // Attempt to parse the token
            return true; // If successful, the token is valid
        } catch (RuntimeException e) {
            return false; // If an exception occurs, the token is invalid
        }
    }

    public Claims parseToken(String token) { // Parse token
        try {
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }




}