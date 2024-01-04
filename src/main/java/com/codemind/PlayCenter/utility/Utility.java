package com.codemind.PlayCenter.utility;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {

	public static Map<String, String> getMonth() {

		Map<String, String> monthMap = new HashMap<>();

		monthMap.put("JANUARY", "01");
		monthMap.put("FEBRUARY", "02");
		monthMap.put("MARCH", "03");
		monthMap.put("APRIL", "04");
		monthMap.put("MAY", "05");
		monthMap.put("JUNE", "06");
		monthMap.put("JULY", "07");
		monthMap.put("AUGUST", "08");
		monthMap.put("SEPTEMBER", "09");
		monthMap.put("OCTOBER", "10");
		monthMap.put("NOVEMBER", "11");
		monthMap.put("DECEMBER", "12");

		return monthMap;

	}

	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

}
