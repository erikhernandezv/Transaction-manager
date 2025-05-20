package co.erikhdez.transactionservice.dto;

import co.erikhdez.transactionservice.common.Movement;

import java.math.BigDecimal;

public class TransactionRequestDTO {
    private Movement typeMovement;
    private Long bankIdOrigin;
    private Long accountIdOrigin;
    private Long bankIdDestination;
    private Long accountIdDestination;
    private BigDecimal amount;

    public TransactionRequestDTO() {
    }

    public TransactionRequestDTO(Movement typeMovement, Long bankIdOrigin, Long accountIdOrigin, Long bankIdDestination,
                                 Long accountIdDestination, BigDecimal amount) {
        this.typeMovement = typeMovement;
        this.bankIdOrigin = bankIdOrigin;
        this.accountIdOrigin = accountIdOrigin;
        this.bankIdDestination = bankIdDestination;
        this.accountIdDestination = accountIdDestination;
        this.amount = amount;
    }

    public Movement getTypeMovement() {
        return typeMovement;
    }

    public void setTypeMovement(Movement typeMovement) {
        this.typeMovement = typeMovement;
    }

    public Long getBankIdOrigin() {
        return bankIdOrigin;
    }

    public void setBankIdOrigin(Long bankIdOrigin) {
        this.bankIdOrigin = bankIdOrigin;
    }

    public Long getAccountIdOrigin() {
        return accountIdOrigin;
    }

    public void setAccountIdOrigin(Long accountIdOrigin) {
        this.accountIdOrigin = accountIdOrigin;
    }

    public Long getBankIdDestination() {
        return bankIdDestination;
    }

    public void setBankIdDestination(Long bankIdDestination) {
        this.bankIdDestination = bankIdDestination;
    }

    public Long getAccountIdDestination() {
        return accountIdDestination;
    }

    public void setAccountIdDestination(Long accountIdDestination) {
        this.accountIdDestination = accountIdDestination;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
