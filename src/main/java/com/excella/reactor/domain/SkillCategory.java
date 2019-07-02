package com.excella.reactor.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "skill_category")
public class SkillCategory extends DomainModel {
  @NotEmpty private String name;
}
