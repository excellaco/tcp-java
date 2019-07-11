package com.excella.reactor.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "skills")
@ToString(exclude = "skills")
@NoArgsConstructor
@Entity
public class Employee extends DomainModel {
  @Embedded @Valid @NotNull private Bio bio;

  @Embedded @Valid @NotNull private Contact contact;

  @JsonManagedReference
  @OneToMany(
      fetch = FetchType.EAGER,
      mappedBy = "employee",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @NotEmpty
  private List<@Valid @NotNull EmployeeSkill> skills;
}
