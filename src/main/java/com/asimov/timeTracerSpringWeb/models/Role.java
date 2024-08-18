package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ROLES")
public record Role(
        @Id
        Integer ID,
        @Column("ROLEDESC")
        String RoleDesc,
        @Column("ISADMIN")
        Boolean IsAdmin,
        @Column("PROJECTID")
        Integer ProjectId
)
{}
