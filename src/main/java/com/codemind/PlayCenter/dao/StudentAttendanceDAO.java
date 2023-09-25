package com.codemind.PlayCenter.dao;


import com.codemind.PlayCenter.entity.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface StudentAttendanceDAO extends JpaRepository<StudentAttendance, Integer> {

    List<StudentAttendance> findByStudentUsername(String userName);

    List<StudentAttendance> findByDate(LocalDate date);

    StudentAttendance findByStudentUsernameAndDate(String userName, LocalDate date);

    @Query(value = "SELECT s.date FROM StudentAttendance s WHERE s.studentUsername = :userName AND s.date LIKE %:dateMonth%")
    List<Date> findAttendanceByStudentNameAndMonth(@Param("userName") String userName, @Param("dateMonth") String dateMonth);

    @Query(value = "SELECT COUNT(*) FROM StudentAttendance s WHERE s.studentUsername = :userName AND s.date LIKE %:dateMonth%")
    int findAttendanceDaysByStudentNameAndMonth(@Param("userName") String userName, @Param("dateMonth") String dateMonth);

}
