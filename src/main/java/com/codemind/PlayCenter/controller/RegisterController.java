package com.codemind.PlayCenter.controller;

import com.codemind.PlayCenter.entity.Student;
import com.codemind.PlayCenter.service.UserService;
import com.codemind.PlayCenter.user.WebUser;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);

    }

    @GetMapping("/registerPage")
    private String showRegisterPage(Model model) {

        model.addAttribute("webUser", new WebUser());
        return "/homeDirectory/register/register-page";
    }

    @PostMapping("/processData")
    private String processRegisterData(@ModelAttribute("webUser") WebUser webUser, BindingResult bindingResult, Model model, HttpSession httpSession,HttpServletRequest httpRequest) throws UnsupportedEncodingException, MessagingException {
        String userName = webUser.getUserName();
        logger.info("Processing registration for : " + userName);

        Student student = userService.findByUserName(userName);

        if (student != null) {
            model.addAttribute("isExists",true);

            logger.warning("Username already exists.");
            return "/homeDirectory/register/register-page";
        }

        userService.save(webUser,httpRequest);
        logger.info("Successfully created user : " + userName);

        httpSession.setAttribute("user", webUser);

        return "redirect:/loginPage";


    }
}
