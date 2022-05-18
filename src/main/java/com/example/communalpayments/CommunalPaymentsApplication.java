package com.example.communalpayments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CommunalPaymentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunalPaymentsApplication.class, args);
    }
}
