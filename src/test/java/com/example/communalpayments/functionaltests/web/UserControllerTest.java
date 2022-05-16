package com.example.communalpayments.functionaltests.web;

import com.example.communalpayments.functionaltests.BaseFunctionalTest;
import com.example.communalpayments.functionaltests.util.RestAssuredUtil;
import com.example.communalpayments.services.interfaces.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;

import static io.restassured.RestAssured.given;

@Testcontainers
public class UserControllerTest extends BaseFunctionalTest {

    @Autowired
    private UserService userService;

    @Test
    void registrationTest() throws IOException {

        try(InputStream userDtoIs = this.getClass().getResourceAsStream("user_dto.json")) {
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
    }

//    public static String read(String resourcePath, Class<?> clazz) {
//        try (InputStream is = clazz.getResourceAsStream(resourcePath)) {
//            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
//        } catch (IOException e) {
//            throw new RuntimeException("Error occurred while reading file", e);
//        }
//    }
}
