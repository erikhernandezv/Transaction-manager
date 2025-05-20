package co.erikhdez.accountsservice.dto;

public class BankDTO {
    private Long id;
    private String name;
    private String webAddress;

    public BankDTO() {
    }

    public BankDTO(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.webAddress = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }
}
