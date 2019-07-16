package com.excella.reactor.service.impl;

import static org.mockito.Mockito.mock;

import com.excella.reactor.repositories.SkillCategoryRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SkillCategoryServiceImplUnitTests {

  private SkillCategoryRepository skillCategoryRepository;
  private SkillCategoryServiceImpl skillCategoryService;

  @BeforeMethod
  private void beforeEach() {
    skillCategoryRepository = mock(SkillCategoryRepository.class);
    skillCategoryService = new SkillCategoryServiceImpl(skillCategoryRepository);
  }

  @Test
  public void testGetRepository() {
    Assert.assertEquals(skillCategoryRepository, skillCategoryService.getRepository());
  }
}
