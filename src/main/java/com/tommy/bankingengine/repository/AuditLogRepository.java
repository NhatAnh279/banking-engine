package com.tommy.bankingengine.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tommy.bankingengine.model.AuditLog;


    
    public interface AuditLogRepository extends JpaRepository <AuditLog, Long> {

        List<AuditLog> findByAccountNumber(String accountNumber);
    }
