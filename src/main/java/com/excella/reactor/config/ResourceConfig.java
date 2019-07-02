package com.excella.reactor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {

  private TokenStore tokenStore;

  public ResourceConfig(final TokenStore tokenStore) {
    this.tokenStore = tokenStore;
  }

  /**
   * Sets the token store for the resource server.
   *
   * @see AuthorizationConfig#tokenStore()
   * @param resources resources
   */
  @Override
  public void configure(final ResourceServerSecurityConfigurer resources) {
    resources.tokenStore(tokenStore);
  }

  /**
   * Authentication setup for endpoints. Swagger URIs are white-listed. Everything else requires the
   * user to be authenticated.
   *
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
            "/webjars/springfox-swagger-ui/**")
        .permitAll()
        .antMatchers("/**")
        .authenticated();
  }
}
