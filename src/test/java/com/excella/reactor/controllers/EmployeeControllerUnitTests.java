package com.excella.reactor.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.excella.reactor.domain.Employee;
import com.excella.reactor.service.EmployeeService;
import java.util.Arrays;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class EmployeeControllerUnitTests {

  EmployeeService mockService = mock(EmployeeService.class);
  Employee mockEmployee1 = new Employee();
  Employee mockEmployee2 = new Employee();
  Employee mockEmployee3 = new Employee();

  EmployeeController employeeController = new EmployeeController(mockService);

  @Test
  public void contextLoads() {}

  @Test
  public void getAll_can_return_flux_of_multiple_employees() {
    when(mockService.all()).thenReturn(Flux.just(mockEmployee1, mockEmployee2, mockEmployee3));

    StepVerifier.create(employeeController.getAll())
        .expectNextSequence(Arrays.asList(mockEmployee1, mockEmployee2, mockEmployee3))
        .expectComplete()
        .verify();
  }

  @Test
  public void getAll_can_return_flux_of_no_employees() {
    when(mockService.all()).thenReturn(Flux.empty());

    StepVerifier.create(employeeController.getAll()).expectComplete().verify();
  }
}
