package com.excella.reactor.controllers;

import static org.mockito.Mockito.mock;

import com.excella.reactor.service.SkillService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SkillControllerUnitTests {

  private SkillService skillService;
  private SkillController skillController;

  @BeforeMethod
  private void beforeEach() {
    skillService = mock(SkillService.class);
    skillController = new SkillController(skillService);
  }

  @Test
  public void testGetService() {
    Assert.assertEquals(skillService, skillController.getService());
  }
}
