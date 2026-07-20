package com.tommy.bankingengine.service;
import com.tommy.bankingengine.repository.AuditLogRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.tommy.bankingengine.model.AuditLog;

@Service
public class AuditLogService {
    
    private final AuditLogRepository auditLogRepository; 

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog log(AuditLog.Action action, String accountNumber, BigDecimal amount, 
                            String description, String transactionID) {
            
        AuditLog auditLog = AuditLog.builder()
                            .accountNumber(accountNumber)
                            .amount(amount)
                            .description(description)
                            .transactionID(transactionID)
                            .action(action)
                            .performedAt(LocalDateTime.now())
                            .build();

        return auditLogRepository.save(auditLog);
    }
}
