package com.excella.reactor.domain;

import javax.persistence.*;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excella.reactor.validation.EmployeeChecks;
import com.excella.reactor.validation.SkillChecks;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * This class represents an immutable skill, as defined in the domain, as opposed to a skill
 * possessed by an employee.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Skill extends DomainModel {
  @NotEmpty
  private String name;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "category_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private SkillCategory category;

  @Override
  @NotNull(groups = {EmployeeChecks.class})
  public void setId(Long id) {
    this.id = id;
  }

}
