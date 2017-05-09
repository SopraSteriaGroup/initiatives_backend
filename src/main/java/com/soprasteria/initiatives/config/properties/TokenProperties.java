package com.soprasteria.initiatives.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OAuth2 token properties
 *
 * @author jntakpe
 */
@Component
@ConfigurationProperties("auth.token")
public class TokenProperties {

    private int accessValiditySeconds = 1800;

    private int refreshValidityMinutes = 180;

    public int getAccessValiditySeconds() {
        return accessValiditySeconds;
    }

    public void setAccessValiditySeconds(int accessValiditySeconds) {
        this.accessValiditySeconds = accessValiditySeconds;
    }

    public int getRefreshValidityMinutes() {
        return refreshValidityMinutes;
    }

    public void setRefreshValidityMinutes(int refreshValidityMinutes) {
        this.refreshValidityMinutes = refreshValidityMinutes;
    }
}
