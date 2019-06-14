package com.excella.reactor.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Employee extends DomainModel {

  @Embedded private Bio bio;

  @Embedded private Contact contact;
}
