package com.tommy.bankingengine.service;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.AccountRepository;

public class AccountServiceTest {
    
    @Test
    public void shouldSave_whenCorrectAccount() {
        AccountRepository mockRepo = mock(AccountRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        AccountService service = new AccountService(mockRepo, mockAuditLog);

        when(mockRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Account result = service.createAccount("Tommy", Account.AccountType.SAVING);
        
        assertEquals("Tommy", result.getOwnerName());
        assertEquals(Account.AccountType.SAVING, result.getAccountType());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals(Account.Status.ACTIVE, result.getStatus());
        assertNotNull(result.getAccountNumber());
    }

    @Test
    public void shouldThrow_whenAccountNotFound() {
        AccountRepository mockRepo = mock(AccountRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        AccountService service = new AccountService(mockRepo, mockAuditLog);

        when(mockRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.getAccount(1L);
        });
    }

   
    @Test
    public void shouldReturn_whenAccountFound() {
        AccountRepository mockRepo = mock(AccountRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        AccountService service = new AccountService(mockRepo, mockAuditLog);

        Account account = Account.builder()
                .id(1L)
                .ownerName("Tommy")
                .build();

        when(mockRepo.findById(1L)).thenReturn(Optional.of(account));

        Account result = service.getAccount(1L);
        assertEquals("Tommy", result.getOwnerName());
    }
    
    @Test
    public void shouldThrow_whenAccountNumberNotFound() {
        AccountRepository mockRepo = mock(AccountRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        AccountService service = new AccountService(mockRepo, mockAuditLog);

        when(mockRepo.findByAccountNumber("ACC-999"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.findAccountNumber("ACC-999");
        });
    }
}

