package com.example.CarProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginProcessController {

    @RequestMapping("/authentication/login")
    public String login(){
        return "login";
    }

}
