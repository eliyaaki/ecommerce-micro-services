package com.ecommercemicroservices.apigateway.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.util.Date;


@ConfigurationProperties(prefix = "spring.application.jwt")
@Slf4j
@Getter
@Setter
public class JwtConfig {

    private String privateSecretKey;
    private String tokenPrefix;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    public SecretKey secretKey() {
        var keyBytes = Decoders.BASE64.decode(getPrivateSecretKey());
        var keyAfterHash=Keys.hmacShaKeyFor(keyBytes);
        return keyAfterHash;
    }

    public String authorities() {
        return "authorities";
    }

    public String authority() {
        return "authority";
    }

}
