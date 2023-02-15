package com.ecommercemicroservices.apigateway.service;

import com.ecommercemicroservices.apigateway.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final JwtConfig jwtConfig;

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUserName(String token) {
        return extractClaim(token, s -> s.getSubject());
    }

    public ServerWebExchange populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        var authorities =extractAuthorities(token);
        var userName= extractUserName(token);
        String[] authoritiesAsArray= authorities.toArray(String[]::new);
        log.error("authoritiesAsArray:  "+authoritiesAsArray);
        exchange.getRequest().mutate()
                .header("authorities", authoritiesAsArray)
                .header("userName", userName)
                .build();
        return exchange;
    }


    public List<String> extractAuthorities(String token) {
        var body = extractAllClaims(token);
        var authorities = (List<Map<String, String>>) body.get(jwtConfig.authorities());
        log.error("extract authorities from body:  " + authorities);
        List<String> simpleGrantedAuthorities = authorities.stream()
                .map(m -> m.get(jwtConfig.authority()))
                .toList();
        log.error("extract simpleGrantedAuthorities from authorities:  " + simpleGrantedAuthorities);


        return simpleGrantedAuthorities;
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, s -> s.getExpiration());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtConfig.secretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
