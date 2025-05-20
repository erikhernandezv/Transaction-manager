package co.erikhdez.bankservice.controller;

import co.erikhdez.bankservice.model.Bank;
import co.erikhdez.bankservice.service.BankService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public Flux<Bank> getBanks() {
        return bankService.getAll();
    }

    @GetMapping("/{bankId}")
    public Mono<Bank> getBankById(@PathVariable Long bankId) {
        return bankService.getBankById(bankId);
    }

    @PostMapping
    public Mono<Bank> createBank(@RequestBody Bank bank) {
        return bankService.createBank(bank);
    }

    @PutMapping("/{bankId}")
    public Mono<Bank> updateBank(@PathVariable Long bankId, @RequestBody Bank bank) {
        return bankService.updateBank(bankId, bank);
    }
}
