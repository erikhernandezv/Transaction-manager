package co.erikhdez.accountsservice.repository;

import co.erikhdez.accountsservice.model.Accounts;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IAccountsRepository extends ReactiveCrudRepository<Accounts, Long> {
    Mono<Accounts> findByBankIdAndAccountId(Long bankId, Long accountId);
}
