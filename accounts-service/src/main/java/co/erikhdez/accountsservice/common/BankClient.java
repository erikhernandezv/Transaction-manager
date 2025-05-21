package co.erikhdez.accountsservice.common;

import co.erikhdez.accountsservice.dto.BankDTO;
import co.erikhdez.accountsservice.exception.BankNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BankClient {

    private final WebClient webClient;

    public BankClient(WebClient.Builder webClientBuilder, @Value("${bank.service.url}") String baseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<BankDTO> getBankById(Long bankId) {
        return webClient.get()
                .uri("/{id}", bankId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new BankNotFoundException("Bank not found with ID: " + bankId))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Server error while validating bank with ID: " + bankId))
                )
                .bodyToMono(BankDTO.class);
    }
}
