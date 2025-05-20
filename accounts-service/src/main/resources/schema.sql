CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    balance DOUBLE NOT NULL,
    currency VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    bank_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL
    );

INSERT INTO accounts(account_number, balance, currency, active, bank_id, customer_id) VALUES('903212344568', 2987545, 'USD', TRUE, 1, 1);
INSERT INTO accounts(account_number, balance, currency, active, bank_id, customer_id) VALUES('903245674568', 159753, 'CO', TRUE, 1, 1);
INSERT INTO accounts(account_number, balance, currency, active, bank_id, customer_id) VALUES('903278914568', 999875461, 'GBP', TRUE, 1, 1);
INSERT INTO accounts(account_number, balance, currency, active, bank_id, customer_id) VALUES('903201594568', 123578, 'USD', TRUE, 1, 1);
INSERT INTO accounts(account_number, balance, currency, active, bank_id, customer_id) VALUES('903275304568', 985620, 'CO', TRUE, 1, 1);
