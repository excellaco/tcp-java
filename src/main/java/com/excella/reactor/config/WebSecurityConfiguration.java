package com.excella.reactor.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final DataSource dataSource;

  private PasswordEncoder passwordEncoder;
  private UserDetailsService userDetailsService;

  public WebSecurityConfiguration(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Sets up Authentication with a UserDetailsService and PasswordEncoder.
   *
   * @see #userDetailsService()
   * @see #passwordEncoder()
   * @param auth AuthenticationManagerBuider
   * @throws Exception exception
   */
  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
  }

  /**
   * Returns the default implementation of the AuthenticationManager and creates a bean from it.
   *
   * @return AuthenticationManager
   * @throws Exception exception
   */
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   * Creates a password encoder.
   *
   * @return PasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    if (passwordEncoder == null) {
      passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    return passwordEncoder;
  }

  /**
   * Overrides default implementation and sets up UserDetailsService using default JdbcDaoImpl and
   * sets the datasource to the application's default (in this case, it's Postgres). See the
   * properties under spring.datasource in the application.yml.
   *
   * @see WebSecurityConfigurerAdapter#userDetailsServiceBean()
   * @return UserDetailsService
   */
  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    if (userDetailsService == null) {
      userDetailsService = new JdbcDaoImpl();
      ((JdbcDaoImpl) userDetailsService).setDataSource(dataSource);
    }
    return userDetailsService;
  }
}
