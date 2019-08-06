package com.excella.reactor.domain;

import com.excella.reactor.validation.groups.PostChecks;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class DomainModel implements Serializable {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Null(
      groups = PostChecks.class,
      message = "ID should not be populated on a POST request. Did you mean to PUT?")
  protected Long id;
}
