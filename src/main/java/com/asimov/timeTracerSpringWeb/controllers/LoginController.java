package com.asimov.timeTracerSpringWeb.controllers;

import com.asimov.timeTracerSpringWeb.models.*;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.Optional;

import static com.asimov.timeTracerSpringWeb.utils.Password.checkPassword;
import static com.asimov.timeTracerSpringWeb.utils.Password.createHashedAndSaltedPassword;

@Controller
public class LoginController {

    private final Users users;
    private final Employees employees;
    private final Roles roles;
    @Autowired
    PunchController punchController;

    public LoginController(Users users, Employees employees, Roles roles) {
        this.users = users;
        this.employees = employees;
        this.roles = roles;
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/")
    public String loginFormSubmit(@Valid @ModelAttribute User user, Model model) {
        Optional<User> dbUser = users.findById(user.UserName());
        if(dbUser.isPresent()) {
            if(dbUser.get().Password() == null) {
                // reset password
                Timestamp now = new Timestamp(new java.util.Date().getTime());
                Timestamp reset = dbUser.get().ResetTime();

                long millis = now.getTime() - reset.getTime();
                int secs = (int) (millis / 1000);
                int minutes = (secs % 3600) / 60;

                if(minutes < 15) {
                    // ok to reset password
                    model.addAttribute("userId", dbUser.get().UserName());
                    return "changePwd";
                } else {
                    // tell user to have his password reset again
                    return "resetTimeout";
                }
            } else {
                if(checkPassword(user.Password(), dbUser.get().Password())) {
                    Role role = roles.findById(dbUser.get().RoleID()).get(); //RoleID must exist per db constraint
                    if(!role.IsAdmin()) {
                        // Role != Admin
                        if(dbUser.get().EmployeeID() != null) {
                            //Access granted to Employee role
                            Optional<Employee> optEmp = employees.findById(dbUser.get().EmployeeID());
                            if(optEmp.isPresent()) {
                                Employee emp = optEmp.get();
                                User newUser = new User(dbUser.get().UserName(), null,
                                        null, dbUser.get().RoleID(), emp.ID());
                                return punchController.punchIn(newUser, model);
                            } else {
                                return "changePwdError";  //this error page is temporary, read the TODO
                                //TODO This should never happen
                                //TODO ---------------------------------------------------------------------
                                //TODO It happens only when the admin doesn't insert the right EmployeeId
                                //TODO or when undoing an employee delete, the undone deleted employee
                                //TODO doesn't have the same id so the user points to the wrong employee
                                //TODO ---------------------------------------------------------------------
                                //TODO A user insert shoud always be accompanied by an employee insert
                                //TODO and an employee delete should always be accompanied by a user delete
                            }
                        } else {
                            //This should never happen
                            //Role != Admin and EmployeeId == null
                            return "accessDenied";
                        }
                    } else {
                        //Access granted to Admin role
                        return "forward:/admin";
                    }
                } else {
                    // Wrong Password
                    return "accessDenied";
                }
            }
        } else {
            //User not in database
            return "accessDenied";
        }
    }

    @PostMapping("changePwd")
    public String changePwd(String username, String pwd1, String pwd2) {
        if(pwd1.equals(pwd2)) {
            Optional<String> optNewPwd = createHashedAndSaltedPassword(pwd1);
            if(optNewPwd.isPresent()) {
                if(!users.updatePassword(username, optNewPwd.get())) {
                    return "changePwdError";
                }
            } else {
                return "changePwdError";
            }
        }
        return "login";
    }
}
