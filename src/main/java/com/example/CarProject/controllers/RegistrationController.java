package com.example.CarProject.controllers;


import com.example.CarProject.dto.MyUserDto;
import com.example.CarProject.services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @GetMapping("/registrationForm")
    public String registrationForm(Model model){
        model.addAttribute("myUserDto",new MyUserDto());
        return "registrationForm";
    }

    @PostMapping("/registryUser")
    public String registryUser(@Valid @ModelAttribute MyUserDto myUserDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return("registrationForm");
        }
        registrationService.saveUser(myUserDto);
        return "redirect:/registration/success";
    }

    @GetMapping("/registration/success")
    public String registrationSuccess(){
        return "registrationSuccess";
    }



}
