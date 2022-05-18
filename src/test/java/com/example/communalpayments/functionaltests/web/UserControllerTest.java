package com.example.communalpayments.functionaltests.web;

import com.example.communalpayments.dao.UserRepository;
import com.example.communalpayments.entities.User;
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
public class UserControllerTest extends BaseFunctionalTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.truncateForTest();
    }

    @AfterEach
    void tearDown() {
        userRepository.truncateForTest();
    }

    @Test
    void registrationTest() throws IOException {

        try (InputStream userDtoIs = this.getClass().getResourceAsStream("user_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(userDtoIs)
                    .post("/api/users/registration")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.CREATED_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body("id", Matchers.greaterThan(0));
        }

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        User user = users.stream().findAny().orElseThrow();
        assertEquals("Ivanov", user.getLastName());
        assertEquals("Ivan", user.getFirstName());
        assertEquals("Ivanovych", user.getPatronymic());
        assertEquals("ivanov@gmail.com", user.getEmail());
        assertEquals("0961236545", user.getPhoneNumber());
    }

    @Test
    void registrationThrowsExTest() throws IOException {
        userRepository.save(User.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivanov@gmail.com")
                .phoneNumber("0961236545")
                .build());

        try (InputStream userDtoIs = this.getClass().getResourceAsStream("user_dto.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(userDtoIs)
                    .post("/api/users/registration")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body(Matchers.equalTo("Пользователь с заданным email уже существует"));
        }

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        User user = users.stream().findAny().orElseThrow();
        assertEquals("Ivanov", user.getLastName());
        assertEquals("Ivan", user.getFirstName());
        assertEquals("Ivanovych", user.getPatronymic());
        assertEquals("ivanov@gmail.com", user.getEmail());
        assertEquals("0961236545", user.getPhoneNumber());
    }

    @Test
    void getUserTest() {
        userRepository.save(User.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivanov@gmail.com")
                .phoneNumber("0961236545")
                .build());

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/users/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.OK_STATUS_CODE_AND_CONTENT_TYPE)
                .assertThat()
                .body("id", Matchers.equalTo(1))
                .body("lastName", Matchers.equalTo("Ivanov"))
                .body("firstName", Matchers.equalTo("Ivan"))
                .body("patronymic", Matchers.equalTo("Ivanovych"))
                .body("email", Matchers.equalTo("ivanov@gmail.com"))
                .body("phoneNumber", Matchers.equalTo("0961236545"));
    }

    @Test
    void getUserThrowsExTest() {

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/api/users/1")
                .then()
                .log().body()
                .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                .assertThat()
                .body(Matchers.equalTo("Пользователь с заданным id не существует"));
    }

    @Test
    void updateUserTest() throws IOException {
        userRepository.save(User.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivanov@gmail.com")
                .phoneNumber("0961236545")
                .build());

        try (InputStream updatedUserIs = this.getClass().getResourceAsStream("updated_user.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(updatedUserIs)
                    .put("/api/users")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.OK_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body("id", Matchers.equalTo(1))
                    .body("lastName", Matchers.equalTo("Ivanov"))
                    .body("firstName", Matchers.equalTo("Ivan"))
                    .body("patronymic", Matchers.equalTo("Ivanovych"))
                    .body("email", Matchers.equalTo("ivanov@ukr.net"))
                    .body("phoneNumber", Matchers.equalTo("0960020001"));
        }

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
    }

    @Test
    void updateUserThrowsUserNotFoundExTest() throws IOException {

        try (InputStream updatedUserIs = this.getClass().getResourceAsStream("updated_user.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(updatedUserIs)
                    .put("/api/users")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body(Matchers.equalTo("Пользователь с заданным id не существует"));
        }
        List<User> users = userRepository.findAll();
        assertEquals(0, users.size());
    }

    @Test
    void updateUserThrowsUserEmailExistsExTest() throws IOException {
        userRepository.save(User.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .patronymic("Ivanovych")
                .email("ivanov@gmail.com")
                .phoneNumber("0961236545")
                .build());

        userRepository.save(User.builder()
                .lastName("Ivanov")
                .firstName("Oleg")
                .patronymic("Olegovych")
                .email("ivanov@ukr.net")
                .phoneNumber("0963202535")
                .build());

        try (InputStream updatedUserIs = this.getClass().getResourceAsStream("updated_user.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(updatedUserIs)
                    .put("/api/users")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body(Matchers.equalTo("Пользователь с заданным email уже существует"));

            List<User> users = userRepository.findAll();
            assertEquals(2, users.size());
            User user1 = users.get(0);
            assertEquals("Ivanov", user1.getLastName());
            assertEquals("Ivan", user1.getFirstName());
            assertEquals("Ivanovych", user1.getPatronymic());
            assertEquals("ivanov@gmail.com", user1.getEmail());
            assertEquals("0961236545", user1.getPhoneNumber());
            User user2 = users.get(1);
            assertEquals("Ivanov", user2.getLastName());
            assertEquals("Oleg", user2.getFirstName());
            assertEquals("Olegovych", user2.getPatronymic());
            assertEquals("ivanov@ukr.net", user2.getEmail());
            assertEquals("0963202535", user2.getPhoneNumber());
        }
    }
}
