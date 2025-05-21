package co.erikhdez.transactionservice.service;

import co.erikhdez.transactionservice.common.AccountsClient;
import co.erikhdez.transactionservice.common.Movement;
import co.erikhdez.transactionservice.common.State;
import co.erikhdez.transactionservice.common.TransactionType;
import co.erikhdez.transactionservice.dto.TransactionRequestDTO;
import co.erikhdez.transactionservice.exception.BusinessException;
import co.erikhdez.transactionservice.model.Transaction;
import co.erikhdez.transactionservice.repository.ITransactionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class TransactionService {

    private final ITransactionService transactionServices;
    private final AccountsClient accountsClient;

    public TransactionService(ITransactionService transactionService, AccountsClient accountsClient) {
        this.transactionServices = transactionService;
        this.accountsClient = accountsClient;
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

        if (!isValidTransactionBetweenAccounts(transactionRequestDTO)) {
            return Mono.error(
                    new BusinessException("Transaction not allowed: destination account is equal to origin account.")
            );
        }

        return sufficientFunds(transactionRequestDTO)
                .flatMap(hasFunds -> {
                    if (!hasFunds) {
                        return Mono.error(new BusinessException("Transaction not allowed: insufficient funds."));
                    }

                    if ( isIntrabank(transactionRequestDTO) ) {
                        return saveTransaction(transactionRequestDTO, Movement.DEBIT, TransactionType.INTRABANK)
                                .then(saveTransaction(transactionRequestDTO, Movement.CREDIT, TransactionType.INTERBANK));
                    } else {
                        // En transacción interbancaria, se envía luego a una cola o se encadena la operación
                        return saveTransaction(transactionRequestDTO, Movement.DEBIT, TransactionType.INTRABANK);
                                /*debit.then(
                                sendInterbankCreditTransaction(transactionRequestDTO)*/
                    }
                });


        /*return sufficientFunds(transactionRequestDTO)
                .flatMap(hasFunds -> {
                    if (!hasFunds) {
                        return Mono.error(new BusinessException("Transaction not allowed: insufficient funds."));
                    }

                    if ( isIntrabank(transactionRequestDTO) ){
                        return saveTransaction(transactionRequestDTO, Movement.DEBIT, TransactionType.INTERBANK)
                                .then(saveTransaction(transactionRequestDTO, Movement.CREDIT, TransactionType.INTERBANK));
                    }else {
                        return saveTransaction(transactionRequestDTO, Movement.DEBIT, TransactionType.INTRABANK);
                                //.then();
                        // Enviar a cola la transaccion tipo Deposito - Credito
                    }
                    /*return prepareTransaction(transactionRequestDTO);/ *saveTransaction(transactionRequestDTO, Movement.DEBIT, TransactionType.INTERBANK)
                            .then(saveTransaction(transactionRequestDTO, Movement.CREDIT, TransactionType.INTERBANK));* /
                });*/

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

    private Boolean isIntrabank(TransactionRequestDTO transactionRequestDTO) {
        return (transactionRequestDTO.getBankIdOrigin().compareTo(transactionRequestDTO.getBankIdDestination()) == 0);
    }

    private Mono<Boolean> sufficientFunds(TransactionRequestDTO transactionRequestDTO) {
        return accountsClient.findByBankIdAndAccountId(transactionRequestDTO.getBankIdOrigin(), transactionRequestDTO.getAccountIdOrigin())
                .map(accountsData -> accountsData.getAmount().compareTo(transactionRequestDTO.getAmount()) >= 0)
                .defaultIfEmpty(false);
    }

    private Boolean isValidTransactionBetweenAccounts(TransactionRequestDTO transactionRequestDTO) {
        return (transactionRequestDTO.getAccountIdOrigin().compareTo(transactionRequestDTO.getAccountIdDestination()) != 0);
    }

    private Mono<Transaction> saveTransaction(TransactionRequestDTO transactionRequestDTO, Movement movement,
                                              TransactionType transactionType) {

        Transaction trx = new Transaction();
        trx.setTypeTransaction(transactionType);
        trx.setDateTransaction(LocalDate.now());
        trx.setTypeMovement(movement);
        trx.setStateTransaction(State.COMPLETADA);

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
