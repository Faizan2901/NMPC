package com.codemind.PlayCenter.service;

import com.codemind.PlayCenter.entity.Student;
import com.codemind.PlayCenter.user.WebUser;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Student findByUserName(String username);

    void save(WebUser webUser,HttpServletRequest httpRequest) throws UnsupportedEncodingException, MessagingException;

}
