package com.excella.reactor.domain;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class DomainModel implements Serializable {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  protected Long id;
}
