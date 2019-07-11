package com.excella.reactor.controllers;

import static org.mockito.Mockito.mock;

import com.excella.reactor.service.SkillCategoryService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SkillCategoryControllerUnitTests {

  private SkillCategoryService skillCategoryService;
  private SkillCategoryController skillCategoryController;

  @BeforeMethod
  private void beforeEach() {
    skillCategoryService = mock(SkillCategoryService.class);
    skillCategoryController = new SkillCategoryController(skillCategoryService);
  }

  @Test
  public void testGetService() {
    Assert.assertEquals(skillCategoryService, skillCategoryController.getService());
  }
}
