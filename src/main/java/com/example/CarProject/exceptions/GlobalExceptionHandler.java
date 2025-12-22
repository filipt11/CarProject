package com.example.CarProject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CarNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCarNotFound(CarNotFoundException ex, Model model) {
        logger.error("Car not found: " + ex);
        model.addAttribute("status", 404);
        model.addAttribute("error", ex.getMessage());

        return "errorPage";
    }

    @ExceptionHandler(TakenUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleTakenUsername(TakenUsernameException ex, Model model){
        logger.warn("User with that username already exits" + ex);
        model.addAttribute("status",409);
        model.addAttribute("error",ex.getMessage());

        return "errorPage";
    }

    @ExceptionHandler(TakenEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleTakenEmail(TakenEmailException ex, Model model){
        logger.warn("User with that email already exits" + ex);
        model.addAttribute("status",409);
        model.addAttribute("error",ex.getMessage());

        return "errorPage";
    }

    @ExceptionHandler(PasswordsNotMatchingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotMatchingPasswords(PasswordsNotMatchingException ex, Model model){
        logger.warn("Passwords are not matching");
        model.addAttribute("status",400);
        model.addAttribute("error",ex.getMessage());

        return "errorPage";
    }

}
