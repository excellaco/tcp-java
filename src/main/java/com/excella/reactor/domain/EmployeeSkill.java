package com.excella.reactor.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import javax.persistence.*;
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
  private Skill skill;

  @Enumerated(EnumType.STRING)
  private SkillProficiency proficiency;

  @Column(name = "is_primary")
  private boolean primary;
}
