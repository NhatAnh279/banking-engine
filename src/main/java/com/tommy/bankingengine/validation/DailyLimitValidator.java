package com.tommy.bankingengine.validation;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;
import java.math.BigDecimal;

public class DailyLimitValidator implements TransactionValidator {
    private TransactionValidator next;

    @Override
    public void setNext(TransactionValidator next) {
        this.next = next;
    }

    @Override
    public void validate(Transaction transaction, Account account) {
        if (transaction.getAmount().compareTo(new BigDecimal("20000")) > 0 ) {
            throw new RuntimeException("Daily Limit Exceed");
        }

        if (next != null) {
            next.validate(transaction, account);
        }
    }
}
