package com.tommy.bankingengine.repository;
import com.tommy.bankingengine.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository <Transaction, Long>  {
    List<Transaction> findBySourceAccount(String sourceAccount);
}
