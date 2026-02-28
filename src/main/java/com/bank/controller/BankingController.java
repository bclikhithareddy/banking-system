package com.bank.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.bank.service.BankingService;
import com.bank.entity.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
public class BankingController {

    private final BankingService bankingService = new BankingService();

    @PostMapping("/register")
    public Customer register(@RequestBody Customer customer) {
        return bankingService.register(customer);
    }

    @PostMapping("/deposit/{id}/{amount}")
    public Transaction deposit(@PathVariable Long id, @PathVariable double amount) {
        return bankingService.deposit(id, amount);
    }

    @PostMapping("/withdraw/{id}/{amount}")
    public Transaction withdraw(@PathVariable Long id, @PathVariable double amount) {
        return bankingService.withdraw(id, amount);
    }

    @GetMapping("/transactions/{id}")
    public List<Transaction> getTransactions(@PathVariable Long id) {
        return bankingService.getTransactions(id);
    }
}