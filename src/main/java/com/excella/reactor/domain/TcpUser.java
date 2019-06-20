package com.excella.reactor.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "users")
public class TcpUser extends DomainModel {
  @NotNull
  @Size(min = 1, max = 50)
  @Column(length = 50, unique = true, nullable = false)
  private String login;

  @JsonIgnore
  @NotNull
  @Size(min = 60, max = 60)
  @Column(name = "password_hash", length = 60)
  private String password;

  @Size(max = 50)
  @Column(name = "first_name", length = 50)
  private String firstName;

  @Size(max = 50)
  @Column(name = "last_name", length = 50)
  private String lastName;

  @Email
  @Size(min = 5, max = 100)
  @Column(length = 100, unique = true)
  private String email;

  @ElementCollection(targetClass = Authority.class)
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "users_authorities", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "authority")
  private Set<Authority> authorities = new HashSet<>();


}
