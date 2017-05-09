package com.soprasteria.initiatives;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class InitiativesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(InitiativesBackendApplication.class, args);
    }

}
