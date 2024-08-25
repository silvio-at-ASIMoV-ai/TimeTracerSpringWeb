package com.asimov.timeTracerSpringWeb.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Optional;


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
) {
        public static Optional<User> fromString(String repr) {
                try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return Optional.of(objectMapper.readValue(repr, User.class));
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
