package com.bank.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.bank.repository.*;
import com.bank.entity.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankingService {

    private final CustomerRepository customerRepo = null;
    private final TransactionRepository transactionRepo = null;

    public Customer register(Customer customer) {
        return customerRepo.save(customer);
    }

    public Transaction deposit(Long customerId, double amount) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();

        customer.setBalance(customer.getBalance() + amount);
        customerRepo.save(customer);

        Transaction tx = new Transaction();
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setCustomer(customer);

        return transactionRepo.save(tx);
    }

    public Transaction withdraw(Long customerId, double amount) {
        Customer customer = customerRepo.findById(customerId).orElseThrow();

        if (customer.getBalance() < amount) {
            throw new RuntimeException("Insufficient Balance");
        }

        customer.setBalance(customer.getBalance() - amount);
        customerRepo.save(customer);

        Transaction tx = new Transaction();
        tx.setType("WITHDRAW");
        tx.setAmount(amount);
        tx.setCustomer(customer);

        return transactionRepo.save(tx);
    }

    public List<Transaction> getTransactions(Long customerId) {
        return transactionRepo.findByCustomerId(customerId);
    }
}