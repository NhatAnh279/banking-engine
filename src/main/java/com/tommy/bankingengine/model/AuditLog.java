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
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private BigDecimal amount;
    private String description;
    private LocalDateTime performedAt;
    private String transactionID;

    @Enumerated(EnumType.STRING)
    private Action action;


    public enum Action {
        DEPOSIT, WITHDRAWAL, TRANSFER, ACCOUNT_CREATED, ACCOUNT_FROZEN
    }
    
}
