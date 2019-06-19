package com.excella.reactor.controllers;

import com.excella.reactor.shared.SampleEntity;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan("com.excella.reactor")
@SpringBootTest
public class ControllerSmokeTest {

  @Autowired private CrudController<SampleEntity> controller;

  @Test
  public void contexLoads() throws Exception {
    assertThat(controller).isNotNull();
  }
}
