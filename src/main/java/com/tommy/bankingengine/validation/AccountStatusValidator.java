package com.tommy.bankingengine.validation;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.model.Transaction;

public class AccountStatusValidator implements TransactionValidator {
    private TransactionValidator next;

    @Override
    public void setNext(TransactionValidator next) {
        this.next = next;
    }

    @Override
    public void validate(Transaction transaction, Account account) {
        if (account.getStatus() == Account.Status.FROZEN) {
            throw new RuntimeException ("Account is not active, please contact our customer service");
        }
        
        if (next != null) {
            next.validate(transaction, account);
        }
    }
}