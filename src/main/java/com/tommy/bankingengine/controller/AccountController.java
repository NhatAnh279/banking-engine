package com.tommy.bankingengine.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.tommy.bankingengine.model.Account;
import com.tommy.bankingengine.service.AccountService;
import com.tommy.bankingengine.service.InterestService;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService accountService;
    private final InterestService interestService;

    public AccountController(AccountService accountService, InterestService interestService) {
        this.accountService = accountService;
        this.interestService = interestService;
    }

    @PostMapping
    public Account createAccount(@RequestParam String ownerName, @RequestParam Account.AccountType accountType) {
        return accountService.createAccount(ownerName, accountType);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @GetMapping("/number/{number}")
    public Account findAccountNumber(@PathVariable String number) {
        return accountService.findAccountNumber(number);
    }

    @PostMapping("/interest")
    public Account calculateInterestRate(@RequestParam String accountNumber) {
        return interestService.calculateInterestRate(accountNumber);
    }
}
