package co.erikhdez.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator createRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("banks-service-route", route -> route.path("/api/bank/**")
                        .uri("http://localhost:8081"))
                .route("accounts-service-route", route -> route.path("/api/accounts/**")
                        .uri("http://localhost:8082"))
                .route("transaction-service-route", route -> route.path("/api/transaction/**")
                        .uri("http://localhost:8083"))
                .build();
    }
}
