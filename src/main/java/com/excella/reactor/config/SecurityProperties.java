package com.excella.reactor.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Data
@ConfigurationProperties(value = "security", ignoreInvalidFields = true)
public class SecurityProperties {

  private OAuth2Properties oauth2;
  private JwtProperties jwt;

  @Data
  static class OAuth2Properties {
    private OAuth2ClientProperties client;
  }

  @Data
  static class JwtProperties {
    // location of the jks file. e.g. "classpath:server.jks"
    private Resource keyStore;
    // password to get into the JKS file
    private String keyStorePassword;
    // keypair alias name.
    private String keyPairAlias;
    // keypair password for alias above
    private String keyPairPassword;
  }
}
