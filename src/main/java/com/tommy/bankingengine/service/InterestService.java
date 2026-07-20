package com.tommy.bankingengine.service;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.tommy.bankingengine.model.AuditLog;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.repository.AccountRepository;

@Service
public class InterestService {
    
    private final AccountRepository accountRepository;
    private final AuditLogService auditLogService;

    public InterestService(AccountRepository accountRepository, AuditLogService auditLogService) {
        this.accountRepository = accountRepository;
        this.auditLogService = auditLogService;
    }

    public Account calculateInterestRate(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getAccountType() == Account.AccountType.SAVING) {
            BigDecimal monthlyInterest = account.getBalance()
                                        .multiply(account.getInterestRate())
                                        .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

        account.setBalance(account.getBalance().add(monthlyInterest));
        accountRepository.save(account);

        auditLogService.log(
        AuditLog.Action.INTEREST_APPLIED,
        account.getAccountNumber(),
        monthlyInterest,
        "Monthly interest $" + monthlyInterest + " applied",
        null
        );
        }


        return account;
    }

}
