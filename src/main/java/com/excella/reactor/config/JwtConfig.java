package com.excella.reactor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
public class JwtConfig {

  @Autowired
  TcpProperties tcpProperties;

  @Bean
  public JwtTokenStore tokenStore() {
    return new JwtTokenStore(jwtAccessTokenConverter());
  }

  /**
   * Configure access token converter with signing key.
   * @return
   */
  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    TcpProperties.KeyStore keystore = tcpProperties.getKeyStore();
    KeyPair keyPair = new KeyStoreKeyFactory(
        new ClassPathResource(keystore.getName()), keystore.getPassword().toCharArray())
        .getKeyPair(keystore.getAlias());

    converter.setKeyPair(keyPair);
    return converter;
  }
}
