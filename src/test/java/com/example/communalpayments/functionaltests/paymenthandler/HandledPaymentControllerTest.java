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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class HandledPaymentControllerTest extends BaseFunctionalTest {

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
        wireMockServer.stubFor(post(urlEqualTo("/api/payment-handler"))
                .withHeader(HttpHeaders.CONTENT_TYPE, WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HttpHeaders.ACCEPT, containing(MediaType.APPLICATION_JSON_VALUE))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
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
    void savePaymentTest() throws IOException, InterruptedException {
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

        try (InputStream paymentHandlerResponseIS =
                     this.getClass().getResourceAsStream("payment_handler_response.json")) {

            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentHandlerResponseIS)
                    .post("/api/handled-payments/saving")
                    .then()
                    .log().body()
                    .assertThat()
                    .statusCode(200);

            TimeUnit.SECONDS.sleep(2);

            Payment payment = paymentRepository.findById(1L).orElseThrow();
            assertEquals(1, payment.getId());
            assertEquals(PaymentStatus.DONE, payment.getStatus());

            List<Payment> payments = paymentRepository.findAll();
            assertEquals(1, payments.size());
        }
    }

    @Test
    void savePaymentThrowsExTest() throws IOException {
        try (InputStream paymentHandlerResponseIS =
                     this.getClass().getResourceAsStream("payment_handler_response.json")) {

            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentHandlerResponseIS)
                    .post("/api/handled-payments/saving")
                    .then()
                    .log().body()
                    .assertThat()
                    .statusCode(404);

            List<Payment> payments = paymentRepository.findAll();
            assertEquals(0, payments.size());
        }
    }
}
