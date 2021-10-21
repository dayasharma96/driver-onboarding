package com.uber.driver.onboarding.web.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * Configuration class to setup swagger api doc.
 * <p>
 * Uri : http://{host}:{port}/{context-path}/swagger-ui/
 *
 * @author drs
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any())
                .build().apiInfo(metadata());
    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

    private ApiInfo metadata() {
        return new ApiInfo("Uber Driver Onboarding System", "REST APIs for Driver Onboarding Application", "1.0", null, new Contact("Daya Sharma", StringUtils.EMPTY, "dayasharma96@gmail.com"), StringUtils.EMPTY, StringUtils.EMPTY, new ArrayList<>());
    }

}
