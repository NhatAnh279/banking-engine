package com.tommy.bankingengine.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.RequestParam;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.service.TransactionService;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService ) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(@RequestParam BigDecimal amount,
                                            @RequestParam Transaction.Type type,
                                            @RequestParam String sourceAccount,
                                            @RequestParam(required = false) String destinationAccount) {
        
            Transaction transaction = Transaction.builder() 
            .amount(amount)
            .type(type)
            .sourceAccount(sourceAccount)
            .destinationAccount(destinationAccount)
            .status(Transaction.Status.PENDING)
            .createdAt(java.time.LocalDateTime.now())
            .build();
            return transactionService.processTransaction(transaction);
        
         }
    
    @GetMapping("/history/{sourceAccount}")
    public List<Transaction> getTransactionHistory(@PathVariable String sourceAccount) {
        return transactionService.getTransactionHistory(sourceAccount);
    }

    }

