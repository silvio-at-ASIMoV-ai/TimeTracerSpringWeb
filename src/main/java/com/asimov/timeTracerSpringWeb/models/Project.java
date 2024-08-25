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

//    public static void main(String[] args) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Project prj1 = new Project(1, "Learning");
////        try {
////            String json = objectMapper.writeValueAsString(prj1);
////            System.out.println(json);
////            Project prj2 = objectMapper.readValue(json, Project.class);
////            System.out.println(prj2);
//            System.out.println(prj1);
//            Optional<Project> prj2 = Project.fromString(prj1.toString());
//            if(prj2.isPresent()) System.out.println(prj2.get());
////        } catch (JsonProcessingException e) {
////
////        }
//
//    }

}
