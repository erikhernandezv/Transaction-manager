package co.erikhdez.transactionservice.model;

import co.erikhdez.transactionservice.common.Movement;
import co.erikhdez.transactionservice.common.State;
import co.erikhdez.transactionservice.common.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table
public class Transaction {

    @Id
    private Long id;
    private Movement typeMovement;
    private Long bankId;
    private Long accountId;
    private BigDecimal amount;
    private TransactionType typeTransaction;
    private LocalDate dateTransaction;
    private State stateTransaction;

    public Transaction() {
    }

    public Transaction(Long id, Movement typeMovement, Long bankId, Long accountId, BigDecimal amount,
                       TransactionType typeTransaction, LocalDate dateTransaction, State stateTransaction) {
        this.id = id;
        this.typeMovement = typeMovement;
        this.bankId = bankId;
        this.accountId = accountId;
        this.amount = amount;
        this.typeTransaction = typeTransaction;
        this.dateTransaction = dateTransaction;
        this.stateTransaction = stateTransaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movement getTypeMovement() {
        return typeMovement;
    }

    public void setTypeMovement(Movement typeMovement) {
        this.typeMovement = typeMovement;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(TransactionType typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public LocalDate getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDate dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public State getStateTransaction() {
        return stateTransaction;
    }

    public void setStateTransaction(State stateTransaction) {
        this.stateTransaction = stateTransaction;
    }
}
