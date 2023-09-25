package com.codemind.PlayCenter.dao;


import com.codemind.PlayCenter.entity.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface StudentAttendanceDAO extends JpaRepository<StudentAttendance, Integer> {

    List<StudentAttendance> findByDate(LocalDate date);

    StudentAttendance findByStudentIdAndDate(int id, LocalDate date);

    @Query(value = "SELECT s.date FROM StudentAttendance s WHERE s.studentId = :id AND s.date LIKE %:dateMonth%")
    List<Date> findAttendanceByStudentIdAndMonth(@Param("id") int id, @Param("dateMonth") String dateMonth);

    @Query(value = "SELECT COUNT(*) FROM StudentAttendance s WHERE s.studentId = :id AND s.date LIKE %:dateMonth%")
    int findAttendanceDaysByStudentIdAndMonth(@Param("id") int id, @Param("dateMonth") String dateMonth);

}
