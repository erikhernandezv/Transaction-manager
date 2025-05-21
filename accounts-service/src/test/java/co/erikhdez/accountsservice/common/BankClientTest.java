package co.erikhdez.accountsservice.common;

import co.erikhdez.accountsservice.dto.BankDTO;
import co.erikhdez.accountsservice.exception.BankNotFoundException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

class BankClientTest {

    private MockWebServer mockWebServer;
    private BankClient bankClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String mockUrl = mockWebServer.url("/api/bank").toString();
        WebClient.Builder builder = WebClient.builder();
        bankClient = new BankClient(builder, mockUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldReturnBankByIdSuccessfully() {
        String jsonResponse = """
                {
                  "id": 1,
                  "name": "Test Bank"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<BankDTO> result = bankClient.getBankById(1L);

        StepVerifier.create(result)
                .expectNextMatches(bank ->
                        bank.getId() == 1L && "Test Bank".equals(bank.getName()))
                .verifyComplete();
    }

    @Test
    void shouldReturnBankNotFoundExceptionOn404() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404));

        Mono<BankDTO> result = bankClient.getBankById(99L);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BankNotFoundException &&
                                error.getMessage().contains("Bank not found with ID: 99"))
                .verify();
    }

    @Test
    void shouldReturnRuntimeExceptionOn500() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        Mono<BankDTO> result = bankClient.getBankById(1L);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof RuntimeException &&
                                error.getMessage().contains("Server error while validating bank with ID: 1"))
                .verify();
    }
}
