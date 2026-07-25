package com.tommy.bankingengine.validation;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;
import static org.junit.jupiter.api.Assertions.*;

public class BalanceValidatorTest {
    @Test
    public void shouldThrow_whenInsufficient () {
        Account account = Account.builder()
                    .balance(new BigDecimal("1000"))
                    .status(Account.Status.ACTIVE)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("2000"))
                    .type(Transaction.Type.TRANSFER)
                    .build();

        BalanceValidator validator = new BalanceValidator();

        assertThrows(RuntimeException.class, () -> {
            validator.validate(transaction, account);
        });
    }

    @Test
    public void shouldPass_whenSufficient () {
        Account account = Account.builder()
                    .balance(new BigDecimal("3000"))
                    .status(Account.Status.ACTIVE)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("2000"))
                    .type(Transaction.Type.TRANSFER)
                    .build();

        BalanceValidator validator = new BalanceValidator();

        assertDoesNotThrow(() -> {
            validator.validate(transaction, account);
        });
    }        
}


