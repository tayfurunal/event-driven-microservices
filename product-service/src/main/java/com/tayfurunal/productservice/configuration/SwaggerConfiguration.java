package com.tayfurunal.productservice.configuration;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${swagger.ignoredParameterTypes:#{T(org.apache.commons.lang3.ArrayUtils).EMPTY_CLASS_ARRAY}}")
    private Class[] ignoredParameterTypes;

    @Value("${spring.application.name:Api Documentation}")
    private String appName;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework")))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(ignoredParameterTypes)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                appName,
                "Product Service",
                "1.0.0",
                "",
                new Contact("Tayfur Unal", "tayfurunal.com", "mtayfurunal@gmail.com"),
                ".",
                ".",
                Collections.singletonList(new StringVendorExtension("vendor", "tayfurunal"))
        );
    }
}
