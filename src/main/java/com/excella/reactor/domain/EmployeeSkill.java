package com.excella.reactor.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "employee_skill")
public class EmployeeSkill implements Serializable {
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id")
  @JsonBackReference
  private Employee employee;

  @Id
  @ManyToOne
  @JoinColumn(name = "skill_id")
  @Valid
  @NotNull
  private Skill skill;

  @NotNull
  @Enumerated(EnumType.STRING)
  private SkillProficiency proficiency;

  @NotNull
  @Column(name = "is_primary")
  private Boolean primary;
}
