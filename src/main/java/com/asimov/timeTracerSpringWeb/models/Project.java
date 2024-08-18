package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("PROJECTS")
public record Project(
        @Id
        Integer id,
        @Column("PROJECTNAME")
        String ProjectName
) { }
