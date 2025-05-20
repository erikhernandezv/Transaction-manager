package co.erikhdez.accountsservice.dto;

import co.erikhdez.accountsservice.common.Currency;

import java.math.BigDecimal;

public class AccountWithBankDTO {
    private Long accountId;
    private String accountNumber;
    private BigDecimal accountBalance;
    private Currency accountCurrency;
    private Boolean accountActive;
    private BankDTO bank;
    private Long customerId;

    public AccountWithBankDTO() {
    }

    public AccountWithBankDTO(Long accountId, String accountNumber, BigDecimal accountBalance,
                              Currency accountCurrency, Boolean accountActive, BankDTO bank, Long customerId) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.accountCurrency = accountCurrency;
        this.accountActive = accountActive;
        this.bank = bank;
        this.customerId = customerId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Currency getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(Currency accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public Boolean getAccountActive() {
        return accountActive;
    }

    public void setAccountActive(Boolean accountActive) {
        this.accountActive = accountActive;
    }

    public BankDTO getBank() {
        return bank;
    }

    public void setBank(BankDTO bank) {
        this.bank = bank;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}

