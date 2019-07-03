package com.excella.reactor.validation;

import com.excella.reactor.domain.Address;
import com.excella.reactor.domain.Contact;
import javax.validation.Validation;
import javax.validation.Validator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class ContactValidationTests {
  private Contact contact;
  private Validator validator;

  @BeforeTest
  public void runBeforeTest() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @BeforeMethod
  public void runBeforeEachMethod() {
    contact = new Contact();
    var address = new Address();

    contact.setAddress(address);
    contact.setPhoneNumber("(571)555-5555");
    contact.setEmail("john.doe@test.com");

    address.setStateCode("VA");
  }

  @Test
  public void contact_is_valid_if_all_fields_valid() {
    assert isValidContact();
  }

  @Test
  public void contact_is_invalid_if_address_invalid() {
    contact.getAddress().setStateCode(null);
    assert !isValidContact();
  }

  @Test
  public void contact_is_invalid_if_address_null() {
    contact.getAddress().setStateCode(null);
    assert !isValidContact();
  }

  @Test
  public void contact_is_invalid_if_phone_invalid() {
    contact.setPhoneNumber("57155555550");
    assert !isValidContact();
  }

  @Test
  public void contact_is_invalid_if_phone_null() {
    contact.setPhoneNumber(null);
    assert !isValidContact();
  }

  @Test
  public void contact_is_invalid_if_email_invalid() {
    contact.setEmail("Invalid E-mail Address");
    assert !isValidContact();
  }

  @Test
  public void contact_is_invalid_if_email_null() {
    contact.setEmail(null);
    assert !isValidContact();
  }

  private boolean isValidContact() {
    return validator.validate(contact).isEmpty();
  }
}
