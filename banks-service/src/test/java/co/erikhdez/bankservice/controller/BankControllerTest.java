package co.erikhdez.bankservice.controller;

import co.erikhdez.bankservice.model.Bank;
import co.erikhdez.bankservice.service.BankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BankController.class)
class BankControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BankService bankService;

    private final Bank sampleBank = new Bank(1L, "Bank Name", "Country", "www.bank.com");

    @Test
    void shouldGetAllBanks() {
        when(bankService.getAll()).thenReturn(Flux.just(sampleBank));

        webTestClient.get()
                .uri("/api/bank")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Bank.class)
                .hasSize(1)
                .consumeWith(response -> {
                    Bank responseBody = response.getResponseBody().get(0);
                    assertNotNull(responseBody);
                    assertEquals(sampleBank.getId(), responseBody.getId());
                    assertEquals(sampleBank.getName(), responseBody.getName());
                    assertEquals(sampleBank.getCountry(), responseBody.getCountry());
                });
    }

    @Test
    void shouldGetBankById() {
        when(bankService.getBankById(1L)).thenReturn(Mono.just(sampleBank));

        webTestClient.get()
                .uri("/api/bank/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Bank.class)
                .consumeWith(response -> {
                    Bank responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    assertEquals(sampleBank.getId(), responseBody.getId());
                    assertEquals(sampleBank.getName(), responseBody.getName());
                    assertEquals(sampleBank.getCountry(), responseBody.getCountry());
                });
    }

    @Test
    void shouldCreateBank() {
        when(bankService.createBank(any(Bank.class))).thenReturn(Mono.just(sampleBank));

        webTestClient.post()
                .uri("/api/bank")
                .bodyValue(sampleBank)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Bank.class)
                .consumeWith(response -> {
                    Bank responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    assertEquals(sampleBank.getId(), responseBody.getId());
                    assertEquals(sampleBank.getName(), responseBody.getName());
                    assertEquals(sampleBank.getCountry(), responseBody.getCountry());
                });
    }

    @Test
    void shouldUpdateBank() {
        when(bankService.updateBank(eq(1L), any(Bank.class))).thenReturn(Mono.just(sampleBank));

        webTestClient.put()
                .uri("/api/bank/1")
                .bodyValue(sampleBank)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Bank.class)
                .consumeWith(response -> {
                    Bank responseBody = response.getResponseBody();
                    assertNotNull(responseBody);
                    assertEquals(sampleBank.getId(), responseBody.getId());
                    assertEquals(sampleBank.getName(), responseBody.getName());
                    assertEquals(sampleBank.getCountry(), responseBody.getCountry());
                });
    }
}
