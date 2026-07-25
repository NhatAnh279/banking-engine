package com.tommy.bankingengine.validation;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;

public class AccountStatusValidatorTest {
    
    @Test
    public void shouldThrow_whenFrozen () {
        Account account = Account.builder()
                    .status(Account.Status.FROZEN)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("1000"))
                    .type(Transaction.Type.WITHDRAWAL)
                    .build();

        AccountStatusValidator validator = new AccountStatusValidator();
        assertThrows(RuntimeException.class, () -> {
            validator.validate(transaction, account);
        });
    }

    @Test
    public void shouldPass_whenActive () {
        Account account = Account.builder()
                    .status(Account.Status.ACTIVE)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("1000"))
                    .type(Transaction.Type.WITHDRAWAL)
                    .build();

        AccountStatusValidator validator = new AccountStatusValidator();
        assertDoesNotThrow(() -> {
            validator.validate(transaction, account);
        });
    }
}
