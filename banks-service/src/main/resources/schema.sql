CREATE TABLE IF NOT EXISTS bank (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    webaddress VARCHAR(200)
);

INSERT INTO bank(name, country, webaddress) VALUES('Luke', 'Colombia', 'www.luke.co');
INSERT INTO bank(name, country, webaddress) VALUES('Bancolombia', 'Colombia', 'www.bancolombia.co');
INSERT INTO bank(name, country, webaddress) VALUES('Sure', 'Canada', 'www.suce.ca');
INSERT INTO bank(name, country, webaddress) VALUES('Nu', 'Brazil', 'www.nu.br');
INSERT INTO bank(name, country, webaddress) VALUES('MonopolyBank', 'United Stated', 'www.monopolibank.com');
