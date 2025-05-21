package co.erikhdez.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "routes")
public class RouteProperties {

    private String banksServiceUrl;
    private String accountsServiceUrl;
    private String transactionServiceUrl;
    private String pathBank;
    private String pathAccount;
    private String pathTransaction;
    private String banksServiceRoute;
    private String accountsServiceRoute;
    private String transactionServiceRoute;

    public String getBanksServiceUrl() {
        return banksServiceUrl;
    }

    public void setBanksServiceUrl(String banksServiceUrl) {
        this.banksServiceUrl = banksServiceUrl;
    }

    public String getAccountsServiceUrl() {
        return accountsServiceUrl;
    }

    public void setAccountsServiceUrl(String accountsServiceUrl) {
        this.accountsServiceUrl = accountsServiceUrl;
    }

    public String getTransactionServiceUrl() {
        return transactionServiceUrl;
    }

    public void setTransactionServiceUrl(String transactionServiceUrl) {
        this.transactionServiceUrl = transactionServiceUrl;
    }

    public String getPathBank() {
        return pathBank;
    }

    public void setPathBank(String pathBank) {
        this.pathBank = pathBank;
    }

    public String getPathAccount() {
        return pathAccount;
    }

    public void setPathAccount(String pathAccount) {
        this.pathAccount = pathAccount;
    }

    public String getPathTransaction() {
        return pathTransaction;
    }

    public void setPathTransaction(String pathTransaction) {
        this.pathTransaction = pathTransaction;
    }

    public String getBanksServiceRoute() {
        return banksServiceRoute;
    }

    public void setBanksServiceRoute(String banksServiceRoute) {
        this.banksServiceRoute = banksServiceRoute;
    }

    public String getAccountsServiceRoute() {
        return accountsServiceRoute;
    }

    public void setAccountsServiceRoute(String accountsServiceRoute) {
        this.accountsServiceRoute = accountsServiceRoute;
    }

    public String getTransactionServiceRoute() {
        return transactionServiceRoute;
    }

    public void setTransactionServiceRoute(String transactionServiceRoute) {
        this.transactionServiceRoute = transactionServiceRoute;
    }
}
