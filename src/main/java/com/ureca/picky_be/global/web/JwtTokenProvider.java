package com.ureca.picky_be.global.web;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private Key key;

    @PostConstruct
    private void init() {
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public LocalJwtDto generate(Long userId, String role) {
        Claims claims = Jwts.claims();
        claims.put("id", userId);
        claims.put("role", role);
        return new LocalJwtDto(generateToken(claims));
    }

    private String generateToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt())
                .setExpiration(expiredAt())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date issuedAt() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date expiredAt() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.plusHours(jwtProperties.getExpiration()).atZone(ZoneId.systemDefault()).toInstant());
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id", Long.class);
    }

    public String getRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Malformed token: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Token is invalid: {}", e.getMessage());
        }
        return false;
    }
}
