package com.excella.reactor.validation;

import com.excella.reactor.domain.*;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import org.testng.annotations.*;

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
    employee.setSkills(List.of(employeeSkill));

    bio.setEthnicity(Ethnicity.CAUCASIAN);
    bio.setFirstName("John");
    bio.setLastName("Doe");
    bio.setGender(Gender.MALE);
    bio.setUsCitizen(Boolean.TRUE);
    bio.setBirthDate(LocalDate.now().minusYears(18));

    contact.setAddress(address);
    contact.setEmail("john.doe@test.com");
    contact.setPhoneNumber("5715555555");

    address.setLine1("1 Fake St");
    address.setCity("Portsmouth");
    address.setStateCode("VA");
    address.setZipCode("23523");

    employeeSkill.setSkill(skill);
    employeeSkill.setProficiency(SkillProficiency.HIGH);
    employeeSkill.setPrimary(Boolean.TRUE);

    skill.setId(1L);
  }

  @AfterMethod
  public void runAfterEachMethod() {}

  @Test
  public void employee_is_valid_when_all_fields_valid() {
    assert validator.validate(employee, EmployeeChecks.class, Default.class).isEmpty();
  }
}
