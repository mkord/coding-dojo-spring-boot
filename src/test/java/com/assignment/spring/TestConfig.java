package com.assignment.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Configuration
@Testcontainers
public class TestConfig {
    @Value("${spring.datasource.username}")
    private static String dbUserName;
    @Value("${spring.datasource.password}")
    private static String dbUserPassword;

    @SuppressWarnings("rawtypes")
    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withUsername(dbUserName)
            .withPassword(dbUserPassword);
}
