package com.codemind.PlayCenter.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codemind.PlayCenter.utility.Utility;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

	Map<String, String> monthMap = Utility.getMonth();

	@GetMapping("/get-full-attended-students")
	private String getAttendedMonth(Model model) {
		System.out.println("Hello");

		return "";
	}

}
