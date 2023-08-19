package com.codemind.PlayCenter.service;

import com.codemind.PlayCenter.entity.Student;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Student findByUserName(String username);

}
