package com.excella.reactor.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class SkillCategory implements Serializable {
  @JsonProperty("name")
  private String category;
}
