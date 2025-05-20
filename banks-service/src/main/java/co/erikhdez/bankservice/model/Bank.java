package co.erikhdez.bankservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Bank {

    @Id
    private Long id;
    private String name;
    private String country;
    private String webaddress;

    public Bank() {
    }

    public Bank(Long id, String name, String country, String webaddress) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.webaddress = webaddress;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String contry) {
        this.country = contry;
    }

    public String getWebAddress() {
        return webaddress;
    }

    public void setWebaddress(String webaddress) {
        this.webaddress = webaddress;
    }
}
