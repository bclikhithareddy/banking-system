package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.entity.Customer;
import com.bank.entity.Transaction;
import com.bank.repository.CustomerRepository;
import com.bank.repository.TransactionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
@CrossOrigin(origins = "http://localhost:3000")
public class BankingController {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    // REGISTER
    @PostMapping("/register")
    public String register(@RequestBody Customer customer) {
        customer.setBalance(0.0);
        customerRepo.save(customer);
        return "User Registered Successfully";
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer customer) {

        Customer existingCustomer =
                customerRepo.findByEmailAndPassword(
                        customer.getEmail(),
                        customer.getPassword()
                );

        if (existingCustomer != null) {
            return ResponseEntity.ok(existingCustomer);
        } else {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }

    // DEPOSIT
    @PutMapping("/deposit/{id}/{amount}")
    public ResponseEntity<String> deposit(
            @PathVariable Long id,
            @PathVariable Double amount) {

        Customer customer = customerRepo.findById(id).orElse(null);

        if (customer == null) {
            return ResponseEntity.status(404).body("Customer Not Found");
        }

        customer.setBalance(customer.getBalance() + amount);
        customerRepo.save(customer);

        Transaction tx = new Transaction();
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setCustomer(customer);
        transactionRepo.save(tx);

        return ResponseEntity.ok("Amount Deposited Successfully");
    }

    // WITHDRAW
    @PutMapping("/withdraw/{id}/{amount}")
    public ResponseEntity<String> withdraw(
            @PathVariable Long id,
            @PathVariable Double amount) {

        Customer customer = customerRepo.findById(id).orElse(null);

        if (customer == null) {
            return ResponseEntity.status(404).body("Customer Not Found");
        }

        if (customer.getBalance() < amount) {
            return ResponseEntity.badRequest().body("Insufficient Balance");
        }

        customer.setBalance(customer.getBalance() - amount);
        customerRepo.save(customer);

        Transaction tx = new Transaction();
        tx.setType("WITHDRAW");
        tx.setAmount(amount);
        tx.setCustomer(customer);
        transactionRepo.save(tx);

        return ResponseEntity.ok("Amount Withdrawn Successfully");
    }

    // GET CUSTOMER
    @GetMapping("/customer/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerRepo.findById(id).orElse(null);
    }

    // GET TRANSACTIONS (Latest First)
    @GetMapping("/transactions/{customerId}")
    public List<Transaction> getTransactions(@PathVariable Long customerId) {

        return transactionRepo
                .findByCustomer_IdOrderByTransactionDateDesc(customerId);
    }
}