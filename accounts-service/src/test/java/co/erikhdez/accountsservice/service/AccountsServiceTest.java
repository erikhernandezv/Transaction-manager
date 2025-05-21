package co.erikhdez.accountsservice.service;

import co.erikhdez.accountsservice.common.BankClient;
import co.erikhdez.accountsservice.common.Currency;
import co.erikhdez.accountsservice.dto.BankDTO;
import co.erikhdez.accountsservice.model.Accounts;
import co.erikhdez.accountsservice.repository.IAccountsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class AccountsServiceTest {

    @Mock
    private IAccountsRepository accountsRepository;

    @Mock
    private BankClient bankClient;

    @InjectMocks
    private AccountsService accountsService;

    private Accounts account;
    private BankDTO bankDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Accounts();
        account.setId(1L);
        account.setBankId(10L);
        account.setAccountNumber("123456");
        account.setAmount(BigDecimal.valueOf(1000.0));
        account.setCurrency(Currency.USD);
        account.setActive(true);
        account.setCustomerId(5L);

        bankDTO = new BankDTO();
        bankDTO.setId(10L);
        bankDTO.setName("Test Bank");
    }

    @Test
    void getAll_shouldReturnFluxOfAccountWithBankDTO() {
        when(accountsRepository.findAll()).thenReturn(Flux.just(account));
        when(bankClient.getBankById(10L)).thenReturn(Mono.just(bankDTO));

        StepVerifier.create(accountsService.getAll())
                .expectNextMatches(dto -> dto.getBank().getName().equals("Test Bank"))
                .verifyComplete();
    }

    @Test
    void getAccountsById_shouldReturnAccountWithBankDTO_whenFound() {
        when(accountsRepository.findById(1L)).thenReturn(Mono.just(account));
        when(bankClient.getBankById(10L)).thenReturn(Mono.just(bankDTO));

        StepVerifier.create(accountsService.getAccountsById(1L))
                .expectNextMatches(dto -> dto.getBank().getId().equals(10L))
                .verifyComplete();
    }

    @Test
    void getAccountsById_shouldReturnError_whenNotFound() {
        when(accountsRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(accountsService.getAccountsById(1L))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Account not found!"))
                .verify();
    }

    @Test
    void createAccounts_shouldSaveAccountAfterBankCheck() {
        when(bankClient.getBankById(10L)).thenReturn(Mono.just(bankDTO));
        when(accountsRepository.save(account)).thenReturn(Mono.just(account));

        StepVerifier.create(accountsService.createAccounts(account))
                .expectNext(account)
                .verifyComplete();
    }

    @Test
    void updateBank_shouldUpdateAndReturnAccount_whenFound() {
        Accounts updated = new Accounts();
        updated.setBankId(10L);
        updated.setAccountNumber("654321");
        updated.setAmount(BigDecimal.valueOf(2000.0));
        updated.setCurrency(Currency.EUR);
        updated.setActive(false);

        Accounts expected = new Accounts();
        expected.setId(1L);
        expected.setBankId(10L);
        expected.setAccountNumber("654321");
        expected.setAmount(BigDecimal.valueOf(2000.0));
        expected.setCurrency(Currency.EUR);
        expected.setActive(false);
        expected.setCustomerId(5L);

        when(accountsRepository.findById(1L)).thenReturn(Mono.just(account));
        when(bankClient.getBankById(10L)).thenReturn(Mono.just(bankDTO));
        when(accountsRepository.save(any())).thenReturn(Mono.just(expected));

        StepVerifier.create(accountsService.updateBank(1L, updated))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void updateBank_shouldReturnError_whenAccountNotFound() {
        when(accountsRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(accountsService.updateBank(1L, account))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Accounts not found"))
                .verify();
    }

    @Test
    void findByBankIdAndAccountId_shouldReturnMonoAccount() {
        when(accountsRepository.findByBankIdAndAccountId(10L, 1L)).thenReturn(Mono.just(account));

        StepVerifier.create(accountsService.findByBankIdAndAccountId(10L, 1L))
                .expectNext(account)
                .verifyComplete();
    }
}
