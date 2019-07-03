package com.excella.reactor.service.impl;

import com.excella.reactor.repositories.SkillRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class SkillServiceImplUnitTests {

    private SkillRepository skillRepository;
    private SkillServiceImpl skillService;

    @BeforeMethod
    private void beforeEach() {
        skillRepository = mock(SkillRepository.class);
        skillService = new SkillServiceImpl(skillRepository);
    }

    @Test
    public void testGetRepository() {
        Assert.assertEquals(skillRepository, skillService.getRepository());
    }
}
