package com.example.final_project.config;

import org.junit.After;
import org.junit.Before;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainerConfig {


    @Container
    private final static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.4");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getConnectionString() + "/expenses");
    }

    @Before
    public void setUp() {
        mongoDBContainer.start();
    }


    @After
    public void tearDown() {
        mongoDBContainer.stop();
    }




}
