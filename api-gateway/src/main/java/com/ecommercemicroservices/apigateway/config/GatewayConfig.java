package com.ecommercemicroservices.apigateway.config;

import com.ecommercemicroservices.apigateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("discovery-server", r -> r.path("/eureka/**").uri("http://localhost:8761"))
                .route("user-auth-service", r -> r.path("/api/v1/user/**").uri("lb://user-auth-service"))
                .route("product-service", r -> r.path("/api/v1/product/**").filters(f -> f.filter(filter)).uri("lb://product-service"))
                .route("inventory-service", r -> r.path("/api/v1/inventory/**").filters(f -> f.filter(filter)).uri("lb://inventory-service"))
                .route("order-service", r -> r.path("/api/v1/order/**").filters(f -> f.filter(filter)).uri("lb://order-service"))
                .build();
    }

}