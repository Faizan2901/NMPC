package com.codemind.PlayCenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginPage")
    private String getLoginPage(){
        return "/homeDirectory/login/login-page";
    }

    @GetMapping("/dashboard")
    private String getDashboardPage(){
        return "/homeDirectory/login/dash-board";
    }
}
