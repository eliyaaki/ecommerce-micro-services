package com.ecommercemicroservices.userauthservice.service;

import com.ecommercemicroservices.userauthservice.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    //    @Value("${spring.application.secret.key}")
//    public String SECRET_KEY;
//
//
    private final JwtConfig jwtConfig;

    public HashMap<String, ? extends Object> generateExtraClaims(Set<GrantedAuthority> authorities) {
        var extraClaims = new HashMap<String, Set<? extends Object>>();
        extraClaims.put(jwtConfig.authorities(), authorities);
        return extraClaims;
    }

    public String generateToken(String userName, Set<GrantedAuthority> authorities) {
        var extraClaims = generateExtraClaims(authorities);
        return generateToken(extraClaims, userName);
    }

    public String generateToken(HashMap<String, ? extends Object> extraClaims, String userName) {

        log.error("PRIVATE_SECRET_KEY:  " + jwtConfig.getPrivateSecretKey());
        log.error("extraClaims:  " + extraClaims);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userName)
                .setIssuedAt(jwtConfig.getCurrentDate())
                .setExpiration(jwtConfig.getDateByTokenExpirationAfterDays())
                .signWith(jwtConfig.secretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String userName) {
        var userNameFromToken = extractUserName(token);
        return (userNameFromToken.equals(userName) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUserName(String token) {
        return extractClaim(token, s -> s.getSubject());
    }

    public Set<SimpleGrantedAuthority> extractAuthorities(String token) {
        var body = extractAllClaims(token);
//        var authorities = (Set<GrantedAuthority>) body.get("authorities");
        var authorities = (List<Map<String, String>>) body.get(jwtConfig.authorities());
        log.error("extract authorities from body:  " + authorities);
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get(jwtConfig.authority())))
                .collect(Collectors.toSet());
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
