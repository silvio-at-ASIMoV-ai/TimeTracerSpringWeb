package com.asimov.timeTracerSpringWeb.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Optional;


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
{
        public static Role empty() {
                return new Role(null, null, null, null);
        }

        public static Optional<Role> fromString(String repr) {
                try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return Optional.of(objectMapper.readValue(repr, Role.class));
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
