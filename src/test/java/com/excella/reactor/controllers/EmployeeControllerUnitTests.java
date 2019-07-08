package com.excella.reactor.controllers;

import static org.mockito.Mockito.mock;

import com.excella.reactor.service.EmployeeService;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EmployeeControllerUnitTests {


  private EmployeeService employeeService;
  private EmployeeController employeeController;


  @BeforeMethod
  private void beforeEach() {
    employeeService = mock(EmployeeService.class);
    employeeController = new EmployeeController(employeeService);
  }

  @Test
  public void testGetService() {
    Assert.assertEquals(employeeService, employeeController.getService());
  }

}
