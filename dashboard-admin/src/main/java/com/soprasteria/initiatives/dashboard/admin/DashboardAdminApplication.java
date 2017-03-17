package com.soprasteria.initiatives.dashboard.admin;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Class starting Spring Boot admin UI server
 *
 * @author jntakpe
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class DashboardAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboardAdminApplication.class, args);
    }

}
