package com.codemind.PlayCenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @GetMapping("/issuesLogin")
    private String issuesWithLogin() {
        return "/homeDirectory/issues-handle/issues-page";
    }
}
