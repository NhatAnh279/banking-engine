package com.tommy.bankingengine.controller;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.tommy.bankingengine.model.AuditLog;
import com.tommy.bankingengine.repository.AuditLogRepository;


@RestController
@RequestMapping("/api/audit")
public class AuditLogController {
    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    
    @GetMapping("/{accountNumber}")
    public List<AuditLog> getAuditLog(@PathVariable String accountNumber) {
        return auditLogRepository.findByAccountNumber(accountNumber);
    }
}
