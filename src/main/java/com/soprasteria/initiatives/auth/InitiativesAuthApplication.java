package com.soprasteria.initiatives.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class InitiativesAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(InitiativesAuthApplication.class, args);
    }

}
