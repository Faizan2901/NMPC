package com.codemind.PlayCenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @GetMapping("/nmpc-home")
    private String showHomePage(){
        return "/homeDirectory/home-Page";
    }
}
