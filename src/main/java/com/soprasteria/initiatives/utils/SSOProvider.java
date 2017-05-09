package com.soprasteria.initiatives.utils;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * @author cegiraud
 */
public enum SSOProvider {
    GOOGLE("google"),
    LINKEDIN("linkedin"),
    FAKE_SSO("fakesso");

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
                .filter(s -> s.ssoProviderName.equalsIgnoreCase(ssoProviderName)
                        || s.name().equalsIgnoreCase(ssoProviderName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}