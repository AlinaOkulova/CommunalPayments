package com.example.communalpayments.functionaltests.web;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.User;
import com.example.communalpayments.functionaltests.BaseFunctionalTest;
import com.example.communalpayments.functionaltests.util.RestAssuredUtil;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.example.communalpayments.functionaltests.web.PaymentControllerTest.getObjectMapper;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class BillingAddressControllerTest extends BaseFunctionalTest {

    @Autowired
    private BillingAddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        addressRepository.truncateForTest();
        userRepository.truncateForTest();
    }

    @AfterEach
    void tearDown() {
        addressRepository.truncateForTest();
        userRepository.truncateForTest();
    }

    @Test
    void createBillingAddressTest() throws IOException {
        User user = userRepository.save(User.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivanov@gmail.com")
                .phoneNumber("0961236545")
                .build());

        try (InputStream addressDtoIS = this.getClass().getResourceAsStream("address_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(addressDtoIS)
                    .post("/api/billing-addresses")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.CREATED_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body("id", Matchers.equalTo(1));

            List<BillingAddress> billingAddresses = addressRepository.findAll();
            assertEquals(1, billingAddresses.size());
            BillingAddress address = billingAddresses.stream().findAny().orElseThrow();
            assertEquals("Днепр, Калиновая, 95", address.getAddress());
            assertEquals(user, address.getUser());
        }
    }

    @Test
    void createBillingAddressThrowsExTest() throws IOException {

        try (InputStream addressDtoIS = this.getClass().getResourceAsStream("address_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(addressDtoIS)
                    .post("/api/billing-addresses")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.NOT_FOUND_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body(Matchers.equalTo("Пользователь с заданным id не существует"));

            List<BillingAddress> billingAddresses = addressRepository.findAll();
            assertEquals(0, billingAddresses.size());
        }
    }

    @Test
    void getAllAddressesByUserIdTest() throws IOException {
        ObjectMapper objectMapper = getObjectMapper();

        User user = userRepository.save(User.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivanov@gmail.com")
                .phoneNumber("0961236545")
                .build());

        try (InputStream addressDtoIS = this.getClass().getResourceAsStream("address_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(addressDtoIS)
                    .post("/api/billing-addresses");
        }

        ExtractableResponse<Response> response =
                given()
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .get("/api/billing-addresses/user/1")
                        .then()
                        .log().body()
                        .spec(RestAssuredUtil.OK_STATUS_CODE_AND_CONTENT_TYPE)
                        .extract();

        String jsonResponse = response.asString();
        List<BillingAddress> addresses = addressRepository.getBillingAddressesByUserId(1);
        BillingAddress address = addresses.stream().findAny().orElseThrow();
        assertEquals(1, addresses.size());
        assertEquals(1, address.getUser().getId());
        assertEquals(1L, address.getId());
        assertEquals("Днепр, Калиновая, 95", address.getAddress());

        assertEquals(objectMapper.writeValueAsString(addresses), jsonResponse);
    }

    @Test
    void getAllAddressesByUserIdThrowsExTest() {
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/billing-addresses/user/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.NOT_FOUND_STATUS_CODE_AND_CONTENT_TYPE)
                .assertThat()
                .body(Matchers.equalTo("Пользователь с заданным id не существует"));
    }

    @Test
    void getBillingAddressByIdTest() throws IOException {
        userRepository.save(User.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivanov@gmail.com")
                .phoneNumber("0961236545")
                .build());

        try (InputStream addressDtoIS = this.getClass().getResourceAsStream("address_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(addressDtoIS)
                    .post("/api/billing-addresses");
        }

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/billing-addresses/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.OK_STATUS_CODE_AND_CONTENT_TYPE)
                .assertThat()
                .body("id", Matchers.equalTo(1))
                .body("address", Matchers.equalTo("Днепр, Калиновая, 95"));
    }

    @Test
    void getBillingAddressByIdThrowsExTest() {
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/billing-addresses/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.NOT_FOUND_STATUS_CODE_AND_CONTENT_TYPE)
                .assertThat()
                .body(Matchers.equalTo("Платежный адрес с заданным id не существует"));
    }
}
