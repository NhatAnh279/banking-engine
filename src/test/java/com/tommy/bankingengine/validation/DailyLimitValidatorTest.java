package com.tommy.bankingengine.validation;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;
import static org.junit.jupiter.api.Assertions.*;

public class DailyLimitValidatorTest {
    
    @Test
    public void shouldThrow_whenExceedLimit() {
        Account account = Account.builder()
                    .balance(new BigDecimal("100000"))
                    .status(Account.Status.ACTIVE)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("25000"))
                    .type(Transaction.Type.TRANSFER)
                    .build();

        DailyLimitValidator validator = new DailyLimitValidator();

        assertThrows(RuntimeException.class, () -> {
            validator.validate(transaction, account);
        });
    }

    @Test
    public void shouldPass_whenBelowLimit() {
        Account account = Account.builder()
                    .balance(new BigDecimal("100000"))
                    .status(Account.Status.ACTIVE)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("15000"))
                    .type(Transaction.Type.TRANSFER)
                    .build();

        DailyLimitValidator validator = new DailyLimitValidator();

        assertDoesNotThrow(() -> {
            validator.validate(transaction, account);
        });
    }

    @Test
    public void shouldPass_whenExactlyAtLimit() {
        Account account = Account.builder()
                    .balance(new BigDecimal("100000"))
                    .status(Account.Status.ACTIVE)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("20000"))
                    .type(Transaction.Type.TRANSFER)
                    .build();

        DailyLimitValidator validator = new DailyLimitValidator();

        assertDoesNotThrow(() -> {
            validator.validate(transaction, account);
        });
    }
}