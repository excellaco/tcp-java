package com.excella.reactor.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@Data
@Entity
@Table(name = "employee_skill")
public class EmployeeSkill implements Serializable {
  @Id
  @ManyToOne(fetch = FetchType.LAZY) // Prevents recursion when merging
  @JoinColumn(name = "employee_id")
  @JsonBackReference
  private Employee employee;

  @Id
  @ManyToOne
  @JoinColumn(name = "skill_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @NotNull
  private Skill skill;

  @NotNull
  @Enumerated(EnumType.STRING)
  private SkillProficiency proficiency;

  @NotNull
  @Column(name = "is_primary")
  private Boolean primary;
}
