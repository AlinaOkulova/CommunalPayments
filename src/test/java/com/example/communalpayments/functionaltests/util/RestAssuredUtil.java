package com.example.communalpayments.functionaltests.util;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;

import static org.apache.http.HttpStatus.*;

@UtilityClass
public class RestAssuredUtil {

    public static final ResponseSpecification CHECK_STATUS_CODE_AND_CONTENT_TYPE =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_OK)
                    .expectContentType(MediaType.APPLICATION_JSON_VALUE)
                    .build();

    public static final ResponseSpecification BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_BAD_REQUEST)
                    .expectContentType(MediaType.APPLICATION_JSON_VALUE)
                    .build();

    public static final ResponseSpecification CREATED_STATUS_CODE_AND_CONTENT_TYPE =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_CREATED)
                    .expectContentType(MediaType.APPLICATION_JSON_VALUE)
                    .build();

    public static final ResponseSpecification NOT_FOUND_STATUS_CODE_AND_CONTENT_TYPE =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_NOT_FOUND)
                    .expectContentType(MediaType.APPLICATION_JSON_VALUE)
                    .build();

    public static final ResponseSpecification INTERNAL_ERROR_STATUS_CODE_AND_CONTENT_TYPE =
            new ResponseSpecBuilder()
                    .expectStatusCode(SC_INTERNAL_SERVER_ERROR)
                    .expectContentType(MediaType.APPLICATION_JSON_VALUE)
                    .build();

}
