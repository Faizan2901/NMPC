package com.codemind.PlayCenter.controller;

import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.entity.Student;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    AuthController authController;

    @Autowired
    StudentDAO studentDAO;

    @GetMapping("/student-info")
    private String getStudentInfo(HttpSession httpSession,Model model){
        String authenticateUserName=authController.getAuthenticateUserName();
        Student student=studentDAO.findByUserName(authenticateUserName);

        boolean isNull=false;
        if(httpSession.getAttribute("isNull")!=null){
            isNull=(boolean)httpSession.getAttribute("isNull");
        }

        boolean ifAny=false;
        List<String> selectedMonths=new ArrayList<>();
        if(httpSession.getAttribute("selectedMonth")!=null && httpSession.getAttribute("ifAny")!=null){
            ifAny=(boolean)httpSession.getAttribute("ifAny");
            List<String> temp=(List<String>) httpSession.getAttribute("selectedMonth");
            selectedMonths.addAll(temp);
        }

        LocalDate admissionDate=student.getAdmissionDate();
        LocalDate date1 =LocalDate.of(2011,12,12);
        LocalDate date2 =LocalDate.now();
        List<String> attendanceMonth=new ArrayList<>();
        while(admissionDate.isBefore(date2)){
//            System.out.println(admissionDate.toString());
//            System.out.println(admissionDate.getMonth()+"-"+admissionDate.getYear());
            attendanceMonth.add(admissionDate.getMonth()+"-"+admissionDate.getYear());
            admissionDate = admissionDate.plus(Period.ofMonths(1));
        }

        model.addAttribute("isNull",isNull);
        model.addAttribute("ifAny",ifAny);
        model.addAttribute("selectedMonth",selectedMonths);
        model.addAttribute("username",student.getFirstName());
        model.addAttribute("attendanceMonth",attendanceMonth);
        return "/homeDirectory/student-dashboard";
    }

    @PostMapping("/show-statistics")
    private String showStatistics(@RequestParam(name= "selectedItems", required = false) List<String> selectedMonth, Model model, HttpSession httpSession){

        if(selectedMonth==null){
            httpSession.setAttribute("isNull",true);
            return "redirect:/student/student-info";
        }

        for(String month:selectedMonth){
            System.out.println(month);
        }
        httpSession.setAttribute("ifAny",true);
        httpSession.setAttribute("selectedMonth",selectedMonth);
        return "redirect:/student/student-info";
    }
}
