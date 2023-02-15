package com.ecommercemicroservices.apigateway.filter;

import com.ecommercemicroservices.apigateway.config.JwtConfig;
import com.ecommercemicroservices.apigateway.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationFilter implements GatewayFilter {
    @Autowired
    private RouterValidator routerValidator;//custom route validator
    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtConfig jwtConfig;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request)) {
                log.error("Authorization header is missing in request");
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            }
                final String token = this.getAuthHeader(request);
            if (!jwtService.isTokenValid(token)) {
                log.error("Authorization header is invalid");
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }
            jwtService.populateRequestWithHeaders(exchange, token);

        }
        return chain.filter(exchange);
    }
    /*PRIVATE*/
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
    private String getAuthHeader(ServerHttpRequest request) {
        var authHeader=request.getHeaders().getOrEmpty(jwtConfig.getAuthorizationHeader());
        var jwt = authHeader.get(0).substring(7);
        return jwt;
    }
    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(jwtConfig.getAuthorizationHeader());
    }

}