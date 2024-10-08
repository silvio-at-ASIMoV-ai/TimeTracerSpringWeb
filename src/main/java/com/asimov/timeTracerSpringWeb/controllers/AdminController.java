package com.asimov.timeTracerSpringWeb.controllers;

import com.asimov.timeTracerSpringWeb.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.asimov.timeTracerSpringWeb.utils.Utility.str2Timestamp;


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

    @PostMapping("")
    public String adminPage(Model model) {
        return goToAdminPage(model, "Time");
    }

    @GetMapping("/view/{table}")
    public String view(@PathVariable Map<String, String> pathVariables, @RequestParam("id") String id, Model model) {
        model.addAttribute("table", pathVariables.get("table"));
        switch (pathVariables.get("table")) {
            case "times": model.addAttribute("time", times.findById(Integer.parseInt(id))
                    .orElse(Time.empty()));
                break;
            case "employees": model.addAttribute("employee", employees.findById(Integer.parseInt(id))
                    .orElse(Employee.empty()));
                break;
            case "users": model.addAttribute("user", users.findById(id)
                    .orElse(User.empty()));
                break;
            case "projects": model.addAttribute("project", projects.findById(Integer.parseInt(id))
                    .orElse(Project.empty()));
                break;
            case "roles": model.addAttribute("role", roles.findById(Integer.parseInt(id))
                    .orElse(Role.empty()));
                break;
            case "logs": model.addAttribute("log", logs.findById(Integer.parseInt(id))
                    .orElse(Log.empty()));
                break;
            default:
        }
        return "view";
    }

    @GetMapping("/edit/{table}")
    public String edit(@PathVariable Map<String, String> pathVariables, @RequestParam("id") String id, Model model) {
        model.addAttribute("insert", false);
        model.addAttribute("table", pathVariables.get("table"));
        switch (pathVariables.get("table")) {
            case "times": model.addAttribute("time", times.findById(Integer.parseInt(id))
                    .orElse(Time.empty()));
                break;
            case "employees": model.addAttribute("employee", employees.findById(Integer.parseInt(id))
                    .orElse(Employee.empty()));
                break;
            case "users": model.addAttribute("user", users.findById(id)
                    .orElse(User.empty()));
                break;
            case "projects": model.addAttribute("project", projects.findById(Integer.parseInt(id))
                    .orElse(Project.empty()));
                break;
            case "roles": model.addAttribute("role", roles.findById(Integer.parseInt(id))
                    .orElse(Role.empty()));
                break;
            default:
        }
        return "insertEdit";
    }

    @GetMapping("/delete/{table}")
    public String delete(@PathVariable Map<String, String> pathVariables, @RequestParam("id") String id, Model model) {
        model.addAttribute("table", pathVariables.get("table"));
        model.addAttribute("id", id);
        return "delete";
    }

    @GetMapping("/insert/{table}")
    public String insert(@PathVariable Map<String, String> pathVariables, Model model) {
        model.addAttribute("insert", true);
        model.addAttribute("table", pathVariables.get("table"));
        return "insertEdit";
    }

    private String goToAdminPage(Model model, String startTable) {
        return goToAdminPage(model, startTable, null);
    }

    private String goToAdminPage(Model model, String startTable, List<Log> logList) {
        model.addAttribute("projects", projects.findAll());
        model.addAttribute("users", users.findAll());
        model.addAttribute("employees", employees.findAll());
        model.addAttribute("roles", roles.findAll());
        model.addAttribute("times", times.findAll());
        int limit = 15;
        if(logList == null) model.addAttribute("logs", logs.findAllLimitedReversed(limit));
        else model.addAttribute("logs", logList);
        model.addAttribute("startTable", startTable);
        model.addAttribute("limit", limit);
        return "admin";
    }

    @SuppressWarnings("unused")
    @GetMapping("{from}/back/{table}")
    String backToAdmin(Model model, @PathVariable String table, @PathVariable String from) {
        return goToAdminPage(model, table);
    }

    @PostMapping("/edit/times")
    public String saveTime(String ID, String EmployeeID, String ProjectID, String PunchedTime,
                           String in, String InsertUser, String InsertTimestamp, Model model) {
        int id = Integer.parseInt(ID);
        int empId = Integer.parseInt(EmployeeID);
        int projId = Integer.parseInt(ProjectID);
        Timestamp mt = new Timestamp(System.currentTimeMillis());

        Optional<Time> oldTime = times.findById(id);
        Time newTime = new Time(id, empId, projId, str2Timestamp(PunchedTime), in != null,
                InsertUser, str2Timestamp(InsertTimestamp, true), "Admin", mt);

        Log log = new Log(null,"Edit", "Time", newTime.toString(),
                oldTime.orElse(Time.empty()).toString(),null, mt);
        times.save(newTime);
        logs.save(log);
        return goToAdminPage(model, "Time");
    }

    @PostMapping("/edit/employees")
    public String saveEmployee(@ModelAttribute Employee employee, Model model) {
        Optional<Employee> oldEmployee = employees.findById(employee.ID());
        Employee newEmployee = new Employee(employee.ID(), employee.Name(), employee.Surname(),
                employee.BirthDate(), employee.BirthPlace(), employee.SocialSecurityNum(),
                employee.Residence());
        Log log = new Log(null,"Edit", "Employee", newEmployee.toString(),
                oldEmployee.orElse(Employee.empty()).toString(), null, new Timestamp(System.currentTimeMillis()));
        employees.save(newEmployee);
        logs.save(log);
        return goToAdminPage(model, "Employee");
    }

    @PostMapping("/edit/users")
    public String saveUser(@ModelAttribute User user, Model model) {
        Optional<User> oldUser = users.findByIdWithoutPassword(user.UserName());
        User newUser = new User(user.UserName(), null, null,
                user.RoleID(), user.EmployeeID());
        Log log = new Log(null,"Edit", "User", newUser.toString(),
                oldUser.orElse(User.empty()).toString(), null, new Timestamp(System.currentTimeMillis()));
        users.update(newUser);
        logs.save(log);
        return goToAdminPage(model, "User");
    }

    @PostMapping("/edit/projects")
    public String saveProject(@ModelAttribute Project project, Model model) {
        Optional<Project> oldProject = projects.findById(project.id());
        Project newProject = new Project(project.id(), project.ProjectName());
        Log log = new Log(null,"Edit", "Project", newProject.toString(),
                oldProject.orElse(Project.empty()).toString(), null, new Timestamp(System.currentTimeMillis()));
        projects.save(newProject);
        logs.save(log);
        return goToAdminPage(model, "Project");
    }

    @PostMapping("/edit/roles")
    public String saveRole(@ModelAttribute Role role, Model model) {
        Optional<Role> oldRole = roles.findById(role.ID());
        Role newRole = new Role(role.ID(), role.RoleDesc(), role.IsAdmin(), role.ProjectId());
        Log log = new Log(null,"Edit", "Role", newRole.toString(),
                oldRole.orElse(Role.empty()).toString(), null, new Timestamp(System.currentTimeMillis()));
        roles.save(newRole);
        logs.save(log);
        return goToAdminPage(model, "Role");
    }

    @PostMapping("/insert/times")
    public String insertTime(String EmployeeID, String ProjectID,
                             String PunchedTime, String in, Model model) {

        int empId = Integer.parseInt(EmployeeID);
        int projId = Integer.parseInt(ProjectID);
        Timestamp mt = new Timestamp(System.currentTimeMillis());

        Time newTime = new Time(null, empId, projId, str2Timestamp(PunchedTime),
                in != null, "Admin", mt,null, null);

        Time insertedTime = times.save(newTime);
        Log log = new Log(null,"Insert", "Time",
                insertedTime.toString(), "", null, mt);
        logs.save(log);
        return goToAdminPage(model, "Time");
    }

    @PostMapping("/insert/employees")
    public String insertEmployee(@ModelAttribute Employee employee, Model model) {
        Employee newEmployee = new Employee(null, employee.Name(), employee.Surname(),
                employee.BirthDate(), employee.BirthPlace(), employee.SocialSecurityNum(),
                employee.Residence());
        Employee insertedEmployee = employees.save(newEmployee);
        Log log = new Log(null,"Insert", "Employee", insertedEmployee.toString(),
                "", null, new Timestamp(System.currentTimeMillis()));
        logs.save(log);
        return goToAdminPage(model, "Employee");
    }

    @PostMapping("/insert/users")
    public String insertUser(@ModelAttribute User user, Model model) {
        User newUser = new User(user.UserName(), null, new Timestamp(System.currentTimeMillis()),
                user.RoleID(), user.EmployeeID());
        User insertedUser = new User(user.UserName(), null, null,
                user.RoleID(), user.EmployeeID());
        if(users.insert(newUser)){
            Log log = new Log(null,"Insert", "User", insertedUser.toString(),
                    "", null, new Timestamp(System.currentTimeMillis()));
            logs.save(log);
        }
        return goToAdminPage(model, "User");
    }

    @PostMapping("/insert/projects")
    public String insertProject(@ModelAttribute Project project, Model model) {
        Project newProject = new Project(project.id(), project.ProjectName());
        Project insertedProject = projects.save(newProject);
        Log log = new Log(null,"Insert", "Project", insertedProject.toString(),
                "", null, new Timestamp(System.currentTimeMillis()));
        logs.save(log);
        return goToAdminPage(model, "Project");
    }

    @PostMapping("/insert/roles")
    public String insertRole(@ModelAttribute Role role, Model model) {
        Role newRole = new Role(role.ID(), role.RoleDesc(), role.IsAdmin(), role.ProjectId());
        Role insertedRole = roles.save(newRole);
        Log log = new Log(null,"Insert", "Role", insertedRole.toString(),
                "", null, new Timestamp(System.currentTimeMillis()));
        logs.save(log);
        return goToAdminPage(model, "Role");
    }

    @PostMapping("/delete/delete")
    public String delete(String id, String table, Model model) {
        Log log;
        switch (table) {
            case "times":
                Optional<Time> oldTime = times.findById(Integer.parseInt(id));
                log = new Log(null,"Delete", "Time", "",
                        oldTime.orElse(Time.empty()).toString(), null, new Timestamp(System.currentTimeMillis()));
                times.deleteById(Integer.parseInt(id));
                logs.save(log);
                break;
            case "employees":
                Optional<Employee> oldEmployee = employees.findById(Integer.parseInt(id));
                log = new Log(null,"Delete", "Employee", "",
                        oldEmployee.orElse(Employee.empty()).toString(), null, new Timestamp(System.currentTimeMillis()));
                employees.deleteById(Integer.parseInt(id));
                logs.save(log);
                break;
            case "users":
                Optional<User> oldUser = users.findByIdWithoutPassword(id);
                log = new Log(null,"Delete", "User", "",
                        oldUser.orElse(User.empty()).toString(), null, new Timestamp(System.currentTimeMillis()));
                users.deleteById(id);
                logs.save(log);
                break;
            case "projects":
                Optional<Project> oldProject = projects.findById(Integer.parseInt(id));
                log = new Log(null,"Delete", "Project", "",
                        oldProject.orElse(Project.empty()).toString(), null, new Timestamp(System.currentTimeMillis()));
                projects.deleteById(Integer.parseInt(id));
                logs.save(log);
                break;
            case "roles":
                Optional<Role> oldRole = roles.findById(Integer.parseInt(id));
                log = new Log(null,"Delete", "Role", "",
                        oldRole.orElse(Role.empty()).toString(), null, new Timestamp(System.currentTimeMillis()));
                roles.deleteById(Integer.parseInt(id));
                logs.save(log);
                break;
            default:
        }
        return goToAdminPage(model,
                table.substring(0, 1).toUpperCase() + table.substring(1, table.length() - 1));
    }

    @PostMapping("/resetPwd")
    public String resetPwd(String userName, Model model) {
        users.resetPwd(userName);
        Log log = new Log(null,"Reset Pwd", "User", "",
                userName, null, new Timestamp(System.currentTimeMillis()));
        logs.save(log);
        return goToAdminPage(model, "User");
    }

    private void undoInsert(Log log) {
        String str = log.NewValues();
        String[] pieces = str.substring(0, str.length()-1).split(" -> \\{");
        String table = pieces[0];
        String[] fields = pieces[1].split(",");
        String id = fields[0].split(":")[1];
        Log newLog;
        switch (table) {
            case "Time":
                Optional<Time> oldTime = times.findById(Integer.parseInt(id));
                newLog = new Log(null,"Delete", "Time", "",
                        oldTime.orElse(Time.empty()).toString(), log.ID(), new Timestamp(System.currentTimeMillis()));
                times.deleteById(Integer.parseInt(id));
                logs.save(newLog);
                break;
            case "Employee":
                Optional<Employee> oldEmployee = employees.findById(Integer.parseInt(id));
                newLog = new Log(null,"Delete", "Employee", "",
                        oldEmployee.orElse(Employee.empty()).toString(), log.ID(), new Timestamp(System.currentTimeMillis()));
                employees.deleteById(Integer.parseInt(id));
                logs.save(newLog);
                break;
            case "Project":
                Optional<Project> oldProject = projects.findById(Integer.parseInt(id));
                newLog = new Log(null,"Delete", "Project", "",
                        oldProject.orElse(Project.empty()).toString(), log.ID(), new Timestamp(System.currentTimeMillis()));
                projects.deleteById(Integer.parseInt(id));
                logs.save(newLog);
                break;
            case "User":
                Optional<User> oldUser = users.findByIdWithoutPassword(id);
                newLog = new Log(null,"Delete", "User", "",
                        oldUser.orElse(User.empty()).toString(), log.ID(), new Timestamp(System.currentTimeMillis()));
                users.deleteById(id);
                logs.save(newLog);
                break;
            case "Role":
                Optional<Role> oldRole = roles.findById(Integer.parseInt(id));
                newLog = new Log(null,"Delete", "Role", "",
                        oldRole.orElse(Role.empty()).toString(), log.ID(), new Timestamp(System.currentTimeMillis()));
                roles.deleteById(Integer.parseInt(id));
                logs.save(newLog);
                break;
            default:
        }
    }

    private void undoEdit(Log log) {
        Log newLog;
        switch (log.Table()) {
            case "Time":
                //TODO should I change the ModifyUser and ModifyTimestamp?
                //TODO or should I remove those fields from Time table since
                //TODO the ModifyUser is always Admin and they only record the last modification
                //TODO while all the modifications with their timestamps are already
                //TODO recorded in the logs table?
                Optional<Time> optOldTime = Time.fromString(log.OldValues());
                if(optOldTime.isPresent()) {
                    times.save(optOldTime.get());
                    newLog = new Log(null,"Edit", "Time", log.OldValues(),
                            log.NewValues(), log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            case "Employee":
                Optional<Employee> optOldEmployee = Employee.fromString(log.OldValues());
                if(optOldEmployee.isPresent()) {
                    employees.save(optOldEmployee.get());
                    newLog = new Log(null,"Edit", "Employee", log.OldValues(),
                            log.NewValues(), log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            case "Project":
                Optional<Project> optOldProject = Project.fromString(log.OldValues());
                if(optOldProject.isPresent()) {
                    projects.save(optOldProject.get());
                    newLog = new Log(null,"Edit", "Project", log.OldValues(),
                            log.NewValues(), log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            case "User":
                Optional<User> optOldUser = User.fromString(log.OldValues());
                if(optOldUser.isPresent()) {
                    users.update(optOldUser.get());
                    newLog = new Log(null,"Edit", "User", log.OldValues(),
                            log.NewValues(), log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            case "Role":
                Optional<Role> optOldRole = Role.fromString(log.OldValues());
                if(optOldRole.isPresent()) {
                    roles.save(optOldRole.get());
                    newLog = new Log(null,"Edit", "Role", log.OldValues(),
                            log.NewValues(), log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            default:
        }
    }

    private String removeId(String repr) {
        int start = repr.indexOf(":");
        int end = repr.indexOf(",", start);
        return repr.substring(0, start) + ":null" + repr.substring(end);
    }

    private void undoDelete(Log log) {
        Log newLog;
        switch (log.Table()) {
            case "Time":
                Optional<Time> optOldTime = Time.fromString(removeId(log.OldValues()));
                if(optOldTime.isPresent()) {
                    Time savedTime = times.save(optOldTime.get());
                    newLog = new Log(null,"Insert", "Time", savedTime.toString(),
                            "", log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            case "Employee":
                Optional<Employee> optOldEmployee = Employee.fromString(removeId(log.OldValues()));
                if(optOldEmployee.isPresent()) {
                    Employee savedEmployee = employees.save(optOldEmployee.get());
                    newLog = new Log(null,"Insert", "Employee", savedEmployee.toString(),
                            "", log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            case "Project":
                Optional<Project> optOldProject = Project.fromString(removeId(log.OldValues()));
                if(optOldProject.isPresent()) {
                    Project savedProject = projects.save(optOldProject.get());
                    newLog = new Log(null,"Insert", "Project", savedProject.toString(),
                            "", log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            case "User":
                Optional<User> optOldUser = User.fromString(log.OldValues());
                if(optOldUser.isPresent()) {
                    User newUser = new User(optOldUser.get().UserName(), null,
                            new Timestamp(System.currentTimeMillis()),
                            optOldUser.get().RoleID(), optOldUser.get().EmployeeID());
                    users.insert(newUser);
                    newLog = new Log(null,"Insert", "User", optOldUser.get().toString(),
                            "", log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            case "Role":
                Optional<Role> optOldRole = Role.fromString(removeId(log.OldValues()));
                if(optOldRole.isPresent()) {
                    Role savedRole = roles.save(optOldRole.get());
                    newLog = new Log(null,"Insert", "Role", savedRole.toString(),
                            "", log.ID(), new Timestamp(System.currentTimeMillis()));
                    logs.save(newLog);
                }
                break;
            default:
        }
    }

    @GetMapping("/undo/logs")
    public String undoConfirm(int id, Model model) {
        model.addAttribute("id", id);
        return "undo";
    }

    @PostMapping("/undo")
    public String undo(int id, Model model) {
        Optional<Log> log = logs.findById(id);
        if(log.isPresent()) {
            switch (log.get().Operation()) {
                case "Edit": undoEdit(log.get());
                    break;
                case "Insert": undoInsert(log.get());
                    break;
                case "Delete": undoDelete(log.get());
                    break;
                default:
            }
        }
        return goToAdminPage(model, "Log");
    }

    @PostMapping("search/logs")
    public String logSearch(Model model, String operation, String table, LocalDateTime date_from,
                            LocalDateTime date_to, String contains, int limit) {

        List<Log> logList;
        List<String> sl = new ArrayList<>();
        String where = "";

        if(!operation.equalsIgnoreCase("all")) sl.add("Operation = '" + operation + "'");
        if(!table.equalsIgnoreCase("all")) sl.add("`Table` = '" + table + "'");
        if(date_from != null && date_to != null) sl.
            add("timestamp between '" + date_from + "' and '" + date_to + "'");
        if(!contains.isBlank()) sl.
            add("(NewValues like '%" + contains + "%' or OldValues like '%" + contains + "%')");

        if(!sl.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("WHERE ");
            for(int i=0; i<sl.size() - 1; i++) {
                sb.append(sl.get(i));
                sb.append(" AND ");
            }
            sb.append(sl.get(sl.size() - 1));
            where = sb.toString();
        }

        if(limit > 0) {
            if(!where.isBlank()) {
                logList = logs.findAllWhereReversed(where).stream().limit(limit).toList();
            } else {
                logList = logs.findAllLimitedReversed(limit);
            }
        } else {
            if(!where.isBlank()) {
                logList = logs.findAllWhereReversed(where);
            } else {
                logList = logs.findAllReversed();
            }
        }

        return goToAdminPage(model, "Log", logList);
    }
}
