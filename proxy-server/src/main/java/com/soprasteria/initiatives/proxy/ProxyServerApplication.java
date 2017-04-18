package com.soprasteria.initiatives.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Class starting Zuul proxy that provides dynamic routings
 *
 * @author jntakpe
 */
@EnableZuulProxy
@SpringBootApplication(scanBasePackages = {
        "com.soprasteria.initiatives.proxy",
        "com.soprasteria.initiatives.commons.eurekaclient"})

public class ProxyServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyServerApplication.class, args);
    }
}
