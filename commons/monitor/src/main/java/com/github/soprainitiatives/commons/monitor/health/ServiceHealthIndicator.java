package com.github.soprainitiatives.commons.monitor.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Health indicator checking request call is UP (2XX) DOWN (3XX, 4XX, 5XX)
 *
 * @author jntakpe
 */
public class ServiceHealthIndicator extends AbstractHealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHealthIndicator.class);

    private static final String INFO_SUFFIX = "/info";

    private final DiscoveryClient discoveryClient;

    private final String serviceId;

    ServiceHealthIndicator(DiscoveryClient discoveryClient, String serviceId) {
        this.discoveryClient = discoveryClient;
        this.serviceId = serviceId;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (instances.isEmpty()) {
            buildDownMsg(builder);
            return;
        }
        instances.stream()
                .map(this::optInstanceDown)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .map(i -> buildUnknownMsg(builder, i))
                .orElseGet(() -> buildUpMsg(builder, instances));
    }

    private void buildDownMsg(Health.Builder builder) {
        builder.down().withDetail("Service missing", String.format("No instance available for service '%s'", serviceId));
    }

    private Health.Builder buildUpMsg(Health.Builder builder, List<ServiceInstance> instances) {
        return builder.up().withDetail("Instances up", instances.stream()
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .collect(Collectors.toList()));
    }

    private Health.Builder buildUnknownMsg(Health.Builder builder, ServiceInstance i) {
        return builder.unknown().withDetail("Missing instances", String.format("Instance '%s' is down", i.getUri()));
    }

    private Optional<ServiceInstance> optInstanceDown(ServiceInstance serviceInstance) {
        Objects.requireNonNull(serviceInstance);
        String servceInfoUrl = serviceInstance.getUri().toString() + INFO_SUFFIX;
        try {
            ResponseEntity<String> response = new RestTemplate().getForEntity(servceInfoUrl, String.class);
            return response.getStatusCode().is2xxSuccessful() ? Optional.empty() : Optional.of(serviceInstance);
        } catch (RestClientException e) {
            LOGGER.warn(String.format("Cannot access %s", servceInfoUrl), e);
            return Optional.of(serviceInstance);
        }
    }

}
