package com.excella.reactor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

@Configuration
public class RestApiConfig implements RepositoryRestConfigurer {

  @Bean
  public PageableHandlerMethodArgumentResolver customResolver(
      PageableHandlerMethodArgumentResolver pageableResolver) {
    pageableResolver.setOneIndexedParameters(true);
    pageableResolver.setFallbackPageable(PageRequest.of(0, Integer.MAX_VALUE));
    pageableResolver.setMaxPageSize(Integer.MAX_VALUE);
    return pageableResolver;
  }
}