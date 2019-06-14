package com.excella.reactor.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Embeddable
public class Address {
  private String line1;
  private String line2;
  private String city;

  @Size(min = 2, max = 2)
  private String stateCode; // temporary

  private String zipCode;
}
