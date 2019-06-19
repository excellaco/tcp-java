package com.excella.reactor.domain;

import javax.persistence.*;
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
  @Id private Long id;
  private String name;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "category_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private SkillCategory category;
}
