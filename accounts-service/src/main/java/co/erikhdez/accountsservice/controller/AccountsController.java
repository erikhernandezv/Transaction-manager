package co.erikhdez.accountsservice.controller;

import co.erikhdez.accountsservice.dto.AccountWithBankDTO;
import co.erikhdez.accountsservice.model.Accounts;
import co.erikhdez.accountsservice.service.AccountsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping
    public Flux<AccountWithBankDTO> getAccounts() {
        return accountsService.getAll();
    }

    @GetMapping("/{accountId}")
    public Mono<AccountWithBankDTO> getBankById(@PathVariable Long accountId) {
        return accountsService.getAccountsById(accountId);
    }

    @PostMapping
    public Mono<Accounts> createBank(@RequestBody Accounts accounts) {
        return accountsService.createAccounts(accounts);
    }

    @PutMapping("/{accountsId}")
    public Mono<Accounts> updateBank(@PathVariable Long accountsId, @RequestBody Accounts accounts) {
        return accountsService.updateBank(accountsId, accounts);
    }
}
