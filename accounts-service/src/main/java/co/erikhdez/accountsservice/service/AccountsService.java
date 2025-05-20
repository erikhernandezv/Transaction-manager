package co.erikhdez.accountsservice.service;

import co.erikhdez.accountsservice.common.BankClient;
import co.erikhdez.accountsservice.dto.AccountWithBankDTO;
import co.erikhdez.accountsservice.dto.BankDTO;
import co.erikhdez.accountsservice.model.Accounts;
import co.erikhdez.accountsservice.repository.IAccountsRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountsService {

    private final IAccountsRepository accountsRepository;
    private final BankClient bankClient;

    public AccountsService(IAccountsRepository accountsRepository, BankClient bankClient) {
        this.accountsRepository = accountsRepository;
        this.bankClient = bankClient;
    }

    public Flux<AccountWithBankDTO> getAll() {
        return accountsRepository.findAll()
                .flatMap(account ->
                        bankClient.getBankById(account.getBankId())
                                .flatMap(bankDTO -> Mono.just(buildAccountWithBankDTO(account, bankDTO)))
                );
    }

    public Mono<AccountWithBankDTO> getAccountsById(Long id) {
        return accountsRepository.findById(id)
                .flatMap(account ->
                        bankClient.getBankById(account.getBankId())
                                .map(bankDTO -> buildAccountWithBankDTO(account, bankDTO))
                )
                .switchIfEmpty(
                        Mono.error(new RuntimeException("Account not found!"))
                );
    }

    public Mono<Accounts> createAccounts(Accounts accounts) {
        return bankClient.getBankById(accounts.getBankId())
                .then(accountsRepository.save(accounts));
    }

    public Mono<Accounts> updateBank(Long id, Accounts updatedAccounts) {
        return accountsRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Accounts not found")))
                .flatMap(existingAccounts ->
                        bankClient.getBankById(updatedAccounts.getBankId())
                                .then(Mono.defer(() -> {
                                    existingAccounts.setAccountNumber(updatedAccounts.getAccountNumber());
                                    existingAccounts.setAmount(updatedAccounts.getAmount());
                                    existingAccounts.setCurrency(updatedAccounts.getCurrency());
                                    existingAccounts.setActive(updatedAccounts.getActive());
                                    return accountsRepository.save(existingAccounts);
                                }))
                );
    }

    private AccountWithBankDTO buildAccountWithBankDTO(Accounts account, BankDTO bankDTO) {
        AccountWithBankDTO dto = new AccountWithBankDTO();
        dto.setAccountId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountBalance(account.getAmount());
        dto.setAccountCurrency(account.getCurrency());
        dto.setAccountActive(account.getActive());
        dto.setBank(bankDTO);
        dto.setCustomerId(account.getCustomerId());
        return dto;
    }

    Mono<Accounts> findByBankIdAndAccountId(Long bankId, Long accountId){
        return accountsRepository.findByBankIdAndAccountId(bankId, accountId);
    }
}
