package com.asimov.timeTracerSpringWeb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Table("TIMES")
public record Time(
        @Id
        Integer ID,
        @Column("EMPLOYEEID")
        Integer EmployeeID,
        @Column("PROJECTID")
        Integer ProjectID,
        @Column("PUNCHEDTIME")
        Timestamp PunchedTime,
        Boolean in,
        @Column("INSERTUSER")
        String InsertUser,
        @Column("INSERTTIMESTAMP")
        Timestamp InsertTimestamp,
        @Column("MODIFYUSER")
        String ModifyUser,
        @Column("MODIFYTIMESTAMP")
        Timestamp ModifyTimestamp
) {
}
