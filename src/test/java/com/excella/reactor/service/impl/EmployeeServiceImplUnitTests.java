package com.excella.reactor.service.impl;
import com.excella.reactor.repositories.EmployeeRepository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;


import static org.mockito.Mockito.mock;


public class EmployeeServiceImplUnitTests {
    private EmployeeRepository employeeRepository;
    private EmployeeServiceImpl employeeService;

    @BeforeMethod
    private void beforeEach() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    public void testGetRepository() {
        Assert.assertEquals(employeeRepository, employeeService.getRepository());
    }
}
