package com.example.communalpayments.functionaltests.container;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class CustomPostgreSQLContainer extends PostgreSQLContainer<CustomPostgreSQLContainer> {

    public CustomPostgreSQLContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
    }
}
