package com.codemind.PlayCenter.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {

    public String getAuthenticateUserName(){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(authentication!= null){
            return authentication.getName();
        }else{
            return null;
        }

    }

}
