package com.codemind.PlayCenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/nmpc-home")
    private String showHomePage() {
        return "/homeDirectory/home-Page";
    }
}
