package co.erikhdez.transactionservice.service;

import co.erikhdez.transactionservice.common.AccountsClient;
import co.erikhdez.transactionservice.dto.TransactionRequestDTO;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class TransactionUtils {
    public Boolean isIntrabank(TransactionRequestDTO transactionRequestDTO) {
        return (transactionRequestDTO.getBankIdOrigin().compareTo(transactionRequestDTO.getBankIdDestination()) == 0);
    }

    public Mono<Boolean> sufficientFunds(TransactionRequestDTO transactionRequestDTO, AccountsClient accountsClient) {
        return accountsClient.findByBankIdAndAccountId(transactionRequestDTO.getBankIdOrigin(), transactionRequestDTO.getAccountIdOrigin())
                .map(accountsData -> accountsData.getAmount().compareTo(transactionRequestDTO.getAmount()) >= 0)
                .defaultIfEmpty(false);
    }

    public Boolean isValidTransactionBetweenAccounts(TransactionRequestDTO transactionRequestDTO) {
        return (transactionRequestDTO.getAccountIdOrigin().compareTo(transactionRequestDTO.getAccountIdDestination()) != 0);
    }
}
