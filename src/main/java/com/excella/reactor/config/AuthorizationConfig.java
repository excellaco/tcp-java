package com.excella.reactor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;


@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter
    implements ApplicationContextAware {

  private final DataSource dataSource;

  private final AuthenticationManager authenticationManager;

  private ApplicationContext applicationContext;

  TcpProperties tcpProperties;

  public AuthorizationConfig(
      TcpProperties tcpProperties,
      @Qualifier("dataSource") DataSource dataSource,
      @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager) {
    this.tcpProperties = tcpProperties;
    this.dataSource = dataSource;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(dataSource);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    Collection<TokenEnhancer> tokenEnhancers =
        applicationContext.getBeansOfType(TokenEnhancer.class).values();

    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));

    endpoints
        .authenticationManager(authenticationManager)
        .tokenStore(jdbcTokenStore())
        .tokenEnhancer(tokenEnhancerChain)
        .reuseRefreshTokens(false)
        .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    oauthServer
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()");
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean
  public TokenStore jdbcTokenStore() {
    return new JdbcTokenStore(dataSource);
  }


  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(jdbcTokenStore());
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }

//  @Override
//  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//    endpoints.tokenStore(tokenStore())
//        .accessTokenConverter(accessTokenConverter());
//  }


//  @Bean
//  public JwtAccessTokenConverter accessTokenConverter() {
//    final JwtAccessTokenConverter converter = new JwtTokenConverter();
//    final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
//        new ClassPathResource("server.jks"), passcode.toCharArray());
//    converter.setKeyPair(keyStoreKeyFactory.getKeyPair("server"));
//    return converter;
//  }

//  @Bean
//  @Primary
//  public DefaultTokenServices tokenServices() {
//    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//    defaultTokenServices.setTokenStore(tokenStore());
//    defaultTokenServices.setSupportRefreshToken(true);
//    return defaultTokenServices;
//  }




}

