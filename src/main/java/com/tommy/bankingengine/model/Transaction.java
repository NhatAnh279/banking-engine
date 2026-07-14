package com.tommy.bankingengine.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private String sourceAccount;
    private String destinationAccount;
    private String rejectionReason;
    private LocalDateTime createdAt;
    
    @Enumerated(EnumType.STRING)
    private Type type;
    
    @Enumerated(EnumType.STRING)
    private Status status;

    
    public enum Type {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }

    public enum Status {
        PENDING, COMPLETED, REJECTED
    }
}
