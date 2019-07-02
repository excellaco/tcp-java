package com.excella.reactor.validation;

import com.excella.reactor.domain.SkillCategory;
import javax.validation.Validation;
import javax.validation.Validator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class SkillCategoryValidationTests {
  private SkillCategory skillCategory;
  private Validator validator;

  @BeforeTest
  public void runBeforeTest() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @BeforeMethod
  public void runBeforeEachMethod() {
    skillCategory = new SkillCategory();
    skillCategory.setName("Category");
  }

  @Test
  public void category_is_valid_if_all_fields_valid() {
    assert isValidCategory();
  }

  @Test
  public void category_is_invalid_if_name_null() {
    skillCategory.setName(null);
    assert !isValidCategory();
  }

  @Test
  public void category_is_invalid_if_name_empty() {
    skillCategory.setName("");
    assert !isValidCategory();
  }

  private boolean isValidCategory() {
    return validator.validate(skillCategory).isEmpty();
  }
}
