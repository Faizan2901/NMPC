package com.codemind.PlayCenter.dao;


import com.codemind.PlayCenter.entity.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface StudentAttendanceDAO extends JpaRepository<StudentAttendance,Integer> {

    List<StudentAttendance> findByStudentUsername(String userName);

    List<StudentAttendance> findByDate(LocalDate date);

    StudentAttendance findByStudentUsernameAndDate(String userName,LocalDate date);


}
