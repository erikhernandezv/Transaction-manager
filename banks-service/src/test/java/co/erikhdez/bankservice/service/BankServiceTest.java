package co.erikhdez.bankservice.service;

import co.erikhdez.bankservice.model.Bank;
import co.erikhdez.bankservice.repository.IBankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class BankServiceTest {

    private IBankRepository bankRepository;
    private BankService bankService;

    @BeforeEach
    void setUp() {
        bankRepository = Mockito.mock(IBankRepository.class);
        bankService = new BankService(bankRepository);
    }

    @Test
    void getAll_ShouldReturnAllBanks() {
        Bank bank1 = new Bank(1L, "Bank A", "Colombia", "banka.com");
        Bank bank2 = new Bank(2L, "Bank B", "Chile", "bankb.com");

        Mockito.when(bankRepository.findAll()).thenReturn(Flux.just(bank1, bank2));

        StepVerifier.create(bankService.getAll())
                .expectNext(bank1)
                .expectNext(bank2)
                .verifyComplete();
    }

    @Test
    void getBankById_WhenExists_ShouldReturnBank() {
        Bank bank = new Bank(1L, "Bank A", "Colombia", "banka.com");

        Mockito.when(bankRepository.findById(1L)).thenReturn(Mono.just(bank));

        StepVerifier.create(bankService.getBankById(1L))
                .expectNext(bank)
                .verifyComplete();
    }

    @Test
    void getBankById_WhenNotFound_ShouldReturnError() {
        Mockito.when(bankRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(bankService.getBankById(99L))
                .expectErrorMatches(error -> error instanceof RuntimeException &&
                        error.getMessage().equals("Bank not found"))
                .verify();
    }

    @Test
    void createBank_ShouldSaveBank() {
        Bank bank = new Bank(null, "Bank A", "Colombia", "banka.com");

        Mockito.when(bankRepository.save(bank)).thenReturn(Mono.just(bank));

        StepVerifier.create(bankService.createBank(bank))
                .expectNext(bank)
                .verifyComplete();
    }

    @Test
    void updateBank_WhenExists_ShouldUpdateAndReturnBank() {
        Bank existing = new Bank(1L, "Old Name", "Old Country", "old.com");
        Bank updated = new Bank(null, "New Name", "New Country", "new.com");
        Bank saved = new Bank(1L, "New Name", "New Country", "new.com");

        Mockito.when(bankRepository.findById(1L)).thenReturn(Mono.just(existing));
        Mockito.when(bankRepository.save(Mockito.any(Bank.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(bankService.updateBank(1L, updated))
                .expectNext(saved)
                .verifyComplete();

        Mockito.verify(bankRepository).save(Mockito.argThat(b ->
                b.getName().equals("New Name") &&
                        b.getCountry().equals("New Country") &&
                        b.getWebAddress().equals("new.com")
        ));
    }

    @Test
    void updateBank_WhenNotFound_ShouldReturnError() {
        Mockito.when(bankRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(bankService.updateBank(99L, new Bank()))
                .expectErrorMatches(error -> error instanceof RuntimeException &&
                        error.getMessage().equals("Bank not found"))
                .verify();
    }
}
