package com.codemind.PlayCenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/nmpc-home")
    private String showHomePage(Model model) {
    	
    	String pdfUrl1="/pdfs/Faizan-Final-Curriculum-vitae.pdf";
    	
    	model.addAttribute("pdfUrl",pdfUrl1);
    	
        return "/homeDirectory/home-Page";
    }
}
