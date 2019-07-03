package com.excella.reactor.controllers;

import com.excella.reactor.domain.SkillCategory;
import com.excella.reactor.service.SkillCategoryService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SkillCategoryControllerUnitTests {

    private SkillCategoryService mockService;
    private SkillCategory cat1;
    private SkillCategory cat2;
    private SkillCategory cat3;
    private SkillCategoryController skillCategoryController;

    @BeforeMethod
    private void beforeEach() {
        mockService = mock(SkillCategoryService.class);
        cat1 = new SkillCategory();
        cat2 = new SkillCategory();
        cat3 = new SkillCategory();

        skillCategoryController = new SkillCategoryController(mockService);

    }

    @Test
    public void contextLoads() {}

    @Test
    public void getAll_can_return_flux_of_multiple_skill_categories() {
        when(mockService.all(null)).thenReturn(Flux.just(cat1, cat2, cat3));

        StepVerifier.create(skillCategoryController.getAll(null))
                .expectNextSequence(Arrays.asList(cat1, cat2, cat3))
                .expectComplete()
                .verify();
    }

    @Test
    public void getAll_can_return_flux_of_no_skill_categories() {
        when(mockService.all(null)).thenReturn(Flux.empty());

        StepVerifier.create(skillCategoryController.getAll(null)).expectComplete().verify();
    }
}
