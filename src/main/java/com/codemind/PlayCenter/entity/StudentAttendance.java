package com.codemind.PlayCenter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "student_attendance")
public class StudentAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "stu_id")
    private Integer studentId;

    @Column(name = "stu_username")
    private String studentUsername;

    @Column(name = "date")
    private Date date;

}
