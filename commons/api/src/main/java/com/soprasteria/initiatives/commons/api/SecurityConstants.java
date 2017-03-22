package com.soprasteria.initiatives.commons.api;

import org.springframework.http.HttpHeaders;

/**
 * Constants related to security
 *
 * @author jntakpe
 */
public final class SecurityConstants {

    public static final String SECURED_API_PATH = "/api/**";

    public static final String[] SWAGGER_PATHS = {"/v2/api-docs/**", "/swagger-resources/**", "/swagger-ui.html"};

    public static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;

    public static final String RACINES_TOKEN_KEY = "racines";

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String TOKEN_KEY = "token";
}
