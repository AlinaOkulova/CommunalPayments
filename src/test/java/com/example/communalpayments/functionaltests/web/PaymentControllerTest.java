package com.example.communalpayments.functionaltests.web;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.dao.TemplateRepository;
import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.*;
import com.example.communalpayments.functionaltests.BaseFunctionalTest;
import com.example.communalpayments.functionaltests.util.RestAssuredUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class PaymentControllerTest extends BaseFunctionalTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillingAddressRepository addressRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        userRepository.truncateForTest();
        addressRepository.truncateForTest();
        templateRepository.truncateForTest();
        paymentRepository.truncateForTest();
    }

    @AfterEach
    void teatDown() {
        userRepository.truncateForTest();
        addressRepository.truncateForTest();
        templateRepository.truncateForTest();
        paymentRepository.truncateForTest();
    }

    @Test
    void createPaymentTest() throws IOException {
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

        try(InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentDtoIS)
                    .post("/api/payments")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.CREATED_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body("id", Matchers.equalTo(1));

            List<Payment> payments = paymentRepository.findAll();
            Payment payment = payments.stream().findAny().orElseThrow();
            assertEquals(1, payments.size());
            assertEquals(1, payment.getId());
            assertEquals("4441114450791395", payment.getCardNumber());
            assertEquals(896.15, payment.getAmount());
            assertEquals(1, payment.getTemplate().getId());
            assertEquals(template.getId(), payment.getTemplate().getId());
        }
    }

    @Test
    void createPaymentTemplateNotFoundExTest() throws IOException {
        try(InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentDtoIS)
                    .post("/api/payments")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body(Matchers.equalTo("Шаблон с заданным id не существует"));

            List<Payment> payments = paymentRepository.findAll();
            assertEquals(0, payments.size());
        }
    }

    @Test
    void createPaymentDuplicateExTest() throws IOException {
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

        try(InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentDtoIS)
                    .post("/api/payments");
        }

        try(InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentDtoIS)
                    .post("/api/payments")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body(Matchers.equalTo("Такая оплата уже существует. Повторите действие через минуту"));

            List<Payment> payments = paymentRepository.findAll();
            assertEquals(1, payments.size());
        }
    }
}
