package com.tommy.bankingengine.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.tommy.bankingengine.model.AuditLog;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.AccountRepository;

@Service
public class AccountService {
   
    private final AccountRepository accountRepository;
    private final AuditLogService auditLogService;

    public AccountService(AccountRepository accountRepository, AuditLogService auditLogService) {
        this.accountRepository = accountRepository;
        this.auditLogService = auditLogService;
    }

    public Account createAccount(String ownerName, Account.AccountType accountType) {
        Account account = Account.builder()
                .ownerName(ownerName)
                .accountType(accountType)
                .balance(BigDecimal.ZERO)
                .status(Account.Status.ACTIVE)
                .accountNumber("ACC-" + System.currentTimeMillis())
                .createdAt(LocalDateTime.now())
                .build();
            

        auditLogService.log(
            AuditLog.Action.ACCOUNT_CREATED,
            account.getAccountNumber(),
            null,
            "Account created for " + ownerName,
            null
        );
        return accountRepository.save(account);
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));
       
    }

    public Account findAccountNumber(String number) {
        return accountRepository.findByAccountNumber(number)
                .orElseThrow(()-> new RuntimeException("Account not found"));
    }
}
