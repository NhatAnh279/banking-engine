package com.tommy.bankingengine.validation;

import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.model.Transaction;

public interface TransactionValidator {
    void validate(Transaction transaction, Account account);
    void setNext(TransactionValidator next);
}