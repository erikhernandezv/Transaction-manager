package co.erikhdez.accountsservice.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class WebClientConfigTest {

    @Test
    void testBuilderIsNotNull() {
        WebClientConfig config = new WebClientConfig();
        assertNotNull(config.webClientBuilder());
    }
}