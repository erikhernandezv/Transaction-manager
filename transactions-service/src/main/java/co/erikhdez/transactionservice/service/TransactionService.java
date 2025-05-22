package co.erikhdez.transactionservice.service;

import co.erikhdez.transactionservice.common.AccountsClient;
import co.erikhdez.transactionservice.common.Movement;
import co.erikhdez.transactionservice.common.State;
import co.erikhdez.transactionservice.common.TransactionType;
import co.erikhdez.transactionservice.dto.TransactionRequestDTO;
import co.erikhdez.transactionservice.event.EventPublisher;
import co.erikhdez.transactionservice.exception.BusinessException;
import co.erikhdez.transactionservice.model.Transaction;
import co.erikhdez.transactionservice.repository.ITransactionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransactionService {

    private final ITransactionService transactionServices;
    private final AccountsClient accountsClient;
    private final EventPublisher eventPublisher;
    private final TransactionUtils trxUtils;

    public TransactionService(ITransactionService transactionService, AccountsClient accountsClient,
                              EventPublisher eventPublisher, TransactionUtils transactionUtils) {
        this.transactionServices = transactionService;
        this.accountsClient = accountsClient;
        this.eventPublisher = eventPublisher;
        this.trxUtils = transactionUtils;
    }

    public Flux<Transaction> getAll() {
        return transactionServices.findAll();
    }

    public Mono<Transaction> getBankById(Long id) {
        return transactionServices.findById(id)
                .switchIfEmpty(
                        Mono.error(new RuntimeException("Transaction not found"))
                );
    }

    public Mono<Transaction> createTransaction(TransactionRequestDTO transactionRequestDTO) {

        if (!trxUtils.isValidTransactionBetweenAccounts(transactionRequestDTO)) {
            return Mono.error(
                    new BusinessException("Transaction not allowed: destination account is equal to origin account.")
            );
        }

        return trxUtils.sufficientFunds(transactionRequestDTO, accountsClient)
                .flatMap(hasFunds -> {
                    if (!hasFunds) {
                        return Mono.error(new BusinessException("Transaction not allowed: insufficient funds."));
                    }

                    if ( trxUtils.isIntrabank(transactionRequestDTO) ) {
                        return saveTransaction(transactionRequestDTO, Movement.DEBIT, TransactionType.INTRABANK)
                                .then(saveTransaction(transactionRequestDTO, Movement.CREDIT, TransactionType.INTRABANK));
                    } else {
                        eventPublisher.sendEvent(transactionRequestDTO);
                        return saveTransaction(transactionRequestDTO, Movement.DEBIT, TransactionType.INTRABANK);
                    }
                });
    }

    public Mono<Transaction> updateTransaction(Long id, Transaction updatedTransaction) {
        return transactionServices.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction not found")))
                .flatMap(existingTransaction -> {
                    existingTransaction.setAmount(updatedTransaction.getAmount());
                    existingTransaction.setStateTransaction(updatedTransaction.getStateTransaction());
                    return transactionServices.save(existingTransaction);
                });
    }

    private Mono<Transaction> saveTransaction(TransactionRequestDTO transactionRequestDTO, Movement movement,
                                              TransactionType transactionType) {
        Transaction trx = new Transaction();
        trx.setTypeTransaction(transactionType);
        trx.setDateTransaction(LocalDate.now());
        trx.setTypeMovement(movement);
        trx.setStateTransaction(State.COMPLETADA);
        trx.setAmount(BigDecimal.ZERO);

        if (movement.equals(Movement.CREDIT)) {
            trx.setAccountId(transactionRequestDTO.getAccountIdDestination());
            trx.setBankId(transactionRequestDTO.getBankIdDestination());
            trx.setAmount( trx.getAmount().add( transactionRequestDTO.getAmount() ) );
        }else{
            trx.setAccountId(transactionRequestDTO.getAccountIdOrigin());
            trx.setBankId(transactionRequestDTO.getBankIdOrigin());
            trx.setAmount( trx.getAmount().subtract( transactionRequestDTO.getAmount() ) );
        }

        return transactionServices.save(trx);
    }
}
