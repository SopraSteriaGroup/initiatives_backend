package com.soprasteria.initiatives.auth.config;

import com.soprasteria.initiatives.auth.config.properties.ApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Swagger configuration
 *
 * @author jntakpe
 */
@Configuration
@EnableSwagger2
@Profile(ProfileConstants.NOT_TEST_PROFILE)
public class SwaggerConfig {

    private static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    private final ApiProperties apiProperties;

    public SwaggerConfig(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Bean
    public Docket swaggerDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .globalOperationParameters(Collections.singletonList(
                        new ParameterBuilder()
                                .name("Authorization")
                                .description("Provide if necessary")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .build()))
                .forCodeGeneration(true)
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();
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
