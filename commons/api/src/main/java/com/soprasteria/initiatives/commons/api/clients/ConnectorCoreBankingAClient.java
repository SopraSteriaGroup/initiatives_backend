package com.soprasteria.initiatives.commons.api.clients;

import org.springframework.cloud.netflix.feign.FeignClient;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Wrapper for accessing account-viewing-service via Feign
 *
 * @author jntakpe
 * @see FeignClient
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@FeignClient(name = ServicesUrls.CONNECTOR_CORE_BANKING_A, url = ServicesUrls.CONNECTOR_CORE_BANKING_A_TEST_URL)
public @interface ConnectorCoreBankingAClient {

}
