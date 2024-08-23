package com.asimov.timeTracerSpringWeb.controllers;

import com.asimov.timeTracerSpringWeb.models.Time;
import com.asimov.timeTracerSpringWeb.models.Times;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;

@Controller
public class PunchController {

    private final Times times;

    public PunchController(Times times) {
        this.times = times;
    }

    @PostMapping("/punch")
    public String punchFormSubmit(String userName, int employeeId, String dateTime, int chooseProject, String inout, Model model) {
        boolean in = inout.equals("IN");
        String year = dateTime.substring(6,10);
        String month = dateTime.substring(3,5);
        String day = dateTime.substring(0,2);
        String hour = dateTime.substring(12,14);
        String minute = dateTime.substring(15,17);
        String second = dateTime.substring(18,20);
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
        return "login";
    }
}
