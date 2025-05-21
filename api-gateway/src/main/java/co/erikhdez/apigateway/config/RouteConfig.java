package co.erikhdez.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, RouteProperties properties) {
        return builder.routes()
                .route(properties.getBanksServiceRoute(), route -> route.path(properties.getPathBank())
                        .uri(properties.getBanksServiceUrl()))
                .route(properties.getAccountsServiceRoute(), route -> route.path(properties.getPathAccount())
                        .uri(properties.getAccountsServiceUrl()))
                .route(properties.getTransactionServiceRoute(), route -> route.path(properties.getPathTransaction())
                        .uri(properties.getTransactionServiceUrl()))
                .build();
    }
}
