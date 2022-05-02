package com.example.communalpayments.services;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SchedulerService {

    private final RestTemplate restTemplate;
    private final PaymentRepository repository;


    public SchedulerService(RestTemplate restTemplate, PaymentRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    @Async
    public void handleNewPayments() {
        try {
            URI url = new URI("http://localhost:8081/api/payment-handler");
            List<Payment> payments = repository.getAllWhereStatusNew();

            for (Payment p : payments) {
                System.out.println(p.getId());
                HttpEntity<Long> httpEntity = new HttpEntity<>(p.getId());
                ResponseEntity<Payment> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Payment.class);
                Payment payment = response.getBody();
                if(payment != null) repository.save(payment);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
