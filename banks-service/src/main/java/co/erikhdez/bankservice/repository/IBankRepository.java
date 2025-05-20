package co.erikhdez.bankservice.repository;

import co.erikhdez.bankservice.model.Bank;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBankRepository extends ReactiveCrudRepository<Bank, Long> {
}
