package com.tommy.bankingengine.validation;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.TransactionRepository;
import java.time.LocalDateTime;

public class WithdrawalLimitValidator implements TransactionValidator {
    private TransactionValidator next;
    private final TransactionRepository transactionRepository;

    public WithdrawalLimitValidator(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void setNext(TransactionValidator next) {
        this.next = next;
    }

    @Override
    public void validate(Transaction transaction, Account account) {
        // Savings accounts: max 6 withdrawals per month (industry standard)
        if (account.getAccountType() == Account.AccountType.SAVING
                && transaction.getType() == Transaction.Type.WITHDRAWAL) {

            long count = transactionRepository.countBySourceAccountAndTypeAndCreatedAtAfter(
                    transaction.getSourceAccount(),
                    Transaction.Type.WITHDRAWAL,
                    LocalDateTime.now().withDayOfMonth(1).withHour(0)
            );

            if (count >= 6) {
                throw new RuntimeException("Monthly withdrawal limit exceeded (max 6 for Savings accounts)");
            }
        }

        if (next != null) {
            next.validate(transaction, account);
        }
    }
}