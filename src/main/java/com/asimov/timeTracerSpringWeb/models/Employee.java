package com.asimov.timeTracerSpringWeb.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;
import java.util.Optional;


@Table("EMPLOYEES")
public record Employee(
        @Id
        Integer ID,
        String Name,
        String Surname,
        @Column("BIRTHDATE")
        Date BirthDate,
        @Column("BIRTHPLACE")
        String BirthPlace,
        @Column("SOCIALSECURITYNUM")
        String SocialSecurityNum,
        String Residence
) {
        public Employee() {
                this(null, null, null, null,
                   null, null, null);
        }

        public static Optional<Employee> fromString(String repr) {
                try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return Optional.of(objectMapper.readValue(repr, Employee.class));
                } catch (JsonProcessingException e){
                        return Optional.empty();
                }
        }

        @Override
        public String toString() {
                try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.writeValueAsString(this);
                } catch (JsonProcessingException e){
                        return "";
                }
        }

}
