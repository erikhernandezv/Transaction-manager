package co.erikhdez.transactionservice.controller;

import co.erikhdez.transactionservice.dto.TransactionRequestDTO;
import co.erikhdez.transactionservice.model.Transaction;
import co.erikhdez.transactionservice.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public Flux<Transaction> getTransactions() {
        return transactionService.getAll();
    }

    @GetMapping("/{bankId}")
    public Mono<Transaction> getBankById(@PathVariable Long bankId) {
        return transactionService.getBankById(bankId);
    }

    @PostMapping
    public Mono<Transaction> createTrx(@RequestBody TransactionRequestDTO trxRequestDTO) {
        return transactionService.createTransaction(trxRequestDTO);
    }

    @PutMapping("/{trxId}")
    public Mono<Transaction> updateTrx(@PathVariable Long trxId, @RequestBody Transaction trxRequest) {
        return transactionService.updateTransaction(trxId, trxRequest);
    }
}
