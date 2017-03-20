package com.soprasteria.initiatives.auth.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author cegiraud
 */
public enum SSOProvider {
    GOOGLE("google"),
    LINKEDIN("linkedin");

    private String ssoProviderName;

    SSOProvider(String ssoProviderName) {
        this.ssoProviderName = ssoProviderName;
    }

    @Override
    public String toString() {
        return ssoProviderName;
    }

    @JsonCreator
    public static SSOProvider fromString(String ssoProviderName) {
        return Arrays.stream(SSOProvider.class.getEnumConstants())
                .filter(s -> StringUtils.equalsIgnoreCase(s.toString(), ssoProviderName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}