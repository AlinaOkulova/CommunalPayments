package com.example.communalpayments.functionaltests.web;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.dao.PaymentRepository;
import com.example.communalpayments.dao.TemplateRepository;
import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.Payment;
import com.example.communalpayments.entities.Template;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.functionaltests.BaseFunctionalTest;
import com.example.communalpayments.functionaltests.util.RestAssuredUtil;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonGenerator;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonParser;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
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

        try (InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
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
        try (InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
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

        try (InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentDtoIS)
                    .post("/api/payments");
        }

        try (InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
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

    @Test
    void getAllPaymentsByUserIdTest() throws IOException, InterruptedException {
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

        try (InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentDtoIS)
                    .post("/api/payments");
        }

        TimeUnit.SECONDS.sleep(1);

        ExtractableResponse<Response> response =
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .get("/api/payments/user/1")
                        .then()
                        .log().body()
                        .spec(RestAssuredUtil.OK_STATUS_CODE_AND_CONTENT_TYPE)
                        .extract();

        String responseJson = response.asString();

        List<Payment> payments = paymentRepository.getAllByUserId(1);
        Payment payment = payments.stream().findAny().orElseThrow();
        assertEquals(1, payments.size());
        assertEquals(1, payment.getTemplate().getAddress().getUser().getId());
        assertEquals(1, payment.getId());
        assertEquals("4441114450791395", payment.getCardNumber());
        assertEquals(896.15, payment.getAmount());
        assertEquals(objectMapper.writeValueAsString(payments), responseJson);
    }

    @Test
    void getAllPaymentsByUserIdThrowsExTest() {
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/payments/user/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                .assertThat()
                .body(Matchers.equalTo("Пользователь с заданным id не существует"));
    }

    @Test
    void getPaymentTest() throws IOException {
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

        try (InputStream paymentDtoIS = this.getClass().getResourceAsStream("payment_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(paymentDtoIS)
                    .post("/api/payments");
        }

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/payments/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.OK_STATUS_CODE_AND_CONTENT_TYPE)
                .assertThat()
                .body("id", Matchers.equalTo(1))
                .body("cardNumber", Matchers.equalTo("4441114450791395"))
                .body("amount", Matchers.equalTo(896.15F));
    }

    @Test
    void getPaymentThrowsExTest() {
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/payments/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                .assertThat()
                .body(Matchers.equalTo("Платежа с заданным id не существует"));
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        });
        simpleModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                return LocalDateTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        });
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }
}
