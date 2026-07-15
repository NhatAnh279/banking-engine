package com.tommy.bankingengine.validation;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.model.Transaction;

public class TransactionValidator {
    
    public interface TransactionValidator (Transaction transaction, Account account) {
        
        void validate(Transaction transaction, Account account);  // kiểm tra
        void setNext(TransactionValidator next);  
        

    }
}
