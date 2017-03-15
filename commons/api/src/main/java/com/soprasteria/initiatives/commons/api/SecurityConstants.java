package com.soprasteria.initiatives.commons.api;

/**
 * Constants related to security
 *
 * @author jntakpe
 */
public final class SecurityConstants {

    public static final String SECURED_API_PATH = "/api/**";

    public static final String[] SWAGGER_PATHS = {"/v2/api-docs/**", "/swagger-resources/configuration/ui", "/swagger-ui/index.html"};

    public static final String AUTHORIZATION_HEADER = "Authorization";

    static final String RACINES_TOKEN_KEY = "racines";

    static final String BEARER_PREFIX = "Bearer ";

    static final String TOKEN_KEY = "token";
}
