package com.soprasteria.initiatives.utils;

import java.util.Objects;

/**
 * Utilities for URLs
 *
 * @author jntakpe
 */
public final class UrlUtils {

    private static final String START_INDEX = "://";

    public static String getServerAdressFromRequest(String request) {
        Objects.requireNonNull(request);
        return request.substring(0, request.indexOf("/", request.indexOf(START_INDEX) + START_INDEX.length()));
    }

}
