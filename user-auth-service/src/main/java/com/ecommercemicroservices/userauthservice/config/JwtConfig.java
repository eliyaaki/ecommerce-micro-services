package com.ecommercemicroservices.userauthservice.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.util.Date;


@ConfigurationProperties(prefix = "spring.application.jwt")
@Getter
@Setter
public class JwtConfig {

    private String privateSecretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public Date getDateByTokenExpirationAfterDays() {
        return new Date(System.currentTimeMillis() + 1000 * 60 * 24 * tokenExpirationAfterDays);
    }

    public Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    public SecretKey secretKey() {
        var keyBytes = Decoders.BASE64.decode(getPrivateSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String authorities() {
        return "authorities";
    }

    public String authority() {
        return "authority";
    }

}
