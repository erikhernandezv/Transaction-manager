package co.erikhdez.transactionservice.repository;

import co.erikhdez.transactionservice.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionService extends ReactiveCrudRepository<Transaction, Long> {
}
