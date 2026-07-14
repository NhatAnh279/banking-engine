package com.tommy.bankingengine.repository;
import com.tommy.bankingengine.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, Long>  {
    
}
