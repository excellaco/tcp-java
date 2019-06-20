package com.excella.reactor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;


@Component
@ConfigurationProperties(prefix = "tcp", ignoreUnknownFields = false)
public class TcpProperties {
  private KeyStore keyStore = new KeyStore();

  private WebClientConfiguration webClientConfiguration = new WebClientConfiguration();

  private final CorsConfiguration cors = new CorsConfiguration();

  public KeyStore getKeyStore() {
    return keyStore;
  }

  public WebClientConfiguration getWebClientConfiguration() {
    return webClientConfiguration;
  }

  public CorsConfiguration getCors() {
    return cors;
  }


  @Getter
  @Setter
  public static class KeyStore {

    private String name;
    private String password;
    private String alias;

  }

  @Getter
  @Setter
  public static class WebClientConfiguration {

    private int accessTokenValidityInSeconds = 5 * 60;

    private int refreshTokenValidityInSecondsForRememberMe = 7 * 24 * 60 * 60;

    private String clientId;
    private String secret;



  }
}
