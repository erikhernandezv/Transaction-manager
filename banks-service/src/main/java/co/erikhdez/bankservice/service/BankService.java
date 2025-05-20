package co.erikhdez.bankservice.service;

import co.erikhdez.bankservice.model.Bank;
import co.erikhdez.bankservice.repository.IBankRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankService {

    private final IBankRepository bankRepository;

    public BankService(IBankRepository iBankRepository) {
        this.bankRepository = iBankRepository;
    }

    public Flux<Bank> getAll() {
        return bankRepository.findAll();
    }

    public Mono<Bank> getBankById(Long id) {
        return bankRepository.findById(id)
                .switchIfEmpty(
                        Mono.error( new RuntimeException("Bank not found") )
                );
    }

    public Mono<Bank> createBank(Bank bank) {
        return bankRepository.save(bank);
    }

    public Mono<Bank> updateBank(Long id, Bank updatedBank) {
        return bankRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Bank not found")))
                .flatMap(existingBank -> {
                    existingBank.setName(updatedBank.getName());
                    existingBank.setCountry(updatedBank.getCountry());
                    existingBank.setWebaddress(updatedBank.getWebAddress());
                    return bankRepository.save(existingBank);
                });
    }
}
