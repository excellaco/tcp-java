package com.excella.reactor.repositories;

import com.excella.reactor.domain.TcpUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<TcpUser, UUID> {

  @EntityGraph(attributePaths = "authorities")
  Optional<TcpUser> findOneWithAuthoritiesById(UUID id);

  @EntityGraph(attributePaths = "authorities")
  Optional<TcpUser> findOneWithAuthoritiesByLogin(String login);

  @EntityGraph(attributePaths = "authorities")
  Optional<TcpUser> findOneWithAuthoritiesByEmail(String email);

}
