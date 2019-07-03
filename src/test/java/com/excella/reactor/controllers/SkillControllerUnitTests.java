package com.excella.reactor.controllers;

import com.excella.reactor.domain.Skill;
import com.excella.reactor.service.SkillService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SkillControllerUnitTests {

    private SkillService mockService;
    private Skill skill1;
    private Skill skill2;
    private Skill skill3;
    private SkillController skillController;

    @BeforeMethod
    private void beforeEach() {
        mockService = mock(SkillService.class);
        skill1 = new Skill();
        skill2 = new Skill();
        skill3 = new Skill();

        skillController = new SkillController(mockService);

    }

    @Test
    public void contextLoads() {}

    @Test
    public void getAll_can_return_flux_of_multiple_skills() {
        when(mockService.all(null)).thenReturn(Flux.just(skill1, skill2, skill3));

        StepVerifier.create(skillController.getAll(null))
                .expectNextSequence(Arrays.asList(skill1, skill2, skill3))
                .expectComplete()
                .verify();
    }

    @Test
    public void getAll_can_return_flux_of_no_skills() {
        when(mockService.all(null)).thenReturn(Flux.empty());

        StepVerifier.create(skillController.getAll(null)).expectComplete().verify();
    }
}
