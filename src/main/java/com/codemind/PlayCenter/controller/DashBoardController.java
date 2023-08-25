package com.codemind.PlayCenter.controller;

import com.codemind.PlayCenter.dao.StudentAttendanceDAO;
import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.entity.Role;
import com.codemind.PlayCenter.entity.Student;
import com.codemind.PlayCenter.entity.StudentAttendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController {

    @Autowired
    AuthController authController;

    @Autowired
    StudentDAO studentDAO;

    @Autowired
    StudentAttendanceDAO studentAttendanceDAO;

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
    private String getAllStudentForAttendance(Model model){

        List<Student> studentList=studentDAO.findAll();

        List<Student> students=new ArrayList<>();

        for(Student tempStudent:studentList)
        {
            List<Role> stuRole=tempStudent.getRoles();
            for(Role role:stuRole){
                if(role.getName().equals("ROLE_STUDENT")){
                    students.add(tempStudent);
                    break;
                }
            }
        }

        LocalDate date = LocalDate.now();
        model.addAttribute("allStudents",students);
        model.addAttribute("todayDate",date);
        return "/homeDirectory/attendance-page";
    }

    @PostMapping("/fill-info")
    private String showAttendedStudent(@RequestParam("selectedItems") List<String> selectedItems){

        LocalDate date = LocalDate.now();

        for(String selectString:selectedItems)
        {
            Student student=studentDAO.findByUserName(selectString);
            StudentAttendance studentAttendance=new StudentAttendance();
            studentAttendance.setStudentId(student.getId());
            studentAttendance.setStudentUsername(student.getUserName());
            studentAttendance.setDate(date);
            studentAttendanceDAO.save(studentAttendance);
        }



        return "redirect:/dashboard/dash-board";
    }



}
