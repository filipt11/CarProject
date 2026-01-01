package com.example.CarProject.exceptions;

import com.example.CarProject.dto.MyUserDto;
import com.example.CarProject.security.SignedUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(UserNotFoundException ex, Model model) {
        logger.error("User not found: " + ex);
        model.addAttribute("status", 404);
        model.addAttribute("error", ex.getMessage());

        return "errorPage";
    }
    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleReservationNotFound(ReservationNotFoundException ex, Model model) {
        logger.error("Reservation not found: " + ex);
        model.addAttribute("status", 404);
        model.addAttribute("error", ex.getMessage());

        return "errorPage";
    }

    @ExceptionHandler(InvalidReservationDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidReservationDate(InvalidReservationDateException ex, Model model) {
        logger.warn("Invalid Reservation Date: " + ex);
        model.addAttribute("status", 400);
        model.addAttribute("error", ex.getMessage());

        return "errorPage";
    }

    @ExceptionHandler(TakenUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleTakenUsername(TakenUsernameException ex, Model model){
        logger.warn("User with that username already exits" + ex);
        model.addAttribute("error",ex.getMessage());
        model.addAttribute("myUserDto", new MyUserDto());

        return "registrationForm";
    }

    @ExceptionHandler(TakenEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleTakenEmail(TakenEmailException ex, Model model){
        logger.warn("User with that email already exits" + ex);
        model.addAttribute("error",ex.getMessage());
        model.addAttribute("myUserDto", new MyUserDto());

        return "registrationForm";
    }

    @ExceptionHandler(PasswordsNotMatchingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotMatchingPasswords(PasswordsNotMatchingException ex, Model model){
        logger.warn("Passwords are not matching");
        model.addAttribute("error",ex.getMessage());
        model.addAttribute("myUserDto", new MyUserDto());

        return "registrationForm";
    }

    @ModelAttribute("currentUser")
    public SignedUserDetails signedUserDetails(@AuthenticationPrincipal SignedUserDetails user){
        return user;
    }


}
