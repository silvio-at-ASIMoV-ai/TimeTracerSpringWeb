package com.asimov.timeTracerSpringWeb.controllers;

import com.asimov.timeTracerSpringWeb.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final Times times;
    private final Employees employees;
    private final Users users;
    private final Projects projects;
    private final Roles roles;
    private final Logs logs;

    public AdminController(Times times, Employees employees, Users users,
                           Projects projects, Roles roles, Logs logs) {
        this.times = times;
        this.employees = employees;
        this.users = users;
        this.projects = projects;
        this.roles = roles;
        this.logs = logs;
    }

    @GetMapping("/view/{table}")
    public String view(@PathVariable Map<String, String> pathVariables, @RequestParam("id") String id, Model model) {
        model.addAttribute("table", pathVariables.get("table"));
        switch (pathVariables.get("table")) {
            case "times": model.addAttribute("time", times.findById(Integer.parseInt(id)).get());
                break;
            case "employees": model.addAttribute("employee", employees.findById(Integer.parseInt(id)).get());
                break;
            case "users": model.addAttribute("user", users.findById(id).get());
                break;
            case "projects": model.addAttribute("project", projects.findById(Integer.parseInt(id)).get());
                break;
            case "roles": model.addAttribute("role", roles.findById(Integer.parseInt(id)).get());
                break;
            case "logs": model.addAttribute("log", logs.findById(Integer.parseInt(id)).get());
                break;
            default:
        }
        return "view";
    }

    @GetMapping("/edit/{table}")
    public String edit(@PathVariable Map<String, String> pathVariables, @RequestParam("id") String id, Model model){
        model.addAttribute("insert", false);
        model.addAttribute("table", pathVariables.get("table"));
        switch (pathVariables.get("table")) {
            case "times": model.addAttribute("time", times.findById(Integer.parseInt(id)).get());
                break;
            case "employees": model.addAttribute("employee", employees.findById(Integer.parseInt(id)).get());
                break;
            case "users": model.addAttribute("user", users.findById(id).get());
                break;
            case "projects": model.addAttribute("project", projects.findById(Integer.parseInt(id)).get());
                break;
            case "roles": model.addAttribute("role", roles.findById(Integer.parseInt(id)).get());
                break;
            default:
        }
        return "insertEdit";
    }

    @GetMapping("/delete/{table}")
    public String delete(@PathVariable Map<String, String> pathVariables, @RequestParam("id") String id, Model model){
        model.addAttribute("table", pathVariables.get("table"));
        model.addAttribute("id", id);
        return "delete";
    }

    @GetMapping("/insert/{table}")
    public String insert(@PathVariable Map<String, String> pathVariables, Model model){
        model.addAttribute("insert", true);
        model.addAttribute("table", pathVariables.get("table"));
        return "insertEdit";
    }

    private String goBackToAdminPage(Model model) {
        model.addAttribute("projects", projects.findAll());
        model.addAttribute("users", users.findAll());
        model.addAttribute("employees", employees.findAll());
        model.addAttribute("roles", roles.findAll());
        model.addAttribute("times", times.findAll());
        model.addAttribute("logs", logs.findAll());
        return "admin";
    }

    @PostMapping("/edit/times")
    public String saveTime(@ModelAttribute Time time, Model model){
        Optional<Time> oldTime = times.findById(time.ID());
        Time newTime = new Time(time.ID(), time.EmployeeID(), time.ProjectID(),
                time.PunchedTime(), time.in(), time.InsertUser(), time.InsertTimestamp(),
                "Admin", new Timestamp(System.currentTimeMillis()));
        Log log = new Log(null,"Edit Times", newTime.toString(), oldTime.get().toString(),
                new Timestamp(System.currentTimeMillis()));
        times.save(newTime);
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/edit/employees")
    public String saveEmployee(@ModelAttribute Employee employee, Model model){
        Employee employeeToSave = new Employee(employee.ID(), employee.Name(), employee.Surname(),
                employee.BirthDate(), employee.BirthPlace(), employee.SocialSecurityNum(),
                employee.Residence());
        employees.save(employeeToSave);
        return goBackToAdminPage(model);
    }

    @PostMapping("/edit/users")
    public String saveUser(@ModelAttribute User user, Model model){
        User userToSave = new User(user.UserName(), null, null,
                user.RoleID(), user.EmployeeID());
        users.update(userToSave);
        return goBackToAdminPage(model);
    }

    @PostMapping("/edit/projects")
    public String saveProject(@ModelAttribute Project project, Model model){
        Project projectToSave = new Project(project.id(), project.ProjectName());
        projects.save(projectToSave);
        return goBackToAdminPage(model);
    }

    @PostMapping("/edit/roles")
    public String saveRole(@ModelAttribute Role role, Model model){
        Role roleToSave = new Role(role.ID(), role.RoleDesc(), role.IsAdmin(), role.ProjectId());
        roles.save(roleToSave);
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/times")
    public String insertTime(@ModelAttribute Time time, Model model){
        Time timeToSave = new Time(null, time.EmployeeID(), time.ProjectID(),
                time.PunchedTime(), time.in(), "Admin",
                new Timestamp(System.currentTimeMillis()),null, null);
        times.save(timeToSave);
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/employees")
    public String insertEmployee(@ModelAttribute Employee employee, Model model){
        Employee employeeToSave = new Employee(null, employee.Name(), employee.Surname(),
                employee.BirthDate(), employee.BirthPlace(), employee.SocialSecurityNum(),
                employee.Residence());
        employees.save(employeeToSave);
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/users")
    public String insertUser(@ModelAttribute User user, Model model){
        User userToSave = new User(user.UserName(), null, new Timestamp(System.currentTimeMillis()),
                user.RoleID(), user.EmployeeID());
        users.insert(userToSave);
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/projects")
    public String insertProject(@ModelAttribute Project project, Model model){
        Project projectToSave = new Project(project.id(), project.ProjectName());
        projects.save(projectToSave);
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/roles")
    public String insertRole(@ModelAttribute Role role, Model model){
        Role roleToSave = new Role(role.ID(), role.RoleDesc(), role.IsAdmin(), role.ProjectId());
        roles.save(roleToSave);
        return goBackToAdminPage(model);
    }

    @PostMapping("/delete/delete")
    public String delete(String id, String table, Model model){
        switch (table) {
            case "times": times.deleteById(Integer.parseInt(id));
                break;
            case "employees": employees.deleteById(Integer.parseInt(id));
                break;
            case "users": users.deleteById(id);
                break;
            case "projects": projects.deleteById(Integer.parseInt(id));
                break;
            case "roles": roles.deleteById(Integer.parseInt(id));
                break;
            default:
        }
        return goBackToAdminPage(model);
    }

    @PostMapping("/resetPwd")
    public String resetPwd(String userName, Model model) {
        users.resetPwd(userName);
        return goBackToAdminPage(model);
    }

}
