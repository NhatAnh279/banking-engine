package com.tommy.bankingengine.validation;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;

public class BalanceValidator implements TransactionValidator {
    private TransactionValidator next;

    @Override
    public void setNext(TransactionValidator next) {
        this.next = next;
    }

    @Override
    public void validate(Transaction transaction, Account account) {
        if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new RuntimeException("Insufficient Balance");
        }

        if (next != null) {
            next.validate(transaction, account);
        }
    }


}
