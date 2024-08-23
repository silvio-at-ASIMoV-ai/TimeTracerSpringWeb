package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.repository.ListCrudRepository;

public interface Logs extends ListCrudRepository<Log, Integer> {
}
