package com.excella.reactor.validation.constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.excella.reactor.validation.constraints.validators.AdultBirthdateValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;

/**
 * The annotated element must represent the birthdate of an adult.
 *
 * <p>The age of majority can be customized.
 *
 * <p>Currently only supports {@code java.time.LocalDate} elements using the system clock. Other
 * temporal elements could be added, but if the ability to customize the clock provider is needed,
 * look into the type hierarch of the {@code @Past} annotation's reference implementations, for
 * example {@code
 * org.hibernate.validator.internal.constraintvalidators.bv.time.pastPastValidatorForLocalDate}
 *
 * <p>{@code null} elements are considered valid.
 */
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = AdultBirthdateValidator.class)
@Documented
@Past
public @interface AdultBirthdate {
  String message() default "The birthdate indicates this person is not an adult.";

  Class<?>[] groups() default {};

  /** The age of majority, in years. Defaults to 18. */
  @Positive
  int ageOfMajority() default 18;

  Class<? extends Payload>[] payload() default {};

  @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    AdultBirthdate[] value();
  }
}
