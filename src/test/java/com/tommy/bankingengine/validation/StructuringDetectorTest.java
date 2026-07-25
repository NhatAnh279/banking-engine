package com.tommy.bankingengine.validation;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.TransactionRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;

public class StructuringDetectorTest {
    
    @Test
    public void shouldFlag_whenSumAmountExceed() {
        TransactionRepository mockRepo = mock(TransactionRepository.class);
            List<Transaction> recentTransactions = List.of(
        Transaction.builder().amount(new BigDecimal("5000")).build(),
        Transaction.builder().amount(new BigDecimal("4000")).build()
        );

        when(mockRepo.findBySourceAccountAndCreatedAtAfter(any(), any()))
                                                .thenReturn(recentTransactions);

        Account account = Account.builder()
                .accountType(Account.AccountType.SAVING)
                .status(Account.Status.ACTIVE)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("3000"))
                .type(Transaction.Type.TRANSFER)
                .sourceAccount("ACC-123")         
                .status(Transaction.Status.PENDING) 
                .build();

        StructuringDetector validator = new StructuringDetector(mockRepo);
        validator.validate(transaction, account);
        assertEquals(Transaction.Status.FLAGGED, transaction.getStatus()); 
    }        

    @Test
    public void shouldPending_whenSumAmountBelow() {
        TransactionRepository mockRepo = mock(TransactionRepository.class);
            List<Transaction> recentTransactions = List.of(
        Transaction.builder().amount(new BigDecimal("5000")).build(),
        Transaction.builder().amount(new BigDecimal("4000")).build()
        );

        when(mockRepo.findBySourceAccountAndCreatedAtAfter(any(), any()))
                                                .thenReturn(recentTransactions);

        Account account = Account.builder()
                .accountType(Account.AccountType.SAVING)
                .status(Account.Status.ACTIVE)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("500"))
                .type(Transaction.Type.TRANSFER)
                .sourceAccount("ACC-123")         
                .status(Transaction.Status.PENDING) 
                .build();

        StructuringDetector validator = new StructuringDetector(mockRepo);
        validator.validate(transaction, account);
        assertEquals(Transaction.Status.PENDING, transaction.getStatus()); 
    }      
    
    @Test
    public void shouldPass_whenSingleTransaction() {

        TransactionRepository mockRepo = mock(TransactionRepository.class);
        when(mockRepo.findBySourceAccountAndCreatedAtAfter(any(), any()))
                                        .thenReturn(List.of());
        
        
        Account account = Account.builder()
                .accountType(Account.AccountType.SAVING)
                .status(Account.Status.ACTIVE)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("15000"))
                .type(Transaction.Type.TRANSFER)
                .sourceAccount("ACC-123")         
                .status(Transaction.Status.PENDING) 
                .build();

        StructuringDetector validator = new StructuringDetector(mockRepo);
        validator.validate(transaction, account);
        assertEquals(Transaction.Status.PENDING, transaction.getStatus());
        
    }        
}



