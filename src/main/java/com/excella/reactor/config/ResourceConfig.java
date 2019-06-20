package com.excella.reactor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {


//  private DefaultTokenServices tokenServices;
//
//  @Autowired
//  public ResourceConfig(DefaultTokenServices tokenServices) {
//    this.tokenServices = tokenServices;
//  }
//
//
//  @Override
//  public void configure(ResourceServerSecurityConfigurer config) {
//    config.tokenServices(tokenServices);
//  }



  private final TokenStore tokenStore;

  private final CorsFilter corsFilter;

  @Autowired
  public ResourceConfig(TokenStore tokenStore, CorsFilter corsFilter) {
    this.tokenStore = tokenStore;
    this.corsFilter = corsFilter;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .exceptionHandling()
        .authenticationEntryPoint((request, response, authException) ->
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
        .csrf()
        .disable()
        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        .headers()
        .frameOptions()
        .disable()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(
            "/",
            "/oauth/**",
            "**/oauth/**",
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

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId("api").tokenStore(tokenStore);
  }


}
