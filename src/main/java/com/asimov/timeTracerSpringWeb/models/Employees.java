package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface Employees extends ListCrudRepository<Employee, Integer> {

    //List<Employee> findAllBirthDateAfter(Date date);

    //List<Employee> getByBirthPlaceLikeOrBirthDateBetween(String place, Date date1, Date date2);

//    @Query("""
//            SELECT * FROM EMPLOYEES
//            WHERE RESIDENCE LIKE '%:res%'
//            """)
//    List<Employee> ListByResidence(@Param("res") String residence);
}
