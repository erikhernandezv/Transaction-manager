package co.erikhdez.apigateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
        "routes.banks-service-route=banks-service",
        "routes.accounts-service-route=accounts-service",
        "routes.transaction-service-route=transaction-service",
        "routes.path-bank=/api/bank/**",
        "routes.path-account=/api/accounts/**",
        "routes.path-transaction=/api/transaction/**",
        "routes.banks-service-url=http://localhost:8081",
        "routes.accounts-service-url=http://localhost:8082",
        "routes.transaction-service-url=http://localhost:8083"
})
class RouteConfigTest {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void testCustomRouteLocatorCreation() {
        assertNotNull(routeLocator);
    }
}