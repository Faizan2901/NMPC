package com.codemind.PlayCenter.controller;

import com.codemind.PlayCenter.dao.StudentAttendanceDAO;
import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.entity.Student;
import com.codemind.PlayCenter.entity.StudentAttendance;
import jakarta.annotation.PostConstruct;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    AuthController authController;

    @Autowired
    StudentDAO studentDAO;

    @Autowired
    StudentAttendanceDAO studentAttendanceDAO;

    Map<String,String> monthMap=new HashMap<>();
    @PostConstruct
    private void getMonth(){

        monthMap.put("JANUARY","01");
        monthMap.put("FEBUARY","02");
        monthMap.put("MARCH","03");
        monthMap.put("APRIL","04");
        monthMap.put("MAY","05");
        monthMap.put("JUNE","06");
        monthMap.put("JULY","07");
        monthMap.put("AUGUST","08");
        monthMap.put("SEPTEMBER","09");
        monthMap.put("OCTOBER","10");
        monthMap.put("NOVEMBER","11");
        monthMap.put("DECEMBER","12");

    }
    @GetMapping("/student-info")
    private String getStudentInfo(Model model){
        String authenticateUserName=authController.getAuthenticateUserName();
        Student student=studentDAO.findByUserName(authenticateUserName);

        LocalDate admissionDate=student.getAdmissionDate();
        LocalDate date2 =LocalDate.now();
        List<String> attendanceMonth=new ArrayList<>();
        while(admissionDate.isBefore(date2)){
//            System.out.println(admissionDate.toString());
//            System.out.println(admissionDate.getMonth()+"-"+admissionDate.getYear());
            attendanceMonth.add(admissionDate.getMonth()+"-"+admissionDate.getYear());
            admissionDate = admissionDate.plus(Period.ofMonths(1));
        }

        model.addAttribute("username",student.getFirstName());
        model.addAttribute("attendanceMonth",attendanceMonth);
        return "/homeDirectory/student-dashboard";
    }

    @PostMapping("/show-statistics")
    private String showStatistics(@RequestParam(name= "selectedItems", required = false) List<String> selectedMonth, Model model,HttpSession httpSession){

        if(selectedMonth==null){
            return "redirect:/student/student-info";
        }

//        for(String month:selectedMonth){
//            System.out.println(month);
//        }
        model.addAttribute("selectedMonth",selectedMonth);
        httpSession.setAttribute("selectedMonth",selectedMonth);
        return "redirect:/student/statistics";
    }

    @GetMapping("/statistics")
    private String showStatistics(HttpSession httpSession){

        List<String> months=new ArrayList<>();

        months=(List<String>) httpSession.getAttribute("selectedMonth");

        for(String month:months){
            System.out.println(monthMap.get(month.substring(0,month.indexOf("-"))));
            System.out.println(studentAttendanceDAO.findAttendanceByStudentNameAndMonth(authController.getAuthenticateUserName(), monthMap.get(month.substring(0,month.indexOf("-")))));
        }

        return "";
    }
}

