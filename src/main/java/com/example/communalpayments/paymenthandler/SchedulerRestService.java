package com.example.communalpayments.paymenthandler;

import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SchedulerRestService {

    private final String url;
    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;


    public SchedulerRestService(@Value("${rest.service.PAYMENT_HANDLER_API.url}") String url,
                                RestTemplateBuilder restTemplateBuilder, PaymentRepository repository) {
        this.url = url;
        this.restTemplate = restTemplateBuilder.build();
        this.paymentRepository = repository;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void handleNewPayments() {
        List<Payment> payments = paymentRepository.getPaymentsWhereStatusNewLimit50();

        if (!payments.isEmpty()) {
            log.info("Взял в обработку оплаты: " + payments);
            sendToPaymentHandlerApi(payments);
            log.info("Отправил оплаты " + payments + " в сервис Payment handler");
        }
    }

    private void sendToPaymentHandlerApi(List<Payment> payments) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Payment>> httpEntity = new HttpEntity<>(payments, httpHeaders);
        restTemplate.exchange(url, HttpMethod.POST, httpEntity, HttpStatus.class);
    }
}
