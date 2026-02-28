package com.bank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BankingSystemApplication {

    @Value("${spring.datasource.url:NOT_FOUND}")
    private String url;

    @PostConstruct
    public void print() {
        System.out.println("DATABASE URL = " + url);
    }

    public static void main(String[] args) {
        SpringApplication.run(BankingSystemApplication.class, args);
    }
}