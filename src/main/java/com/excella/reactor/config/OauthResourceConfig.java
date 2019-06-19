package com.excella.reactor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableResourceServer
public class OauthResourceConfig extends ResourceServerConfigurerAdapter {


  private DefaultTokenServices tokenServices;

  @Autowired
  public OauthResourceConfig(DefaultTokenServices tokenServices) {
    this.tokenServices = tokenServices;
  }


  @Override
  public void configure(ResourceServerSecurityConfigurer config) {
    config.tokenServices(tokenServices);
  }



}
