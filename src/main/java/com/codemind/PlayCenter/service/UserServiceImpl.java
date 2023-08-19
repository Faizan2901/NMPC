package com.codemind.PlayCenter.service;

import com.codemind.PlayCenter.dao.StudentDAO;
import com.codemind.PlayCenter.entity.Role;
import com.codemind.PlayCenter.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    StudentDAO studentDAO;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Student student=studentDAO.findByUserName(username);
        if(student==null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(student.getRoles());

        return new org.springframework.security.core.userdetails.User(student.getUserName(), student.getPassword(),
                authorities);
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
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



}
