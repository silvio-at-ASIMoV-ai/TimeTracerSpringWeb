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
        Log log = new Log(null,"Edit", newTime.toString(), oldTime.get().toString(),
                new Timestamp(System.currentTimeMillis()));
        times.save(newTime);
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/edit/employees")
    public String saveEmployee(@ModelAttribute Employee employee, Model model){
        Optional<Employee> oldEmployee = employees.findById(employee.ID());
        Employee newEmployee = new Employee(employee.ID(), employee.Name(), employee.Surname(),
                employee.BirthDate(), employee.BirthPlace(), employee.SocialSecurityNum(),
                employee.Residence());
        Log log = new Log(null,"Edit", newEmployee.toString(), oldEmployee.get().toString(),
                new Timestamp(System.currentTimeMillis()));
        employees.save(newEmployee);
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/edit/users")
    public String saveUser(@ModelAttribute User user, Model model){
        Optional<User> oldUser = users.findByIdWithoutPassword(user.UserName());
        User newUser = new User(user.UserName(), null, null,
                user.RoleID(), user.EmployeeID());
        Log log = new Log(null,"Edit", newUser.toString(), oldUser.get().toString(),
                new Timestamp(System.currentTimeMillis()));
        users.update(newUser);
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/edit/projects")
    public String saveProject(@ModelAttribute Project project, Model model){
        Optional<Project> oldProject = projects.findById(project.id());
        Project newProject = new Project(project.id(), project.ProjectName());
        Log log = new Log(null,"Edit", newProject.toString(), oldProject.get().toString(),
                new Timestamp(System.currentTimeMillis()));
        projects.save(newProject);
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/edit/roles")
    public String saveRole(@ModelAttribute Role role, Model model){
        Optional<Role> oldRole = roles.findById(role.ID());
        Role newRole = new Role(role.ID(), role.RoleDesc(), role.IsAdmin(), role.ProjectId());
        Log log = new Log(null,"Edit", newRole.toString(), oldRole.get().toString(),
                new Timestamp(System.currentTimeMillis()));
        roles.save(newRole);
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/times")
    public String insertTime(@ModelAttribute Time time, Model model){
        Time newTime = new Time(null, time.EmployeeID(), time.ProjectID(),
                time.PunchedTime(), time.in(), "Admin",
                new Timestamp(System.currentTimeMillis()),null, null);
        Time insertedTime = times.save(newTime);
        Log log = new Log(null,"Insert", insertedTime.toString(), "",
                new Timestamp(System.currentTimeMillis()));
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/employees")
    public String insertEmployee(@ModelAttribute Employee employee, Model model){
        Employee newEmployee = new Employee(null, employee.Name(), employee.Surname(),
                employee.BirthDate(), employee.BirthPlace(), employee.SocialSecurityNum(),
                employee.Residence());
        Employee insertedEmployee = employees.save(newEmployee);
        Log log = new Log(null,"Insert", insertedEmployee.toString(), "",
                new Timestamp(System.currentTimeMillis()));
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/users")
    public String insertUser(@ModelAttribute User user, Model model){
        User newUser = new User(user.UserName(), null, new Timestamp(System.currentTimeMillis()),
                user.RoleID(), user.EmployeeID());
        User insertedUser = new User(user.UserName(), null, null,
                user.RoleID(), user.EmployeeID());
        if(users.insert(newUser)){
            Log log = new Log(null,"Insert", insertedUser.toString(), "",
                    new Timestamp(System.currentTimeMillis()));
            logs.save(log);
        } else {
            Log log = new Log(null,"Insert", "Error Inserting User", "",
                    new Timestamp(System.currentTimeMillis()));
            logs.save(log);
        }
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/projects")
    public String insertProject(@ModelAttribute Project project, Model model){
        Project newProject = new Project(project.id(), project.ProjectName());
        Project insertedProject = projects.save(newProject);
        Log log = new Log(null,"Insert", insertedProject.toString(), "",
                new Timestamp(System.currentTimeMillis()));
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/insert/roles")
    public String insertRole(@ModelAttribute Role role, Model model){
        Role newRole = new Role(role.ID(), role.RoleDesc(), role.IsAdmin(), role.ProjectId());
        Role insertedRole = roles.save(newRole);
        Log log = new Log(null,"Insert", insertedRole.toString(), "",
                new Timestamp(System.currentTimeMillis()));
        logs.save(log);
        return goBackToAdminPage(model);
    }

    @PostMapping("/delete/delete")
    public String delete(String id, String table, Model model){
        Log log;
        switch (table) {
            case "times":
                Optional<Time> oldTime = times.findById(Integer.parseInt(id));
                log = new Log(null,"Delete", "", oldTime.get().toString(),
                        new Timestamp(System.currentTimeMillis()));
                times.deleteById(Integer.parseInt(id));
                logs.save(log);
                break;
            case "employees":
                Optional<Employee> oldEmployee = employees.findById(Integer.parseInt(id));
                log = new Log(null,"Delete", "", oldEmployee.get().toString(),
                        new Timestamp(System.currentTimeMillis()));
                employees.deleteById(Integer.parseInt(id));
                logs.save(log);
                break;
            case "users":
                Optional<User> oldUser = users.findByIdWithoutPassword(id);
                log = new Log(null,"Delete", "", oldUser.get().toString(),
                        new Timestamp(System.currentTimeMillis()));
                users.deleteById(id);
                logs.save(log);
                break;
            case "projects":
                Optional<Project> oldProject = projects.findById(Integer.parseInt(id));
                log = new Log(null,"Delete", "", oldProject.get().toString(),
                        new Timestamp(System.currentTimeMillis()));
                projects.deleteById(Integer.parseInt(id));
                logs.save(log);
                break;
            case "roles":
                Optional<Role> oldRole = roles.findById(Integer.parseInt(id));
                log = new Log(null,"Delete", "", oldRole.get().toString(),
                        new Timestamp(System.currentTimeMillis()));
                roles.deleteById(Integer.parseInt(id));
                logs.save(log);
                break;
            default:
        }
        return goBackToAdminPage(model);
    }

    @PostMapping("/resetPwd")
    public String resetPwd(String userName, Model model) {
        users.resetPwd(userName);
        Log log = new Log(null,"Reset Pwd", "", userName,
                new Timestamp(System.currentTimeMillis()));
        logs.save(log);
        return goBackToAdminPage(model);
    }

}
