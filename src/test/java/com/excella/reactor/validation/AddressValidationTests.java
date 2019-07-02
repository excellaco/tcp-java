package com.excella.reactor.validation;

import com.excella.reactor.domain.Address;
import javax.validation.Validation;
import javax.validation.Validator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class AddressValidationTests {
  private Address address;
  private Validator validator;

  @BeforeTest
  public void runBeforeTest() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @BeforeMethod
  public void runBeforeEachMethod() {
    address = new Address();

    address.setStateCode("VA");
  }

  @Test
  public void address_is_valid_if_all_fields_valid() {
    assert isValidAddress();
  }

  @Test
  public void address_is_invalid_if_statecode_too_long() {
    address.setStateCode("VAB");
    assert !isValidAddress();
  }

  @Test
  public void address_is_invalid_if_statecode_too_short() {
    address.setStateCode("V");
    assert !isValidAddress();
  }

  @Test
  public void address_is_invalid_if_statecode_null() {
    address.setStateCode("V");
    assert !isValidAddress();
  }

  private boolean isValidAddress() {
    return validator.validate(address).isEmpty();
  }
}
