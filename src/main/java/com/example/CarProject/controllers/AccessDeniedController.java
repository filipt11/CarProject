package com.example.CarProject.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/accessDenied")
    public String accessDenied(Model model){
        model.addAttribute("status", 403);
        model.addAttribute("error", "Access Denied");

        return "errorPage";
    }


}
