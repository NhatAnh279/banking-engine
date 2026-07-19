package com.tommy.bankingengine.validation;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

public class StructuringDetector implements TransactionValidator {
    private TransactionValidator next;
    private final TransactionRepository transactionRepository;

    public StructuringDetector (TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override 
    public void setNext(TransactionValidator next) {
        this.next = next;
    }

    @Override
    public void validate(Transaction transaction, Account account) {

        List<Transaction> recentTransactions = transactionRepository
                .findBySourceAccountAndCreatedAtAfter(
                        transaction.getSourceAccount(),
                        LocalDateTime.now().minusHours(24)  // 24h trước
                );

        BigDecimal totalLast24h = recentTransactions.stream()
                                    .map(Transaction::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalWithCurrent = totalLast24h.add(transaction.getAmount());

        if (totalWithCurrent.compareTo(new BigDecimal("10000")) >= 0
            && transaction.getAmount().compareTo(new BigDecimal("10000")) < 0) {
        transaction.setStatus(Transaction.Status.FLAGGED);
        }

            if (next != null) {
                next.validate(transaction, account);
            }
    }
}
