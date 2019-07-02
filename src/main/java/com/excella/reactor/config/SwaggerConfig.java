package com.excella.reactor.config;

import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SecurityProperties.class)
public class SwaggerConfig {

  private SecurityProperties securityProperties;
  private ServletContext servletContext;

  private final AuthorizationScope[] scopes =
      new AuthorizationScope[] {
        new AuthorizationScope("read", "read all"), new AuthorizationScope("write", "write all")
      };

  @Autowired
  public SwaggerConfig(
      final SecurityProperties securityProperties, final ServletContext servletContext) {
    this.securityProperties = securityProperties;
    this.servletContext = servletContext;
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(Predicates.not(PathSelectors.regex("/error")))
        .build()
        .securitySchemes(Collections.singletonList(securitySchema()))
        .securityContexts(Collections.singletonList(securityContext()))
        .genericModelSubstitutes(ResponseEntity.class, Mono.class)
        .genericModelSubstitutes(ResponseEntity.class, Flux.class)
        .apiInfo(apiInfo());
  }

  private OAuth securitySchema() {
    List<AuthorizationScope> authorizationScopeList = Arrays.asList(scopes);
    return new OAuth(
        "oauth2schema",
        authorizationScopeList,
        Collections.singletonList(
            new ResourceOwnerPasswordCredentialsGrant(
                String.format("%s%s", servletContext.getContextPath(), "/oauth/token"))));
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .forPaths(PathSelectors.ant("/**"))
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    return Collections.singletonList(new SecurityReference("oauth2schema", scopes));
  }

  @Bean
  public SecurityConfiguration securityInfo() {
    return SecurityConfigurationBuilder.builder()
        .clientId(securityProperties.getOauth2().getClient().getClientId())
        .clientSecret(securityProperties.getOauth2().getClient().getClientSecret())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("TCP API Contract")
        .description("API Contract for Excella's Tech Challenge Platform (TCP)")
        .contact(
            new Contact("Kenneth Russell", "http://www.excella.com", "kenneth.russell@excella.com"))
        .version("1.0.0")
        .build();
  }
}
