package com.example.communalpayments.functionaltests.web;

import com.example.communalpayments.dao.BillingAddressRepository;
import com.example.communalpayments.dao.TemplateRepository;
import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.BillingAddress;
import com.example.communalpayments.entities.Template;
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
public class TemplateControllerTest extends BaseFunctionalTest {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private BillingAddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.truncateForTest();
        addressRepository.truncateForTest();
        templateRepository.truncateForTest();
    }

    @AfterEach
    void teatDown() {
        userRepository.truncateForTest();
        addressRepository.truncateForTest();
        templateRepository.truncateForTest();
    }

    @Test
    void createTemplateTest() throws IOException {
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

        try (InputStream templateDtoIS = this.getClass().getResourceAsStream("template_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(templateDtoIS)
                    .post("/api/templates")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.CREATED_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body("id", Matchers.equalTo(1));

            List<Template> templates = templateRepository.findAll();
            Template template = templates.stream().findAny().orElseThrow();
            assertEquals(1, templates.size());
            assertEquals(1, template.getId());
            assertEquals("газ", template.getName());
            assertEquals("UA230256899632123000000253696", template.getIban());
            assertEquals("за газ лиц.счет 235689", template.getPurposeOfPayment());
            assertEquals(1, template.getAddress().getId());
            assertEquals(address.getId(), template.getAddress().getId());
        }
    }

    @Test
    void createTemplateThrowsExTest() throws IOException {
        try (InputStream templateDtoIS = this.getClass().getResourceAsStream("template_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(templateDtoIS)
                    .post("/api/templates")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body(Matchers.equalTo("Платежный адрес с заданным id не существует"));

            List<Template> templates = templateRepository.findAll();
            assertEquals(0, templates.size());
        }
    }

    @Test
    void getAllTemplatesByAddressIdTest() throws IOException {
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

        try (InputStream templateDtoIS = this.getClass().getResourceAsStream("template_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(templateDtoIS)
                    .post("/api/templates");
        }

        ExtractableResponse<Response> response =
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/templates/billing-address/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.OK_STATUS_CODE_AND_CONTENT_TYPE)
                .extract();

        String responseJson = response.asString();

        List<Template> templates = templateRepository.getTemplatesByAddressId(1);
        Template template = templates.stream().findAny().orElseThrow();
        assertEquals(1, templates.size());
        assertEquals(1, template.getAddress().getId());
        assertEquals(1, template.getId());
        assertEquals("газ", template.getName());
        assertEquals("UA230256899632123000000253696", template.getIban());
        assertEquals("за газ лиц.счет 235689", template.getPurposeOfPayment());
        assertEquals(objectMapper.writeValueAsString(templates), responseJson);
    }

    @Test
    void getAllTemplatesByAddressIdThrowsExTest() {
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/templates/billing-address/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                .body(Matchers.equalTo("Платежный адрес с заданным id не существует"));
    }

    @Test
     void getTemplateByIdTest() throws IOException {
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

        try (InputStream templateDtoIS = this.getClass().getResourceAsStream("template_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(templateDtoIS)
                    .post("/api/templates");
        }

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/templates/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.OK_STATUS_CODE_AND_CONTENT_TYPE)
                .body("id", Matchers.equalTo(1))
                .body("name", Matchers.equalTo("газ"))
                .body("iban", Matchers.equalTo("UA230256899632123000000253696"))
                .body("purposeOfPayment", Matchers.equalTo("за газ лиц.счет 235689"));
     }

    @Test
    void getTemplateByIdThrowsExTest() {
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/templates/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                .body(Matchers.equalTo("Шаблон с заданным id не существует"));
    }
}
