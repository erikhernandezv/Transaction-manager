package co.erikhdez.bankservice.config;

import co.erikhdez.bankservice.model.Bank;
import co.erikhdez.bankservice.repository.IBankRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner commandLineRunner(IBankRepository iBankRepository) {
        return args -> {
            iBankRepository.save(new Bank(null, "Luke", "Colombia", "www.luke.co"));
            iBankRepository.save(new Bank(null, "Bancolombia", "Colombia", "www.bancolombia.co"));
            iBankRepository.save(new Bank(null, "Sure", "Canada", "www.suce.ca"));
            iBankRepository.save(new Bank(null, "Nu", "Brazil", "www.nu.br"));
            iBankRepository.save(new Bank(null, "MonopolyBank", "United Stated", "www.monopolibank.com"));
        };
    }
}
