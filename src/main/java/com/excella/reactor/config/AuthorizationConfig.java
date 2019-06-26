package com.excella.reactor.config;

import java.security.KeyPair;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(SecurityProperties.class)
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

  private final DataSource dataSource;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final SecurityProperties securityProperties;
  private JwtAccessTokenConverter jwtAccessTokenConverter;
  private TokenStore tokenStore;

  public AuthorizationConfig(
      final DataSource dataSource,
      final PasswordEncoder passwordEncoder,
      final AuthenticationManager authenticationManager,
      final SecurityProperties securityProperties) {
    this.dataSource = dataSource;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.securityProperties = securityProperties;
  }

  /**
   * Creates a JWT Token Store given a JWT Access Token Converter.
   *
   * @return TokenStore
   */
  @Bean
  public TokenStore tokenStore() {
    if (tokenStore == null) {
      tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
    }
    return tokenStore;
  }

  /**
   * Sets up the token service that authorizes and authenticates with JWT, users stored in the DB,
   * and the Authentication Manager.
   *
   * @param tokenStore tokenStore
   * @param clientDetailsService clientDetailsService
   * @return Default token service
   */
  @Bean
  public DefaultTokenServices tokenServices(
      final TokenStore tokenStore, final ClientDetailsService clientDetailsService) {
    var tokenServices = new DefaultTokenServices();
    tokenServices.setSupportRefreshToken(true);
    tokenServices.setTokenStore(tokenStore);
    tokenServices.setClientDetailsService(clientDetailsService);
    tokenServices.setAuthenticationManager(this.authenticationManager);
    return tokenServices;
  }

  /**
   * Creates a JWT access token converter with the JKS file in the classpath.
   *
   * @return JwtAccessTokenConcerter
   */
  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    if (jwtAccessTokenConverter != null) {
      return jwtAccessTokenConverter;
    }

    SecurityProperties.JwtProperties jwtProperties = securityProperties.getJwt();
    KeyPair keyPair = keyPair(jwtProperties, keyStoreKeyFactory(jwtProperties));

    jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setKeyPair(keyPair);
    return jwtAccessTokenConverter;
  }

  /**
   * Stores OAuth client details in database.
   *
   * @param clients clients
   * @throws Exception exception
   */
  @Override
  public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    clients.jdbc(this.dataSource);
  }

  @Override
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
        .authenticationManager(this.authenticationManager)
        .accessTokenConverter(jwtAccessTokenConverter())
        .tokenStore(tokenStore());
  }

  /**
   * Configures Authentication, setting password encoder and permissions on a token.
   *
   * @see WebSecurityConfiguration#passwordEncoder()
   * @param oauthServer oauthServer
   */
  @Override
  public void configure(final AuthorizationServerSecurityConfigurer oauthServer) {
    oauthServer
        .passwordEncoder(this.passwordEncoder)
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()");
  }

  /**
   * Get KeyPair object from a JKS file.
   *
   * @param jwtProperties jwtProperties
   * @param keyStoreKeyFactory keyStoreKeyFactory
   * @return KeyPair object.
   */
  private KeyPair keyPair(
      SecurityProperties.JwtProperties jwtProperties, KeyStoreKeyFactory keyStoreKeyFactory) {
    return keyStoreKeyFactory.getKeyPair(
        jwtProperties.getKeyPairAlias(), jwtProperties.getKeyPairPassword().toCharArray());
  }

  /**
   * Creates a keyStoreKeyFactory with JWT Properties. Gets the KeyPair value from the JKS file
   * using the credentials in the JWTProperties in the application.yml file.
   *
   * @param jwtProperties jwtProperties
   * @return KeyStoreKeyFactory
   */
  private KeyStoreKeyFactory keyStoreKeyFactory(SecurityProperties.JwtProperties jwtProperties) {
    return new KeyStoreKeyFactory(
        jwtProperties.getKeyStore(), jwtProperties.getKeyStorePassword().toCharArray());
  }
}
