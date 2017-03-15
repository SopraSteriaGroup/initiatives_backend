package com.soprasteria.initiatives.auth.web;

/**
 * REST API contants
 *
 * @author jntakpe
 */
class ApiConstants {

    private static final String API = "/api";

    static final String USERS = API + "/users";

    static final String USERS_AUTHORITIES = API + "/users/{userId}/authorities";

    static final String AUTHORITIES = API + "/authorities";

    static final String TOKENS = API + "/tokens";

}
