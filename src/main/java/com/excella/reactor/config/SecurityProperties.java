package com.excella.reactor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Data
@ConfigurationProperties(value = "security", ignoreInvalidFields = true)
public class SecurityProperties {
  private JwtProperties jwt;

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
