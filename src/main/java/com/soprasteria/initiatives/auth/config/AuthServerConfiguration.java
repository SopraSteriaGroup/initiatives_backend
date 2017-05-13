package com.soprasteria.initiatives.auth.config;

import com.soprasteria.initiatives.auth.config.properties.CertProperties;
import com.soprasteria.initiatives.auth.config.properties.TokenProperties;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * OAuth2 auth server config
 *
 * @author jntakpe
 * @author cegiraud
 * @see AuthorizationServerConfigurerAdapter
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final CertProperties certProperties;

    private final OAuth2ClientProperties oAuth2ClientProperties;

    private final TokenProperties tokenProperties;

    private final ResourceLoader resourceLoader;

    public AuthServerConfiguration(AuthenticationManager authenticationManager,
                                   CertProperties certProperties,
                                   OAuth2ClientProperties oAuth2ClientProperties,
                                   TokenProperties tokenProperties,
                                   ResourceLoader resourceLoader) {
        this.authenticationManager = authenticationManager;
        this.certProperties = certProperties;
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.tokenProperties = tokenProperties;
        this.resourceLoader = resourceLoader;
    }

    private JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(getKeyPair());
        jwtAccessTokenConverter.setAccessTokenConverter(getCustomAccessTokenConverter());
        return jwtAccessTokenConverter;
    }

    private KeyPair getKeyPair() {
        Resource cert = resourceLoader.getResource(certProperties.getFilePath());
        char[] password = certProperties.getPassword().toCharArray();
        return new KeyStoreKeyFactory(cert, password).getKeyPair(certProperties.getCertAlias());
    }

    private CustomAccessTokenConverter getCustomAccessTokenConverter() {
        CustomAccessTokenConverter customAccessTokenConverter = new CustomAccessTokenConverter();
        customAccessTokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
        return customAccessTokenConverter;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        JwtAccessTokenConverter jwtAccessTokenConverter = jwtAccessTokenConverter();
        endpoints
                .accessTokenConverter(jwtAccessTokenConverter)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(oAuth2ClientProperties.getClientId())
                .secret(oAuth2ClientProperties.getClientSecret())
                .authorizedGrantTypes("authorization_code", "implicit", "password", "refresh_token")
                .accessTokenValiditySeconds(tokenProperties.getAccessValiditySeconds())
                .refreshTokenValiditySeconds(tokenProperties.getRefreshValidityMinutes() * 60)
                .scopes("openid")
                .autoApprove(true);
    }
}
