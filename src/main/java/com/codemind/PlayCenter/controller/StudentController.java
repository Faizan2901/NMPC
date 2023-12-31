package com.codemind.PlayCenter.controller;

import com.codemind.PlayCenter.dao.StudentAttendanceDAO;
import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.entity.Student;
import com.codemind.PlayCenter.utility.Utility;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	AuthController authController;

	@Autowired
	StudentDAO studentDAO;

	@Autowired
	StudentAttendanceDAO studentAttendanceDAO;

	Map<String, String> monthMap = Utility.getMonth();

	@GetMapping("/student-info")
	private String getStudentInfo(Model model) {
		String authenticateUserName = authController.getAuthenticateUserName();
		Student student = studentDAO.findByUserName(authenticateUserName);

		LocalDate admissionDate = student.getAdmissionDate();
		LocalDate date2 = LocalDate.now();
		List<String> attendanceMonth = new ArrayList<>();
		while (admissionDate.isBefore(date2)) {
			attendanceMonth.add(admissionDate.getMonth() + "-" + admissionDate.getYear());
			admissionDate = admissionDate.plus(Period.ofMonths(1));
		}

		model.addAttribute("username", student.getFirstName());
		model.addAttribute("attendanceMonth", attendanceMonth);
		return "/homeDirectory/student-dashboard";
	}

	@PostMapping("/show-statistics")
	private String showStatistics(@RequestParam(name = "selectedItems", required = false) List<String> selectedMonth,
			Model model, HttpSession httpSession) {

		if (selectedMonth == null) {
			return "redirect:/student/student-info";
		}

		model.addAttribute("selectedMonth", selectedMonth);
		httpSession.setAttribute("selectedMonth", selectedMonth);
		return "redirect:/student/statistics";
	}

	@GetMapping("/statistics")
	private String showStatistics(HttpSession httpSession, Model model) {

		List<String> months = new ArrayList<>();

		Student student = studentDAO.findByUserName(authController.getAuthenticateUserName());

		Map<String, Map<List<Date>, Integer>> dateMonthMap = new LinkedHashMap<>();

		months = (List<String>) httpSession.getAttribute("selectedMonth");

		for (String month : months) {
			List<Date> dates = studentAttendanceDAO.findAttendanceByStudentIdAndMonth(student.getId(),
					monthMap.get(month.substring(0, month.indexOf("-"))));
			int dayCount = studentAttendanceDAO.findAttendanceDaysByStudentIdAndMonth(student.getId(),
					monthMap.get(month.substring(0, month.indexOf("-"))));
			if (dayCount > 0) {
				HashMap<List<Date>, Integer> dateCountMap = new HashMap<>();
				dateCountMap.put(dates, dayCount);
				dateMonthMap.put(month, dateCountMap);
			}
		}

		model.addAttribute("dateMonthMap", dateMonthMap);
		model.addAttribute("name", student.getFirstName() + " " + student.getLastName());
		return "/homeDirectory/show-attendance-statistics";
	}
}
