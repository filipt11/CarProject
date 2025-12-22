package com.example.CarProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginProcessController {

    @RequestMapping("/authentication/login")
    public String login(@RequestParam (required = false)String failed, Model model){

        if(failed!=null) {
            model.addAttribute("loginError", "Invalid username or password");
        }

        return "login";
    }

}
