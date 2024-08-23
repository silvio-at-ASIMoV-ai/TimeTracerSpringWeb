package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.repository.ListCrudRepository;

public interface Times extends ListCrudRepository<Time, Integer> {
}
