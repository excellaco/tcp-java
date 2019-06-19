package com.excella.reactor.controllers;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerSmokeTest extends AbstractTestNGSpringContextTests {
  @Autowired private EmployeeController controller;
  @Test
  public void contextLoads() throws Exception {
    assertThat(controller).isNotNull();
  }
}
