package com.asimov.timeTracerSpringWeb.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Optional;


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
        public Time() {
                this(null, null, null, null, null,
               null, null, null, null);
        }

        public static Optional<Time> fromString(String repr) {
                try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return Optional.of(objectMapper.readValue(repr, Time.class));
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
