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
        Optional<Employee> optEmp = employees.findById(user.EmployeeID());
        if(optEmp.isPresent()) {
            Employee emp = optEmp.get();
            model.addAttribute("userId", user.UserName());
            model.addAttribute("employeeId", String.format("%d", user.EmployeeID()));
            model.addAttribute("employeeName", emp.Name());
            model.addAttribute("employeeSurname", emp.Surname());
            model.addAttribute("projects", projects.findAll());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Optional<Time> optLastTime = times.findFirstByEmployeeIDOrderByPunchedTimeDesc(emp.ID());
            Optional<Project> optProject = optLastTime.flatMap(time -> projects.findById(time.ProjectID()));
            String lastTime = optLastTime.map(
                time -> (
                    time.in() ?
                        String.format("Punched in at %s", dateFormat.format(optLastTime.get().PunchedTime())) :
                        String.format("Punched out at %s", dateFormat.format(optLastTime.get().PunchedTime()))
                )
            ).orElse("");
            String lastProject = optProject.isPresent() ? optProject.get().ProjectName() : "";
            model.addAttribute("lastPunchedTime", lastTime);
            model.addAttribute("lastProject", lastProject);
            model.addAttribute("in", optLastTime.map(Time::in).orElse(false));
            return "timePunch";
        } else {
            return "error";
        }
    }

    @PostMapping("/punch")
    public String punchFormSubmit(String userName, int employeeId, String dateTime,
                                  int chooseProject, String inout, boolean logout, Model model) {
        boolean in = inout.equals("IN");
        String day = dateTime.substring(6,8);
        String month = dateTime.substring(9,11);
        String year = dateTime.substring(12,16);
        String hour = dateTime.substring(17,19);
        String minute = dateTime.substring(20,22);
        String second = dateTime.substring(23,25);

        Timestamp pTime = Timestamp.valueOf(year + "-" + month + "-" + day + " "
                + hour + ":" + minute + ":00");
        Timestamp iTime = Timestamp.valueOf(year + "-" + month + "-" + day + " "
                + hour + ":" + minute + ":" + second);
        Time punchedTime = new Time(null, employeeId, chooseProject, pTime,
                in, userName, iTime,null,null);
        times.save(punchedTime);
        if(logout) {
            return "login";
        } else {
            return punchIn(new User(userName, null, null, null, employeeId), model);
        }
    }
}
