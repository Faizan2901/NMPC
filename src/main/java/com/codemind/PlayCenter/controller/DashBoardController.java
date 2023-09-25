package com.codemind.PlayCenter.controller;

import com.codemind.PlayCenter.dao.StudentAttendanceDAO;
import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.entity.Role;
import com.codemind.PlayCenter.entity.Student;
import com.codemind.PlayCenter.entity.StudentAttendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private String getDashboardPage(Model model) {

        String authenticateUserName = authController.getAuthenticateUserName();
        Student student = studentDAO.findByUserName(authenticateUserName);


        List<StudentAttendance> studentAttendance = studentAttendanceDAO.findByDate(LocalDate.now());

        if (!studentAttendance.isEmpty()) {
            model.addAttribute("isHave", true);
        }


        model.addAttribute("userdetails", student.getFirstName() + " " + student.getLastName());


        return "/homeDirectory/login/dash-board";
    }

    @GetMapping("/fill-attendance")
    private String getAllStudentForAttendance(Model model) {
        List<Student> studentList = studentDAO.findAll();

        List<Student> students = new ArrayList<>();

        List<Student> attendedStudents = new ArrayList<>();

        boolean isPresentAttended = false;
        boolean isNotDoneAttendance = false;
        for (Student tempStudent : studentList) {
            List<Role> stuRole = tempStudent.getRoles();
            for (Role role : stuRole) {
                if (role.getName().equals("ROLE_STUDENT")) {
                    StudentAttendance studentAttendance = studentAttendanceDAO.findByStudentIdAndDate(tempStudent.getId(), LocalDate.now());
                    if (studentAttendance != null) {
                        isPresentAttended = true;
                        attendedStudents.add(tempStudent);
                    } else {
                        isNotDoneAttendance = true;
                        students.add(tempStudent);
                    }
                    break;
                }
            }
        }

        LocalDate date = LocalDate.now();
        model.addAttribute("isDoneStudent", isPresentAttended);
        model.addAttribute("attendanceDoneStudent", attendedStudents);
        model.addAttribute("isNotDoneAttendance", isNotDoneAttendance);
        model.addAttribute("allStudents", students);
        model.addAttribute("todayDate", date);
        return "/homeDirectory/attendance-page";
    }

    @PostMapping("/fill-info")
    private String showAttendedStudent(@RequestParam(name = "selectedItems", required = false) List<String> selectedItems, Model model) {

        List<StudentAttendance> tempStudentAttendance = studentAttendanceDAO.findByDate(LocalDate.now());

        if (selectedItems == null && tempStudentAttendance.isEmpty()) {
            return "redirect:/dashboard/fill-attendance";
        }

        if (selectedItems != null) {
            for (String selectString : selectedItems) {
                Student student = studentDAO.findByUserName(selectString);
                StudentAttendance studentAttendance = new StudentAttendance();
                studentAttendance.setStudentId(student.getId());
                studentAttendance.setDate(Date.valueOf(LocalDate.now()));
                studentAttendanceDAO.save(studentAttendance);
            }
            return "redirect:/dashboard/attended-student";

        } else {
            model.addAttribute("isNull", true);
            return "redirect:/dashboard/attended-student";
        }
    }


    @GetMapping("/attended-student")
    private String showAttendedStudentList(Model model) {

        List<StudentAttendance> studentAttendance = studentAttendanceDAO.findByDate(LocalDate.now());

        List<Student> allStudents = new ArrayList<>();

        for (StudentAttendance attendance : studentAttendance) {
            Optional<Student> tempStudent = studentDAO.findById(attendance.getStudentId());
            Student student = tempStudent.get();
            allStudents.add(student);
        }

        model.addAttribute("allStudents", allStudents);
        model.addAttribute("todayDate", LocalDate.now());

        return "/homeDirectory/attended-student-list";
    }

    @GetMapping("/deleteUser")
    String deleteStudentFromAttendanceList(@RequestParam("studentId") String id) {

        Optional<Student> tempStudent = studentDAO.findById(Integer.parseInt(id));
        Student student = tempStudent.get();

        StudentAttendance studentAttendance = studentAttendanceDAO.findByStudentIdAndDate(student.getId(), LocalDate.now());

        studentAttendanceDAO.delete(studentAttendance);

        if (studentAttendanceDAO.findByDate(LocalDate.now()).isEmpty()) {
            return "redirect:/dashboard/fill-attendance";
        }

        return "redirect:/dashboard/attended-student";

    }


}
