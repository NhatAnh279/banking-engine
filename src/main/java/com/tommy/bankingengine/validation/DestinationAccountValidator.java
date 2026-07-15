package com.tommy.bankingengine.validation;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.repository.AccountRepository;

public class DestinationAccountValidator implements TransactionValidator {
    private TransactionValidator next;
    private final AccountRepository accountRepository;

    
    public DestinationAccountValidator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void setNext(TransactionValidator next) {
        this.next = next;
    }

    @Override 
    public void validate(Transaction transaction, Account account) {
        if (transaction.getType() == Transaction.Type.TRANSFER) {
            if (accountRepository.findByAccountNumber(transaction.getDestinationAccount()).isEmpty()) {
                throw new RuntimeException("Receiver not found");
            }
        
        }

        if (next != null) {
        next.validate(transaction, account);
        }
    }


    
}
