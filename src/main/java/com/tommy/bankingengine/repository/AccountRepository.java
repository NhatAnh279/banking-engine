package com.tommy.bankingengine.repository;
import com.tommy.bankingengine.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
}