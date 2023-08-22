package com.codemind.PlayCenter.controller;

import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController {

    @Autowired
    AuthController authController;

    @Autowired
    StudentDAO studentDAO;

    @GetMapping("/dash-board")
    private String getDashboardPage(Model model){

        String authenticateUserName=authController.getAuthenticateUserName();
        Student student=studentDAO.findByUserName(authenticateUserName);

        model.addAttribute("userdetails",student.getFirstName()+" "+student.getLastName());


        return "/homeDirectory/login/dash-board";
    }

    @GetMapping("/student-info")
    private String getStudentInfo(Model model){
        String authenticateUserName=authController.getAuthenticateUserName();
        Student student=studentDAO.findByUserName(authenticateUserName);

        model.addAttribute("username",student.getFirstName());

        return "/homeDirectory/student-dashboard";
    }

    @GetMapping("/fill-attendance")
    private String getAllStudentForAttendance(){
        return "/homeDirectory/attendance-page";
    }

}
