package com.excella.reactor.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Data
@ConfigurationProperties(value = "security", ignoreInvalidFields = true)
public class SecurityProperties {

  private JwtProperties jwt;

  @Data
  static class JwtProperties {

    private Resource keyStore;
    private String keyStorePassword;
    private String keyPairAlias;
    private String keyPairPassword;


  }
}