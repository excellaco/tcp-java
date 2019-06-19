package com.excella.reactor.service.impl;

import com.excella.reactor.domain.Employee;
import com.excella.reactor.repositories.EmployeeRepository;
import com.excella.reactor.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private EmployeeRepository repository;

  @Autowired
  public EmployeeServiceImpl(EmployeeRepository repository) {
    this.repository = repository;
  }

  public JpaRepository<Employee, Long> getRepository() {
    return this.repository;
  }
}
