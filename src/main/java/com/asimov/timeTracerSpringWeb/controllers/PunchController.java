package com.asimov.timeTracerSpringWeb.controllers;

import com.asimov.timeTracerSpringWeb.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Controller
public class PunchController {

    private final Times times;
    private final Projects projects;
    private final Employees employees;

    public PunchController(Times times, Projects projects, Employees employees) {
        this.times = times;
        this.projects = projects;
        this.employees = employees;
    }

    public String punchIn(User user, Model model) {
        Employee emp = employees.findById(user.EmployeeID()).get(); // already know employee exists
        model.addAttribute("userId", user.UserName());
        model.addAttribute("employeeId", String.format("%d", user.EmployeeID()));
        model.addAttribute("employeeName", emp.Name());
        model.addAttribute("employeeSurname", emp.Surname());
        model.addAttribute("projects", projects.findAll());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Optional<Time> optLastTime = times.findFirstByEmployeeIDOrderByPunchedTimeDesc(emp.ID());
        Optional<Project> optProject = projects.findById(optLastTime.get().ProjectID());
        String lastTime = optLastTime.isPresent() ?
                optLastTime.get().in() ?
                        String.format("Punched in at %s", dateFormat.format(optLastTime.get().PunchedTime())) :
                        String.format("Punched out at %s", dateFormat.format(optLastTime.get().PunchedTime()))
                : "";
        String lastProject = optProject.isPresent() ? optProject.get().ProjectName() : "";
        model.addAttribute("lastPunchedTime", lastTime);
        model.addAttribute("lastProject", lastProject);
        model.addAttribute("in", optLastTime.isPresent() ? optLastTime.get().in() : true);
        return "timePunch";
    }

    @PostMapping("/punch")
    public String punchFormSubmit(String userName, int employeeId, String dateTime,
                                  int chooseProject, String inout, boolean logout, Model model) {
        boolean in = inout.equals("IN");
        String day = dateTime.substring(6,8);
        String month = dateTime.substring(9,11);
        String year = dateTime.substring(12,16);
        String hour = dateTime.substring(18,20);
        String minute = dateTime.substring(21,23);
        String second = dateTime.substring(24,26);
        Timestamp time = Timestamp.valueOf(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        Time punchedTime = new Time(
                null,
                employeeId,
                chooseProject,
                time,
                in,
                userName,
                time,
                null,
                null
        );
        times.save(punchedTime);
        if(logout) {
            return "login";
        } else {
            Employee emp = employees.findById(employeeId).get();
            User newUser = new User(userName, null,
                    null, null, emp.ID());
            return punchIn(newUser, model);
        }

    }
}
