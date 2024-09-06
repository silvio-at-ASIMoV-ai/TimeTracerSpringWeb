package com.asimov.timeTracerSpringWeb.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Optional;


@Table("PROJECTS")
public record Project (
    @Id
    Integer id,
    @Column("PROJECTNAME")
    String ProjectName
) {
    public static Project empty() {
        return new Project(null, null);
    }

    public static Optional<Project> fromString(String repr) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.of(objectMapper.readValue(repr, Project.class));
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
