package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table("LOGS")
public record Log(
        @Id
        Integer ID,
        String Operation,
        @Column("NEWVALUES")
        String NewValues,
        @Column("OLDVALUES")
        String OldValues,
        Timestamp timestamp,
        @Column("UNDOID")
        Integer undoId
) {
}
