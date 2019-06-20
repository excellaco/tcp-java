package com.excella.reactor.service.impl;

import com.excella.reactor.domain.TcpUser;
import com.excella.reactor.repositories.UserRepository;
import com.excella.reactor.service.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


@Service("defaultUserDetailsService")
@Slf4j
public class UserDetailsServiceImpl implements CrudService<TcpUser>, UserDetailsService {

  private UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public JpaRepository<TcpUser, Long> getRepository() {
    return null;
  }

  @Override
  public UserDetails loadUserByUsername(final String login) {
    String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
    log.debug("logging in " + lowercaseLogin);
    Optional<TcpUser> userByEmailFromDatabase =
        userRepository.findOneWithAuthoritiesByEmail(lowercaseLogin);
    log.debug("user is here: " + userByEmailFromDatabase.get());
    return userByEmailFromDatabase.map(this::createSpringSecurityUser)
        .orElseGet(() -> {
          Optional<TcpUser> userByLoginFromDatabase =
              userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin);

          return userByLoginFromDatabase.map(this::createSpringSecurityUser)
              .orElseThrow(() -> new UsernameNotFoundException(
                  String.format("User %s was not found in the database", lowercaseLogin)));
        });
  }

  private User createSpringSecurityUser(TcpUser user) {
    List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
        .map(authority -> new SimpleGrantedAuthority(authority.toString()))
        .collect(Collectors.toList());

    return new User(user.getLogin(), user.getPassword(), grantedAuthorities);
  }
}
