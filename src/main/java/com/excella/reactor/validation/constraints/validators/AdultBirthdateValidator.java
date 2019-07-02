package com.excella.reactor.validation.constraints.validators;

import com.excella.reactor.validation.constraints.AdultBirthdate;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AdultBirthdateValidator implements ConstraintValidator<AdultBirthdate, LocalDate> {
  private int ageOfMajority;

  @Override
  public void initialize(AdultBirthdate annotation) {
    this.ageOfMajority = annotation.ageOfMajority();
  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    return value == null || value.plusYears(ageOfMajority).minusDays(1).isBefore(LocalDate.now());
  }
}
