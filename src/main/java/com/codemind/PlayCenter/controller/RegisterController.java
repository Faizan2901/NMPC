package com.codemind.PlayCenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @GetMapping("/registerPage")
    private String issuesWithLogin() {
        return "/homeDirectory/register/register-page";
    }
}
