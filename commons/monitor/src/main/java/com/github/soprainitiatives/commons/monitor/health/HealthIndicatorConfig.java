package com.github.soprainitiatives.commons.monitor.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Custom configuration for health indicators
 *
 * @author jntakpe
 */
@Configuration
public class HealthIndicatorConfig {

    private final HealthProperties healthProperties;

    private final HealthAggregator healthAggregator;

    private final DiscoveryClient discoveryClient;

    @Autowired
    public HealthIndicatorConfig(HealthProperties healthProperties, HealthAggregator healthAggregator, DiscoveryClient discoveryClient) {
        this.healthProperties = healthProperties;
        this.healthAggregator = healthAggregator;
        this.discoveryClient = discoveryClient;
    }

    @Bean
    public HealthIndicator servicesHealthIndicator() {
        CompositeHealthIndicator composite = new CompositeHealthIndicator(healthAggregator);
        healthProperties.getServiceNames()
                .forEach(s -> composite.addHealthIndicator(s, new ServiceHealthIndicator(discoveryClient, s)));
        return composite;
    }
}
