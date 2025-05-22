package co.erikhdez.transactionservice.service;

import co.erikhdez.transactionservice.common.*;
import co.erikhdez.transactionservice.dto.AccountsDTO;
import co.erikhdez.transactionservice.dto.TransactionRequestDTO;
import co.erikhdez.transactionservice.event.EventPublisher;
import co.erikhdez.transactionservice.exception.BusinessException;
import co.erikhdez.transactionservice.model.Transaction;
import co.erikhdez.transactionservice.repository.ITransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private ITransactionService transactionRepository;
    private AccountsClient accountsClient;
    private EventPublisher eventPublisher;
    private TransactionService transactionService;
    private TransactionUtils trxUtils;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(ITransactionService.class);
        accountsClient = mock(AccountsClient.class);
        eventPublisher = mock(EventPublisher.class);
        trxUtils = mock(TransactionUtils.class);
        transactionService = new TransactionService(transactionRepository, accountsClient, eventPublisher, trxUtils);
    }

    @Test
    void getAll_ShouldReturnAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(Flux.just(new Transaction()));

        StepVerifier.create(transactionService.getAll())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getBankById_WhenFound_ShouldReturnTransaction() {
        Transaction trx = new Transaction();
        when(transactionRepository.findById(1L)).thenReturn(Mono.just(trx));

        StepVerifier.create(transactionService.getBankById(1L))
                .expectNext(trx)
                .verifyComplete();
    }

    @Test
    void getBankById_WhenNotFound_ShouldReturnError() {
        when(transactionRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(transactionService.getBankById(1L))
                .expectErrorMatches(err -> err instanceof RuntimeException && err.getMessage().equals("Transaction not found"))
                .verify();
    }

    @Test
    void createTransaction_WhenSameAccount_ShouldReturnError() {
        TransactionRequestDTO dto = new TransactionRequestDTO(Movement.CREDIT, 1L, 1L, 1L, 1L, new BigDecimal("100.00"));

        StepVerifier.create(transactionService.createTransaction(dto))
                .expectErrorMatches(err -> err instanceof BusinessException && err.getMessage().contains("destination account is equal to origin"))
                .verify();
    }

    @Test
    void saveTransaction_ShouldBuildCorrectIntrabankTransactions() {
        TransactionRequestDTO dto = new TransactionRequestDTO(
                Movement.DEBIT, 1L, 2L, 1L, 3L, new BigDecimal("100.00")
        );

        when(accountsClient.findByBankIdAndAccountId(any(), any()))
                .thenReturn(Mono.just(new AccountsDTO(1L, "01254879543", new BigDecimal("200.00"), Currency.USD, 1L)));

        when(transactionRepository.save(any())).thenAnswer(invocation -> {
            Transaction trx = invocation.getArgument(0);
            trx.setAmount(BigDecimal.ZERO);
            return Mono.just(trx);
        });

        StepVerifier.create(transactionService.createTransaction(dto))
                .expectNextCount(1)
                .verifyComplete();

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(2)).save(captor.capture());

        var transactions = captor.getAllValues();
        assertEquals(2, transactions.size());

        Transaction debit = transactions.get(0);
        Transaction credit = transactions.get(1);

        assertEquals(Movement.DEBIT, debit.getTypeMovement());
        assertEquals(Movement.CREDIT, credit.getTypeMovement());
        assertEquals(TransactionType.INTRABANK, debit.getTypeTransaction());
    }

    @Test
    void updateTransaction_WhenFound_ShouldUpdateTransaction() {
        Transaction existing = new Transaction();
        existing.setAmount(BigDecimal.TEN);
        existing.setStateTransaction(State.PENDIENTE);

        Transaction updated = new Transaction();
        updated.setAmount(BigDecimal.ONE);
        updated.setStateTransaction(State.COMPLETADA);

        when(transactionRepository.findById(1L)).thenReturn(Mono.just(existing));
        when(transactionRepository.save(any())).thenReturn(Mono.just(updated));

        StepVerifier.create(transactionService.updateTransaction(1L, updated))
                .expectNext(updated)
                .verifyComplete();
    }

    @Test
    void updateTransaction_WhenNotFound_ShouldReturnError() {
        when(transactionRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(transactionService.updateTransaction(1L, new Transaction()))
                .expectErrorMatches(err -> err instanceof RuntimeException && err.getMessage().equals("Transaction not found"))
                .verify();
    }
}
