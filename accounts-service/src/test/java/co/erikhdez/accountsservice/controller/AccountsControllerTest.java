package co.erikhdez.accountsservice.controller;

import co.erikhdez.accountsservice.dto.AccountWithBankDTO;
import co.erikhdez.accountsservice.model.Accounts;
import co.erikhdez.accountsservice.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = AccountsController.class)
class AccountsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountsService accountsService;

    private final Long accountId = 1L;

    @Test
    void shouldGetAllAccounts() {
        var dto = new AccountWithBankDTO(); // o usa builder si aplica

        Mockito.when(accountsService.getAll()).thenReturn(Flux.just(dto));

        webTestClient.get()
                .uri("/api/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AccountWithBankDTO.class)
                .hasSize(1);
    }

    @Test
    void shouldGetAccountById() {
        var dto = new AccountWithBankDTO();

        Mockito.when(accountsService.getAccountsById(accountId)).thenReturn(Mono.just(dto));

        webTestClient.get()
                .uri("/api/accounts/{id}", accountId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountWithBankDTO.class);
    }

    @Test
    void shouldCreateAccount() {
        var account = new Accounts();
        Mockito.when(accountsService.createAccounts(account)).thenReturn(Mono.just(account));

        webTestClient.post()
                .uri("/api/accounts")
                .bodyValue(account)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Accounts.class);
    }

    @Test
    void shouldUpdateAccount() {
        var updated = new Accounts();
        Mockito.when(accountsService.updateBank(accountId, updated)).thenReturn(Mono.just(updated));

        webTestClient.put()
                .uri("/api/accounts/{id}", accountId)
                .bodyValue(updated)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Accounts.class);
    }
}
