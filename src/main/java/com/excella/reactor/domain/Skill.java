package com.excella.reactor.domain;

import com.excella.reactor.validation.groups.SkillChecks;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
  @NotEmpty(groups = {SkillChecks.class})
  private String name;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "category_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @NotNull(groups = {SkillChecks.class})
  private SkillCategory category;
}
