package com.tommy.bankingengine.validation;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;
import static org.junit.jupiter.api.Assertions.*;

public class FraudValidatorTest {
    
    @Test
        public void shouldFlag_whenExceed() {
        Account account = Account.builder()
                    .balance(new BigDecimal("10000"))
                    .status(Account.Status.ACTIVE)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("25000"))
                    .type(Transaction.Type.TRANSFER)
                    .status(Transaction.Status.PENDING)
                    .build();

        FraudValidator validator = new FraudValidator();
        validator.validate(transaction, account);

    assertEquals(Transaction.Status.FLAGGED, transaction.getStatus()); 
    }

    @Test
        public void shouldNotFlag_whenBelow() {
        Account account = Account.builder()
                    .balance(new BigDecimal("10000"))
                    .status(Account.Status.ACTIVE)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("5000"))
                    .type(Transaction.Type.TRANSFER)
                    .status(Transaction.Status.PENDING)
                    .build();

        FraudValidator validator = new FraudValidator();
        validator.validate(transaction, account);

        assertEquals(Transaction.Status.PENDING, transaction.getStatus());    
    }

    @Test
        public void shouldNotFlag_whenEqual() {
        Account account = Account.builder()
                    .balance(new BigDecimal("10000"))
                    .status(Account.Status.ACTIVE)
                    .build();

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("10000"))
                    .type(Transaction.Type.TRANSFER)
                    .status(Transaction.Status.PENDING)
                    .build();

        FraudValidator validator = new FraudValidator();
        validator.validate(transaction, account);

        assertEquals(Transaction.Status.PENDING, transaction.getStatus());    
    }
}
