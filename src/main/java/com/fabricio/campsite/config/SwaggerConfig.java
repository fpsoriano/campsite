package com.fabricio.campsite.config;

import io.swagger.annotations.Api;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Autowired
  private Environment env;

  @Value("${swaggerBaseUrl}")
  private String baseUrl;

  @Bean
  public Docket productApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(metaData())
        .useDefaultResponseMessages(false)
        .host(baseUrl)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo metaData() {
    final String projectVersion = env.getProperty("API_VERSION");
    return new ApiInfoBuilder()
        .title("Campsite Service")
        .description("\"Spring Boot REST API for Campsite Service\"")
        .version(projectVersion)
        .license("Fabricio © 2020")
        .build();
  }

  @Bean
  public UiConfiguration uiConfig() {
    return UiConfigurationBuilder.builder()
        .displayRequestDuration(true)
        .validatorUrl(Strings.EMPTY)
        .build();
  }
}