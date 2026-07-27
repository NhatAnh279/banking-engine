package com.tommy.bankingengine.service;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.AccountRepository;


public class InterestServiceTest {
    
    @Test
    public void ShouldAddInterest_whenSaving() {
        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        InterestService service = new InterestService(mockAccountRepo, mockAuditLog);

        when(mockAccountRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Account account = Account.builder() 
                .accountNumber("ABC-123")
                .accountType(Account.AccountType.SAVING)
                .balance(new BigDecimal("1000"))
                .interestRate(new BigDecimal("0.045"))
                .build();

        when(mockAccountRepo.findByAccountNumber("ABC-123"))
        .thenReturn(Optional.of(account));

        Account result = service.calculateInterestRate("ABC-123");

        assertEquals(new BigDecimal("1003.75"), result.getBalance());
                
    }

    @Test
    public void ShouldNotAddInterest_whenChecking() {
        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        InterestService service = new InterestService(mockAccountRepo, mockAuditLog);

        when(mockAccountRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Account account = Account.builder() 
                .accountNumber("ABC-456")
                .accountType(Account.AccountType.CHECKING)
                .balance(new BigDecimal("1000"))
                .interestRate(new BigDecimal("0.045"))
                .build();

        when(mockAccountRepo.findByAccountNumber("ABC-456"))
        .thenReturn(Optional.of(account));

        Account result = service.calculateInterestRate("ABC-456");

        assertEquals(new BigDecimal("1000"), result.getBalance());
                
    }

    @Test
    public void ShouldThrow_whenNoAccount() {
        AccountRepository mockAccountRepo = mock(AccountRepository.class);
        AuditLogService mockAuditLog = mock(AuditLogService.class);
        InterestService service = new InterestService(mockAccountRepo, mockAuditLog);

        when(mockAccountRepo.findByAccountNumber("ABC-999"))
                .thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> {
            service.calculateInterestRate("ABC-999");
        });
                
    }
}
