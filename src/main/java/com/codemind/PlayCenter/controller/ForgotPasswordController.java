package com.codemind.PlayCenter.controller;

import java.io.UnsupportedEncodingException;
import java.net.http.HttpRequest;
import java.sql.Timestamp;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.dao.StudentNotFoundException;
import com.codemind.PlayCenter.entity.Student;
import com.codemind.PlayCenter.service.UserService;
import com.codemind.PlayCenter.service.UserServiceImpl;
import com.codemind.PlayCenter.utility.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;

@Controller
@RequestMapping("/forgot")
public class ForgotPasswordController {

	@Autowired
	StudentDAO studentDAO;

	@Autowired
	UserServiceImpl serviceImpl;

	@Autowired
	JavaMailSender javaMailSender;

	@GetMapping("/forgotpassword")
	public String showForgotPasswordForm(Model model) {
		return "/homeDirectory/forgotPassword/forgot-password-form";
	}

	@PostMapping("/forgotPassword")
	public String processForgotPasswordForm(HttpServletRequest httpRequest, Model model) {

		String email = httpRequest.getParameter("email");
		String token = RandomString.make(45);
		System.out.println("Email: " + email + " and Random token: " + token);
		try {
			serviceImpl.updateResetPasswordToken(token, email);
			String resetPasswordLink = Utility.getSiteURL(httpRequest) + "/forgot/reset?token=" + token;

			sendEmail(email, resetPasswordLink);

			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

			System.out.println(resetPasswordLink);
		} catch (StudentNotFoundException e) {

			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Error while sending email!!");
		}
		return "/homeDirectory/forgotPassword/forgot-password-form";
	}

	private void sendEmail(String email, String resetPasswordLink)
			throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("mo.faizansopariwala2901@gmail.com", "NMPC Support");

		helper.setTo(email);

		String subject = "Here's the link to reset your password";

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><b><a href=\"" + resetPasswordLink
				+ "\">Change my password</a></b></p>"
				+ "<p>Ignore this email if you do remember your password, or you have not made the request.</p>";
		helper.setSubject(subject);
		helper.setText(content, true);

		javaMailSender.send(message);
	}

	@GetMapping("/reset")
	public String showResetPasswordForm(@RequestParam("token") String token, Model model) {

		Student student = serviceImpl.get(token);

		Timestamp dbTimestamp = student.getGenTokenTime();

		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

		Calendar calendar = Calendar.getInstance();

		int timeDifferenceInMinutes = 1;

		long timeDifferenceMillis = currentTimestamp.getTime() - dbTimestamp.getTime();

		long timeDifferenceMinutes = timeDifferenceMillis / (60 * 1000);

		if (student == null || timeDifferenceMinutes < timeDifferenceInMinutes) {

			model.addAttribute("error", "Invalid Token or Link is expired");

			return "/homeDirectory/forgotPassword/forgot-password-form";
		}

		model.addAttribute("token", token);

		return "/homeDirectory/forgotPassword/reset-password-form";
	}

	@PostMapping("/reset-password")
	public String resetPassword(HttpServletRequest request, Model model) {

		String token = request.getParameter("token");
		String password = request.getParameter("password");

		Student student = serviceImpl.get(token);

		if (student == null) {
			model.addAttribute("error", "Invalid Token or Link is expired");
			return "/homeDirectory/forgotPassword/forgot-password-form";
		} else {
			serviceImpl.updatePassword(student, password);
			model.addAttribute("message",
					"You have successfully chnaged your password. Now you can use new credentials for login.");
		}

		return "/homeDirectory/login/login-page";
	}

}
