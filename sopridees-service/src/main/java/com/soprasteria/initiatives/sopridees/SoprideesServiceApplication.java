package com.soprasteria.initiatives.sopridees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * Classe principale du service de gestion des SoprIdées
 *
 * @author rjansem
 */
@EnableHystrix
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.soprasteria.initiatives")
public class SoprideesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoprideesServiceApplication.class, args);
    }

}
