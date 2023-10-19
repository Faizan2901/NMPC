package com.codemind.PlayCenter.service;

import com.codemind.PlayCenter.dao.RoleDAO;
import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.dao.StudentNotFoundException;
import com.codemind.PlayCenter.entity.Role;
import com.codemind.PlayCenter.entity.Student;
import com.codemind.PlayCenter.user.WebUser;
import com.codemind.PlayCenter.utility.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	StudentDAO studentDAO;

	@Autowired
	RoleDAO roleDAO;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	JavaMailSender javaMailSender;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Student student = studentDAO.findByUserName(username);
		if (student == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(student.getRoles());

		return new org.springframework.security.core.userdetails.User(student.getUserName(), student.getPassword(),
				authorities);
	}

	public void save(WebUser webUser,HttpServletRequest httpRequest) throws UnsupportedEncodingException, MessagingException {
		
		LocalDate date=LocalDate.parse("2023-06-01");
		Student student = new Student();
		student.setUserName(webUser.getUserName());
		student.setPassword(bCryptPasswordEncoder.encode(webUser.getPassword()));
		student.setFirstName(webUser.getFirstName());
		student.setLastName(webUser.getLastName());
		student.setEmail(webUser.getEmail());
		student.setEnabled(1);
		student.setAdmissionDate(date);
		
		student.setRoles(Arrays.asList(roleDAO.findByName("ROLE_STUDENT")));

		studentDAO.save(student);
		
		sendEmail(webUser.getEmail(),webUser.getFirstName(),httpRequest);

	}

	private void sendEmail(String email,String firstName,HttpServletRequest httpRequest) throws UnsupportedEncodingException, MessagingException {
		// TODO Auto-generated method stub
		MimeMessage message = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("mo.faizansopariwala2901@gmail.com", "NMPC Support");

		helper.setTo(email);

		String subject = "Welcome to Nahne Munhe Playcenter";

		String content = "<p>Hello, "+firstName+"</p>" + "<p>You have successfully register with NMPC.</p>"
				+ "<p>Now you can explore our website <b><a href=\"" + Utility.getSiteURL(httpRequest)+"/nmpc-home"
				+ "\">Nahne Munhe Play Center</a></b> and make decision for your children's future with our services.</p>";
		helper.setSubject(subject);
		helper.setText(content, true);

		javaMailSender.send(message);
		
	}

	private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role tempRole : roles) {
			SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
			authorities.add(tempAuthority);
		}
		return authorities;

	}

	@Override
	public Student findByUserName(String userName) {
		return studentDAO.findByUserName(userName);
	}

	public void updateResetPasswordToken(String token, String email) throws StudentNotFoundException {
		
		Student student=studentDAO.findByEmail(email);
		
		if(student!=null) {
			student.setResetPasswordToken(token);
			
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
			student.setGenTokenTime(currentTimestamp);
			studentDAO.save(student);
		}else {
			throw new StudentNotFoundException("Could not find any student or teacher with email "+email);
		}
		
	}
	
	public Student get(String resetPasswordToken) {
		return studentDAO.findByResetPasswordToken(resetPasswordToken);
	}
	
	public void updatePassword(Student student,String newPassword) {
		
		student.setPassword(bCryptPasswordEncoder.encode(newPassword));
		student.setResetPasswordToken(null);
		
		studentDAO.save(student);
	}
 
}
