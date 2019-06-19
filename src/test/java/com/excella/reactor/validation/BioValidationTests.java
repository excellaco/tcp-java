package com.excella.reactor.validation;

import com.excella.reactor.domain.Bio;
import com.excella.reactor.domain.Ethnicity;
import com.excella.reactor.domain.Gender;
import java.time.LocalDate;
import javax.validation.Validation;
import javax.validation.Validator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class BioValidationTests {
  private Bio bio;
  private Validator validator;

  @BeforeTest
  public void runBeforeTest() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @BeforeMethod
  public void runBeforeEachMethod() {
    bio = new Bio();
    bio.setFirstName("John");
    bio.setLastName("Doe");
    bio.setEthnicity(Ethnicity.CAUCASIAN);
    bio.setGender(Gender.MALE);
    bio.setBirthDate(LocalDate.now().minusYears(18));
    bio.setUsCitizen(true);
  }

  @Test
  public void bio_is_valid_if_all_fields_valid() {
    assert isValidBio();
  }

  @Test
  public void bio_is_invalid_if_first_name_null() {
    bio.setFirstName(null);
    assert !isValidBio();
  }

  @Test
  public void bio_is_invalid_if_first_name_empty() {
    bio.setFirstName("");
    assert !isValidBio();
  }

  @Test
  public void bio_is_invalid_if_last_name_null() {
    bio.setLastName(null);
    assert !isValidBio();
  }

  @Test
  public void bio_is_invalid_if_last_name_empty() {
    bio.setLastName("");
    assert !isValidBio();
  }

  @Test
  public void bio_is_invalid_if_ethnicity_null() {
    bio.setEthnicity(null);
    assert !isValidBio();
  }

  @Test
  public void bio_is_invalid_if_gender_null() {
    bio.setGender(null);
    assert !isValidBio();
  }

  @Test
  public void bio_is_invalid_if_usCitizen_null() {
    bio.setUsCitizen(null);
    assert !isValidBio();
  }

  @Test
  public void bio_is_invalid_if_birthdate_null() {
    bio.setBirthDate(null);
    assert !isValidBio();
  }

  @Test
  public void bio_is_invalid_if_birthdate_in_future() {
    bio.setBirthDate(LocalDate.now().plusDays(3));
    assert !isValidBio();
  }

  @Test
  public void bio_is_invalid_if_birthdate_not_adult() {
    bio.setBirthDate(LocalDate.now().minusYears(16));
    assert !isValidBio();
  }

  private boolean isValidBio() {
    return validator.validate(bio).isEmpty();
  }
}
