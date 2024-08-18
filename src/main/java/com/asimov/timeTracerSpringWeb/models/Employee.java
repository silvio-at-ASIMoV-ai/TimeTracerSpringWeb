package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

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
}
