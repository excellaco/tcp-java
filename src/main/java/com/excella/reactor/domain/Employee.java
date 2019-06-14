package com.excella.reactor.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "skills")
@ToString(exclude = "skills")
@NoArgsConstructor
@Slf4j
@Entity
public class Employee extends DomainModel {
  @Embedded private Bio bio;

  @Embedded private Contact contact;

  @JsonManagedReference
  @OneToMany(
      fetch = FetchType.EAGER,
      mappedBy = "employee",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  List<EmployeeSkill> skills = new ArrayList<>();
}
