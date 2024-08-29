package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.repository.ListCrudRepository;

public interface Employees extends ListCrudRepository<Employee, Integer> { }
