package com.soprasteria.initiatives.auth.web;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Constants for assertions on responses
 *
 * @author jntakpe
 */
final class AssertionConstants {

    static final ResultMatcher STATUS_CREATED = status().isCreated();

    static final ResultMatcher STATUS_OK = status().isOk();

    static final ResultMatcher STATUS_BAD_REQUEST = status().isBadRequest();

    static final ResultMatcher STATUS_UNAUTHORIZED = status().isUnauthorized();

    static final ResultMatcher STATUS_NO_CONTENT = status().isNoContent();

    static final ResultMatcher STATUS_NOT_FOUND = status().isNotFound();

    static final ResultMatcher CONTENT_TYPE_JSON = content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON);

    static final ResultMatcher OBJECT_EXISTS = jsonPath("$").exists();
}
