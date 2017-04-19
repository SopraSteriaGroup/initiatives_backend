package com.soprasteria.initiatives.commons.eurekaclient;

import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.converters.wrappers.CodecWrappers;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClient;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

@Configuration
public class EurekaSslConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaSslConfig.class);

    @Value("${eureka.client.trust-store: classpath:truststore.jks}")
    private Resource trustStore;

    @Value("${eureka.client.trust-store-password:changeit}")
    private String trustStorePassword;

    //cant autowire if running TU's.
    @Autowired(required = false)
    private EurekaClientConfig eurekaClientConfig;

    @Bean
    public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs() throws IOException {
        eurekaClientConfig = eurekaClientConfig == null ? new DefaultEurekaClientConfig() : eurekaClientConfig;
        DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();
        EurekaJerseyClientImpl.EurekaJerseyClientBuilder clientBuilder = new EurekaJerseyClientImpl.EurekaJerseyClientBuilder()
                .withClientName("DiscoveryClient-HTTPClient-Custom")
                .withUserAgent("Java-EurekaClient")
                .withConnectionTimeout(eurekaClientConfig.getEurekaServerConnectTimeoutSeconds() * 1000)
                .withReadTimeout(eurekaClientConfig.getEurekaServerReadTimeoutSeconds() * 1000)
                .withMaxConnectionsPerHost(eurekaClientConfig.getEurekaServerTotalConnectionsPerHost())
                .withMaxTotalConnections(eurekaClientConfig.getEurekaServerTotalConnections())
                .withConnectionIdleTimeout(eurekaClientConfig.getEurekaConnectionIdleTimeoutSeconds() * 1000)
                .withEncoderWrapper(CodecWrappers.getEncoder(eurekaClientConfig.getEncoderName()))
                .withDecoderWrapper(CodecWrappers.resolveDecoder(eurekaClientConfig.getDecoderName(), eurekaClientConfig.getClientDataAccept()));

        if (ResourceUtils.isFileURL(trustStore.getURL())) {
            clientBuilder.withTrustStoreFile(trustStore.getFile().getPath(), trustStorePassword);
        } else {
            LOGGER.warn("impossible de lire le trustore : {}", trustStore.getURL().getPath());
        }
        EurekaJerseyClient jerseyClient = clientBuilder.build();
        args.setEurekaJerseyClient(jerseyClient);
        return args;
    }
}