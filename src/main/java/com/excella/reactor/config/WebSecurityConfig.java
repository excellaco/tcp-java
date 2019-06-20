package com.excella.reactor.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  private UserDetailsService userDetailsService;

  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  private TcpProperties tcpProperties;

  public WebSecurityConfig(
      @Qualifier("defaultUserDetailsService") UserDetailsService userDetailsService,
      AuthenticationManagerBuilder authenticationManagerBuilder,
      TcpProperties tcpProperties) {
    this.userDetailsService = userDetailsService;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.tcpProperties = tcpProperties;
  }

  @PostConstruct
  public void init() throws Exception {
    authenticationManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
        .ignoring()
        .antMatchers(HttpMethod.OPTIONS, "/**");
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    CorsConfiguration config = tcpProperties.getCors();

    if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
      source.registerCorsConfiguration("/api/**", config);
    }

    return new CorsFilter(source);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


}
