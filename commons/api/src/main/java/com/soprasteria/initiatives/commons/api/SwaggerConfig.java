package com.soprasteria.initiatives.commons.api;

import com.soprasteria.initiatives.commons.api.properties.ApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import rx.Single;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Swagger configuration
 *
 * @author jntakpe
 */
@Configuration
@EnableSwagger2
@Profile(ProfileConstants.NOT_TEST_PROFILE)
public class SwaggerConfig {

    private static final String OAUTH_2_NAME = "oauth2";

    private static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    private static final String OPENID_SCOPE = "openid";

    private static final String AUTHORIZE_ENDPOINT = "/oauth/authorize";

    private static final String TOKEN_NAME = "implicit";

    private static final String API_KEY_BEARER = "Bearer ";

    private static final String REALM = "realm";

    private final ApiProperties apiProperties;

    private final OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    public SwaggerConfig(ApiProperties apiProperties, OAuth2ClientProperties oAuth2ClientProperties) {
        this.apiProperties = apiProperties;
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }

    @Bean
    public Docket swaggerDocket(@Value("${auth.gateway.url}") String authUrl) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .forCodeGeneration(true)
                .genericModelSubstitutes(Single.class)
                .ignoredParameterTypes(AuthenticatedUser.class)
                .select()
                .paths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
                .build()
                .securitySchemes(Collections.singletonList(securitySchema(authUrl)))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    @Bean
    public SecurityConfiguration securityInfo(@Value("${spring.application.name}") String appName) {
        return new SecurityConfiguration(oAuth2ClientProperties.getClientId(), oAuth2ClientProperties.getClientSecret(), REALM,
                appName, "", ApiKeyVehicle.HEADER, API_KEY_BEARER, ",");
    }

    private OAuth securitySchema(String authUrl) {
        GrantType grantType = new ImplicitGrant(new LoginEndpoint(authUrl + AUTHORIZE_ENDPOINT), TOKEN_NAME);
        List<AuthorizationScope> scopes = Stream.of(new AuthorizationScope(OPENID_SCOPE, "")).collect(Collectors.toList());
        return new OAuth(OAUTH_2_NAME, scopes, Collections.singletonList(grantType));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.ant(SecurityConstants.SECURED_API_PATH))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] scopes = {new AuthorizationScope(OPENID_SCOPE, "")};
        return Collections.singletonList(new SecurityReference(OAUTH_2_NAME, scopes));
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(apiProperties.getTitle(),
                apiProperties.getDescription(),
                apiProperties.getVersion(),
                apiProperties.getTermsOfServiceUrl(),
                getContact(),
                apiProperties.getLicense(),
                apiProperties.getLicenseUrl());
    }

    private Contact getContact() {
        ApiProperties.Contact contactProperties = apiProperties.getContact();
        return new Contact(contactProperties.getName(), contactProperties.getUrl(), contactProperties.getMail());
    }

}