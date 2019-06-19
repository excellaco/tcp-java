package com.excella.reactor.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.Email;

import lombok.Builder;
import lombok.Data;

@Data
@Embeddable
public class Contact implements Serializable {
  @Email
  private String email;
  private String phoneNumber;
  @Embedded private Address address;
}
