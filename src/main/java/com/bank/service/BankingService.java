package com.bank.service;

import org.springframework.stereotype.Service;
import com.bank.repository.CustomerRepository;
import com.bank.repository.TransactionRepository;
import com.bank.entity.Customer;
import com.bank.entity.Transaction;

import java.util.List;

@Service
public class BankingService {

    private final CustomerRepository customerRepo;
    private final TransactionRepository transactionRepo;

    // ✅ Constructor Injection (Recommended)
    public BankingService(CustomerRepository customerRepo,
                          TransactionRepository transactionRepo) {
        this.customerRepo = customerRepo;
        this.transactionRepo = transactionRepo;
    }

    // ✅ Register
    public Customer register(Customer customer) {
        customer.setBalance(0.0);
        return customerRepo.save(customer);
    }

    // ✅ Login
    public Customer login(String email, String password) {
        return customerRepo.findByEmailAndPassword(email, password);
    }

    // ✅ Deposit
    public Transaction deposit(Long customerId, Double amount) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));

        customer.setBalance(customer.getBalance() + amount);
        customerRepo.save(customer);

        Transaction tx = new Transaction();
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setCustomer(customer);

        return transactionRepo.save(tx);
    }

    // ✅ Withdraw
    public Transaction withdraw(Long customerId, Double amount) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));

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

    // ✅ Get Customer
    public Customer getCustomer(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));
    }

    // ✅ Get Transactions (Latest First)
    public List<Transaction> getTransactions(Long customerId) {
        return transactionRepo
                .findByCustomer_IdOrderByTransactionDateDesc(customerId);
    }
}