package com.excella.reactor.config;

import com.excella.reactor.common.exceptions.CustomSetupException;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@EnableResourceServer
@EnableConfigurationProperties(SecurityProperties.class)
public class ResourceConfig extends ResourceServerConfigurerAdapter {

  private final SecurityProperties securityProperties;

  private TokenStore tokenStore;

  public ResourceConfig(final SecurityProperties securityProperties, final TokenStore tokenStore) {
    this.securityProperties = securityProperties;
    this.tokenStore = tokenStore;
  }

  @Override
  public void configure(final ResourceServerSecurityConfigurer resources) {
    resources.tokenStore(tokenStore);
  }

  /**
   * Authentication setup for endpoints.
   * Swagger URIs are white-listed. Everything else requires authentication.
   * @param http HttpSeccurity
   * @throws Exception exception
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(
            "/",
            "/app/**",
            "/lib/**",
            "/swagger-*/**",
            "/v2/api-docs",
            "/api-docs/**",
            "/configuration/**",
            "/webjars/springfox-swagger-ui/**"
        ).permitAll()
        .antMatchers("/**").authenticated();
  }
}