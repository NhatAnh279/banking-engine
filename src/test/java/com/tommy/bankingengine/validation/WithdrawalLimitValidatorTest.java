package com.tommy.bankingengine.validation;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.TransactionRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class WithdrawalLimitValidatorTest {

    @Test
    public void shouldThrow_whenExceedMonthlyLimit() {
        
        TransactionRepository mockRepo = mock(TransactionRepository.class);
        when(mockRepo.countBySourceAccountAndTypeAndCreatedAtAfter(any(), any(), any()))
                .thenReturn(6L);

        Account account = Account.builder()
                .accountType(Account.AccountType.SAVING)
                .status(Account.Status.ACTIVE)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("1000"))
                .type(Transaction.Type.WITHDRAWAL)
                .sourceAccount("ACC-123")
                .build();

        WithdrawalLimitValidator validator = new WithdrawalLimitValidator(mockRepo);

        assertThrows(RuntimeException.class, () -> {
            validator.validate(transaction, account);
        });
    }

    @Test
    public void shouldPass_whenBelowMonthlyLimit() {

        TransactionRepository mockRepo = mock(TransactionRepository.class);
        when(mockRepo.countBySourceAccountAndTypeAndCreatedAtAfter(any(), any(), any()))
                .thenReturn(2L);

        Account account = Account.builder()
                .accountType(Account.AccountType.SAVING)
                .status(Account.Status.ACTIVE)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("1000"))
                .type(Transaction.Type.WITHDRAWAL)
                .sourceAccount("ACC-123")
                .build();

        WithdrawalLimitValidator validator = new WithdrawalLimitValidator(mockRepo);

        assertDoesNotThrow(() -> {
            validator.validate(transaction, account);
        });
    }
}
