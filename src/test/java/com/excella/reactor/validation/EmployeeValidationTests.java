package com.excella.reactor.validation;

import com.excella.reactor.domain.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import javax.validation.Validation;
import javax.validation.Validator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class EmployeeValidationTests {

  private Employee employee;
  private Validator validator;

  @BeforeTest
  public void runBeforeTest() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @BeforeMethod
  public void runBeforeEachMethod() {
    employee = new Employee();
    var bio = new Bio();
    var contact = new Contact();
    var employeeSkill = new EmployeeSkill();
    var skill = new Skill();
    var address = new Address();

    employee.setBio(bio);
    employee.setContact(contact);
    employee.setSkills(Arrays.asList(employeeSkill));

    bio.setEthnicity(Ethnicity.CAUCASIAN);
    bio.setFirstName("John");
    bio.setLastName("Doe");
    bio.setGender(Gender.MALE);
    bio.setUsCitizen(Boolean.TRUE);
    bio.setBirthDate(LocalDate.now().minusYears(18));

    contact.setAddress(address);
    contact.setEmail("john.doe@test.com");
    contact.setPhoneNumber("(571)555-5555");

    address.setLine1("1 Fake St");
    address.setCity("Portsmouth");
    address.setStateCode("VA");
    address.setZipCode("23523");

    employeeSkill.setSkill(skill);
    employeeSkill.setProficiency(SkillProficiency.HIGH);
    employeeSkill.setPrimary(Boolean.TRUE);

    skill.setId(1L); // Currently we don't validate ID (Liskov); this is a placeholder.
  }

  @Test
  public void employee_is_valid_when_all_fields_valid() {
    assert isValidEmployee();
  }

  @Test
  public void employee_is_invalid_when_bio_invalid() {
    employee.setBio(new Bio());
    assert !isValidEmployee();
  }

  @Test
  public void employee_is_invalid_when_bio_null() {
    employee.setBio(null);
    assert !isValidEmployee();
  }

  @Test
  public void employee_is_invalid_when_contact_invalid() {
    employee.setContact(new Contact());
    assert !isValidEmployee();
  }

  @Test
  public void employee_is_invalid_when_contact_null() {
    employee.setContact(null);
    assert !isValidEmployee();
  }

  @Test
  public void employee_is_invalid_when_skills_list_empty() {
    employee.setSkills(new ArrayList<>());
    assert !isValidEmployee();
  }

  @Test
  public void employee_is_invalid_when_skills_list_null() {
    employee.setSkills(null);
    assert !isValidEmployee();
  }

  @Test
  public void employee_is_invalid_when_skills_list_contains_invalid() {
    employee.getSkills().get(0).setPrimary(null);
    assert !isValidEmployee();
  }

  @Test
  public void employee_is_invalid_when_skills_list_contains_null() {
    employee.getSkills().set(0, null);
    assert !isValidEmployee();
  }

  private boolean isValidEmployee() {
    return validator.validate(employee).isEmpty();
  }
}
