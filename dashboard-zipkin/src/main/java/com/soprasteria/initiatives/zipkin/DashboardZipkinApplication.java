package com.soprasteria.initiatives.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

/**
 * Class starting Zipkin dashboard application
 *
 * @author jntakpe
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZipkinStreamServer
public class DashboardZipkinApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboardZipkinApplication.class, args);
    }

}
