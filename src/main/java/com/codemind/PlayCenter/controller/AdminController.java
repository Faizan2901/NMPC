package com.codemind.PlayCenter.controller;

import com.codemind.PlayCenter.dao.StudentAttendanceDAO;
import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.entity.Student;
import com.codemind.PlayCenter.entity.StudentAttendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    StudentDAO studentDAO;

    @Autowired
    StudentAttendanceDAO studentAttendanceDAO;


    @GetMapping("/deleteUser")
    String deleteUser(@RequestParam("userId") String userId) {

        Optional<Student> student=studentDAO.findById(Integer.parseInt(userId));

        if(student.isPresent()){
            student.get().getRoles().clear();
            List<StudentAttendance> studentAttendanceList=studentAttendanceDAO.findAttendanceByStudentId(student.get().getId());
            for(StudentAttendance studentAttendance:studentAttendanceList){
                studentAttendanceDAO.deleteById(studentAttendance.getId());
            }
            studentDAO.deleteById(Integer.parseInt(userId));
        }

        return "redirect:/dashboard/show-user";
    }

}
