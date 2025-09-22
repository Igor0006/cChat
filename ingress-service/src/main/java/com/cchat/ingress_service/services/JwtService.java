package com.cchat.ingress_service.services;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cchat.ingress_service.UserRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepo userRepo;

    @Value("${security.key}")
    private String secret;

    private static final Duration ttl = Duration.ofHours(2);

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(String login) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttl.toMillis());

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", userRepo.findIdByLogin(login));

        return Jwts.builder()
                .claims(claims)
                .subject(login)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key())
                .compact();
    }

    public boolean isValid(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
