package com.tommy.bankingengine.service;
import com.tommy.bankingengine.model.Transaction;

import org.springframework.stereotype.Service;
import java.util.List;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.AccountRepository;
import com.tommy.bankingengine.repository.TransactionRepository;
import com.tommy.bankingengine.validation.AccountStatusValidator;
import com.tommy.bankingengine.validation.BalanceValidator;
import com.tommy.bankingengine.validation.DailyLimitValidator;
import com.tommy.bankingengine.validation.DestinationAccountValidator;
import com.tommy.bankingengine.validation.FraudValidator;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
}

    public Transaction processTransaction(Transaction transaction) {
        Account account = accountRepository.findByAccountNumber(transaction.getSourceAccount())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        //Connecting chain for the validator
        AccountStatusValidator v1 = new AccountStatusValidator();
        BalanceValidator v2 = new BalanceValidator();
        DailyLimitValidator v3 = new DailyLimitValidator();
        DestinationAccountValidator v4 = new DestinationAccountValidator(accountRepository);
        FraudValidator v5 = new FraudValidator();

        v1.setNext(v2);
        v2.setNext(v3);
        v3.setNext(v4);
        v4.setNext(v5);

        v1.validate(transaction, account);

        if (transaction.getStatus() == Transaction.Status.FLAGGED) {
            return transactionRepository.save(transaction);
        }

        switch (transaction.getType()) {
            
            case DEPOSIT:
                account.setBalance(account.getBalance().add(transaction.getAmount()));
                break;
            
            case WITHDRAWAL:
                account.setBalance(account.getBalance().subtract(transaction.getAmount()));
                break;
            
            case TRANSFER:
                //source account handling
                account.setBalance(account.getBalance().subtract(transaction.getAmount()));

                //destination account handling
                Account destination = accountRepository.findByAccountNumber(transaction.getDestinationAccount())
                        .orElseThrow(() -> new RuntimeException("Receiver not found"));
                destination.setBalance(destination.getBalance().add(transaction.getAmount()));
                accountRepository.save(destination);
                break;
        }

        transaction.setStatus(Transaction.Status.COMPLETED);
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory(String sourceAccount) {
        accountRepository.findByAccountNumber(sourceAccount)
            .orElseThrow(() -> new RuntimeException("Account not found"));
    return transactionRepository.findBySourceAccount(sourceAccount);
        
    }
}
