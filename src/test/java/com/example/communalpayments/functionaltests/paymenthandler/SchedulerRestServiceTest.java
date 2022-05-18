package com.example.communalpayments.functionaltests.paymenthandler;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.dao.TemplateRepository;
import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.*;
import com.example.communalpayments.functionaltests.BaseFunctionalTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.communalpayments.functionaltests.web.PaymentControllerTest.getObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class SchedulerRestServiceTest extends BaseFunctionalTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillingAddressRepository addressRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        userRepository.truncateForTest();
        addressRepository.truncateForTest();
        templateRepository.truncateForTest();
        paymentRepository.truncateForTest();
        wireMockServer.resetAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.truncateForTest();
        addressRepository.truncateForTest();
        templateRepository.truncateForTest();
        paymentRepository.truncateForTest();
        wireMockServer.resetAll();
    }

    @Test
    void handleNewPaymentsTest() throws JsonProcessingException, InterruptedException {
        ObjectMapper objectMapper = getObjectMapper();

        User user = userRepository.save(User.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivanov@gmail.com")
                .phoneNumber("0961236545")
                .build());
        BillingAddress address = addressRepository.save(BillingAddress.builder()
                .address("Днепр, Калиновая, 95")
                .user(user)
                .build());
        Template template = templateRepository.save(Template.builder()
                .name("газ")
                .iban("UA230256899632123000000253696")
                .purposeOfPayment("за газ лиц.счет 235689")
                .address(address)
                .build());
        paymentRepository.save(new Payment(template, "4441114450791395", 896.15));

        List<Payment> newPayments = paymentRepository.getPaymentsWhereStatusNewLimit50();

        wireMockServer.stubFor(post(urlEqualTo("/api/payment-handler"))
                .withHeader(HttpHeaders.CONTENT_TYPE, WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HttpHeaders.ACCEPT, containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(newPayments)))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        assertEquals(1, newPayments.size());
        Payment newPayment = newPayments.stream().findAny().orElseThrow();
        assertEquals(1, newPayment.getId());
        assertEquals("4441114450791395", newPayment.getCardNumber());
        assertEquals(896.15, newPayment.getAmount());
        assertEquals(PaymentStatus.NEW, newPayment.getStatus());

        TimeUnit.SECONDS.sleep(1);

        Payment payment = paymentRepository.findById(1L).orElseThrow();
        assertEquals(PaymentStatus.IN_PROCESS, payment.getStatus());
    }
}
