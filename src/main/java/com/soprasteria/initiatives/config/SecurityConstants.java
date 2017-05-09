package com.soprasteria.initiatives.config;

import org.springframework.http.HttpHeaders;

/**
 * Constants related to security
 *
 * @author jntakpe
 */
public final class SecurityConstants {

    public static final String[] SWAGGER_PATHS = {"/v2/api-docs/**", "/swagger-resources/**", "/swagger-ui.html"};

    public static final String BEARER_PREFIX = "Bearer ";
}
