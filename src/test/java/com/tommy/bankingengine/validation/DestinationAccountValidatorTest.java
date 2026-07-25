package com.tommy.bankingengine.validation;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.AccountRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

public class DestinationAccountValidatorTest {
    
    @Test
    public void shouldPass_whenDestinationExists() {
        AccountRepository mockRepo = mock(AccountRepository.class);
        when(mockRepo.findByAccountNumber("ACC-456"))
                                    .thenReturn(Optional.of(Account.builder().build()));

        Account account = Account.builder()
                .accountType(Account.AccountType.SAVING)
                .status(Account.Status.ACTIVE)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("1000"))
                .type(Transaction.Type.TRANSFER)
                .destinationAccount("ACC-456")
                .build();

        DestinationAccountValidator validator = new DestinationAccountValidator(mockRepo);

        assertDoesNotThrow(() -> {
            validator.validate(transaction, account);
        });
    }

    @Test
    public void shouldThrow_whenDestinationNotExists() {
        AccountRepository mockRepo = mock(AccountRepository.class);
        when(mockRepo.findByAccountNumber("ACC-999"))
                .thenReturn(Optional.empty());

        Account account = Account.builder()
                .accountType(Account.AccountType.SAVING)
                .status(Account.Status.ACTIVE)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("1000"))
                .type(Transaction.Type.TRANSFER)
                .destinationAccount("ACC-999")
                .build();

        DestinationAccountValidator validator = new DestinationAccountValidator(mockRepo);

        assertThrows(RuntimeException.class, () -> {
            validator.validate(transaction, account);
        });
    }

    @Test
    public void shouldSkip_whenTypeNotTransfer() {

        Account account = Account.builder()
                .accountType(Account.AccountType.SAVING)
                .status(Account.Status.ACTIVE)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("1000"))
                .type(Transaction.Type.DEPOSIT)
                .destinationAccount("ACC-999")
                .build();

        AccountRepository mockRepo = mock(AccountRepository.class);
        DestinationAccountValidator validator = new DestinationAccountValidator(mockRepo);

        assertDoesNotThrow(() -> {
            validator.validate(transaction, account);
        });
    }
    
}
