package com.asimov.timeTracerSpringWeb.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table("Users")
public record User(
        @Id
        @NotBlank
        String UserName,
        @NotBlank
        String Password,
        Timestamp ResetTime,
        Integer RoleID,
        Integer EmployeeID
) {}
