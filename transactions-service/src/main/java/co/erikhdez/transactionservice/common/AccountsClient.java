package co.erikhdez.transactionservice.common;

import co.erikhdez.transactionservice.dto.AccountsDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AccountsClient {

    private final WebClient webClient;

    public AccountsClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8080/api/accounts")
                .build();
    }

    public Mono<AccountsDTO> findByBankIdAndAccountId(Long bankId, Long accountId) {
        return webClient.get()
                .uri("/{bankId}/accounts/", bankId, accountId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Accounts not found with ID: " + bankId))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Server error while validating bank with ID: " + bankId))
                )
                .bodyToMono(AccountsDTO.class);
    }
}
