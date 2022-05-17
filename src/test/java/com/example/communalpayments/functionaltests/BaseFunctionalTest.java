package com.example.communalpayments.functionaltests;

import com.example.communalpayments.CommunalPaymentsApplication;
import com.example.communalpayments.functionaltests.container.CustomPostgreSQLContainer;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = CommunalPaymentsApplication.class)
@ContextConfiguration(initializers = BaseFunctionalTest.ContextInitializer.class)
@AutoConfigureWireMock(port = 8082)
@ActiveProfiles(value = "test")
@Slf4j
public abstract class BaseFunctionalTest {

    private static final DockerImageName POSTGRES_IMAGE_NAME = DockerImageName.parse("postgres");
    public static final String POSTGRES_TAG = "latest";
    private static final PostgreSQLContainer<CustomPostgreSQLContainer> POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new CustomPostgreSQLContainer(POSTGRES_IMAGE_NAME.withTag(POSTGRES_TAG))
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("password");
        POSTGRES_CONTAINER.start();
    }

    @LocalServerPort
    protected int serverPort;

    @BeforeEach
    public void setUpRestAssured() {
        RestAssured.port = serverPort;
    }

    public static class ContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
                    "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
