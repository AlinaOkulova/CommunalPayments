package com.example.communalpayments.services;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
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
    private final PaymentRepository paymentRepository;


    public SchedulerService(RestTemplateBuilder restTemplateBuilder, PaymentRepository repository) {
        this.restTemplate = restTemplateBuilder.build();
        this.paymentRepository = repository;
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void handleNewPayments() {
            try {
                URI url = new URI("http://localhost:8081/api/payment-handler");
                List<Payment> payments = paymentRepository.getPaymentsWhereStatusNewLimit50();
                System.out.println(payments);

                if(!payments.isEmpty()) {
                    List<Long> ids = payments.stream().map(Payment::getId).toList();
                    paymentRepository.updateStatusToInProcess(ids);

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<List<Payment>> httpEntity = new HttpEntity<>(payments, httpHeaders);
                    restTemplate.exchange(url, HttpMethod.POST, httpEntity, HttpStatus.class);
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
    }
}
