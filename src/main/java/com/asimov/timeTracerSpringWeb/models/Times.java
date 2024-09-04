package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface Times extends ListCrudRepository<Time, Integer> {

    @Query("""
            SELECT PunchedTime, `in`, ProjectID FROM Times
            WHERE EmployeeID = :empId
            ORDER BY PunchedTime DESC
            LIMIT 0, 1
            """)
    public Optional<Time> findFirstByEmployeeIDOrderByPunchedTimeDesc(int empId);

}
