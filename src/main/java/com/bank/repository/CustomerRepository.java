package com.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Customer findByEmailAndPassword(String email, String password);
}