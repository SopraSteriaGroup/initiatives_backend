package com.soprasteria.initiatives.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Certificate properties
 *
 * @author jntakpe
 */
@Component
@Validated
@ConfigurationProperties("auth.cert")
public class CertProperties {

    @NotNull
    private String filePath;

    @NotNull
    private String password;

    @NotNull
    private String certAlias;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getCertAlias() {
        return certAlias;
    }

    public CertProperties setCertAlias(String certAlias) {
        this.certAlias = certAlias;
        return this;
    }
}
