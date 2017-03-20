package com.soprasteria.initiatives.auth.config.properties;

import com.soprasteria.initiatives.auth.utils.SSOProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * OAuth2 SSO properties
 *
 * @author cegiraud
 */
@Component
@Validated
@ConfigurationProperties("auth.sso")
public class SSOProperties {

    private Map<SSOProvider, SSOValues> providers;

    public Map<SSOProvider, SSOValues> getProviders() {
        return providers;
    }

    public void setProviders(Map<SSOProvider, SSOValues> providers) {
        this.providers = providers;
    }

    public static class SSOValues {
        private String profileUrl;
        private String id;
        private String firstname;
        private String lastname;

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
    }


}
