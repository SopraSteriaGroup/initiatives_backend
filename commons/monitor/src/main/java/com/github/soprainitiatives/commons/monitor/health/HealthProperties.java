package com.github.soprainitiatives.commons.monitor.health;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Properties associated to health checks
 *
 * @author jntakpe
 */
@Component
@ConfigurationProperties("health")
public class HealthProperties {

    private List<String> serviceNames = new ArrayList<>();

    public List<String> getServiceNames() {
        return serviceNames;
    }

    public HealthProperties setServiceNames(List<String> serviceNames) {
        this.serviceNames = serviceNames;
        return this;
    }

}
