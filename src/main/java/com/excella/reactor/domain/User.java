package com.excella.reactor.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "authorities")
@ToString(exclude = "authorities")
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends DomainModel implements UserDetails {
  @JsonManagedReference
  @OneToMany(
      fetch = FetchType.EAGER,
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Collection<UserAuthority> authorities;

  private String password;
  private String username;
  private String email;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private boolean enabled;
}
