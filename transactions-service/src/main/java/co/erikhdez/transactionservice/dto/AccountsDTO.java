package co.erikhdez.transactionservice.dto;

import co.erikhdez.transactionservice.common.Currency;

import java.math.BigDecimal;

public class AccountsDTO {
    private Long id;
    private String accountNumber;
    private BigDecimal amount;
    private Currency currency;
    private Long bankId;

    public AccountsDTO() {
    }

    public AccountsDTO(Long id, String accountNumber, BigDecimal amount, Currency currency, Long bankId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.currency = currency;
        this.bankId = bankId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }
}
