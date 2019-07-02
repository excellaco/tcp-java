package com.excella.reactor.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "skills")
@ToString(exclude = "skills")
@NoArgsConstructor
@Slf4j
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
  List<@Valid @NotNull EmployeeSkill> skills;
}
