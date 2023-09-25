package com.codemind.PlayCenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/explore")
public class ExploreController {

    @GetMapping("/explorePage")
    private String showExplorePage() {
        return "/homeDirectory/explore-page";
    }
}
