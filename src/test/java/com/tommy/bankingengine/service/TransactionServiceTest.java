package com.tommy.bankingengine.service;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.model.Transaction;
import com.tommy.bankingengine.repository.AccountRepository;
import com.tommy.bankingengine.repository.TransactionRepository;
import java.util.List;

public class TransactionServiceTest {
    
    @Test
    public void shouldIncreaseBalance_whenDeposit() {
        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransRepo = mock(TransactionRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        TransactionService service = new TransactionService(mockTransRepo, mockAccountRepo, mockAuditLog);
        

        when(mockTransRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mockAccountRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mockTransRepo.findBySourceAccountAndCreatedAtAfter(any(), any()))
        .thenReturn(List.of());
        when(mockTransRepo.countBySourceAccountAndTypeAndCreatedAtAfter(any(), any(), any()))
        .thenReturn(0L);

        Account account = Account.builder() 
                .accountNumber("ACC-123")
                .balance(new BigDecimal("5000"))
                .status(Account.Status.ACTIVE)
                .accountType(Account.AccountType.SAVING)
                .build();

        when(mockAccountRepo.findByAccountNumber("ACC-123"))
        .thenReturn(Optional.of(account));

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("2000"))
                    .type(Transaction.Type.DEPOSIT)
                    .sourceAccount("ACC-123")
                    .status(Transaction.Status.PENDING)
                    .build();

        Transaction result = service.processTransaction(transaction);

        assertEquals(Transaction.Status.COMPLETED, result.getStatus());
        assertEquals(new BigDecimal("7000"), account.getBalance());

    }

    @Test
    public void shouldDeductBalance_whenWithdraw() {
        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransRepo = mock(TransactionRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        TransactionService service = new TransactionService(mockTransRepo, mockAccountRepo, mockAuditLog);
        

        when(mockTransRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mockAccountRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mockTransRepo.findBySourceAccountAndCreatedAtAfter(any(), any()))
        .thenReturn(List.of());
        when(mockTransRepo.countBySourceAccountAndTypeAndCreatedAtAfter(any(), any(), any()))
        .thenReturn(0L);

        Account account = Account.builder() 
                .accountNumber("ACC-123")
                .balance(new BigDecimal("5000"))
                .status(Account.Status.ACTIVE)
                .accountType(Account.AccountType.SAVING)
                .build();

        when(mockAccountRepo.findByAccountNumber("ACC-123"))
        .thenReturn(Optional.of(account));

        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("2000"))
                    .type(Transaction.Type.WITHDRAWAL)
                    .sourceAccount("ACC-123")
                    .status(Transaction.Status.PENDING)
                    .build();

        Transaction result = service.processTransaction(transaction);

        assertEquals(Transaction.Status.COMPLETED, result.getStatus());
        assertEquals(new BigDecimal("3000"), account.getBalance());

    }

    @Test
    public void shouldMoveBalance_whenTransfer() {
        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        TransactionRepository mockTransRepo = mock(TransactionRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        TransactionService service = new TransactionService(mockTransRepo, mockAccountRepo, mockAuditLog);
        

        when(mockTransRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mockAccountRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(mockTransRepo.findBySourceAccountAndCreatedAtAfter(any(), any()))
        .thenReturn(List.of());
        when(mockTransRepo.countBySourceAccountAndTypeAndCreatedAtAfter(any(), any(), any()))
        .thenReturn(0L);

        Account account1 = Account.builder() 
                .accountNumber("ACC-123")
                .balance(new BigDecimal("5000"))
                .status(Account.Status.ACTIVE)
                .accountType(Account.AccountType.SAVING)
                .build();

        Account account2 = Account.builder()
                .accountNumber("Acc-456")
                .balance(new BigDecimal("2000"))
                .status(Account.Status.ACTIVE)
                .accountType(Account.AccountType.SAVING)
                .build();

        when(mockAccountRepo.findByAccountNumber("ACC-123"))
        .thenReturn(Optional.of(account1));
        
        when(mockAccountRepo.findByAccountNumber("ACC-456"))
        .thenReturn(Optional.of(account2));
        
        Transaction transaction = Transaction.builder()
                    .amount(new BigDecimal("2000"))
                    .type(Transaction.Type.TRANSFER)
                    .sourceAccount("ACC-123")
                    .destinationAccount("ACC-456")
                    .status(Transaction.Status.PENDING)
                    .build();

        Transaction result = service.processTransaction(transaction);

        assertEquals(Transaction.Status.COMPLETED, result.getStatus());
        assertEquals(new BigDecimal("3000"), account1.getBalance());
        assertEquals(new BigDecimal("4000"), account2.getBalance());

    }
}
