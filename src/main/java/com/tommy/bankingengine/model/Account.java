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
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private BigDecimal interestRate;
    

    public enum AccountType {
        SAVING, CHECKING
    }

    public enum Status {
        ACTIVE, FROZEN
    }

    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    
    @Enumerated(EnumType.STRING)
    private Status status;


    
    
}
