package com.excella.reactor.validation;

import com.excella.reactor.domain.Skill;
import com.excella.reactor.domain.SkillCategory;
import javax.validation.Validation;
import javax.validation.Validator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class SkillValidationTests {
  private Skill skill;
  private Validator validator;

  @BeforeTest
  public void runBeforeTest() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @BeforeMethod
  public void runBeforeEachMethod() {
    skill = new Skill();
    var category = new SkillCategory();

    skill.setName("Skill");
    skill.setCategory(category);
  }

  @Test
  public void skill_is_valid_when_all_fields_valid() {
    assert isValidSkill();
  }

  @Test
  public void skill_is_invalid_when_name_empty() {
    skill.setName("");
    assert !isValidSkill();
  }

  @Test
  public void skill_is_invalid_when_name_null() {
    skill.setName(null);
    assert !isValidSkill();
  }

  @Test
  public void skill_is_invalid_when_category_null() {
    skill.setCategory(null);
  }

  private boolean isValidSkill() {
    return validator.validate(skill).isEmpty();
  }
}
