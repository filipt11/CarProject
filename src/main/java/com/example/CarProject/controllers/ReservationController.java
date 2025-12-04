package com.example.CarProject.controllers;

import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Reservation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.CarProject.services.ReservationService;
import com.example.CarProject.utils.ReservationConverter;

@Controller
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Autowired
    private ReservationConverter reservationConverter;

    @GetMapping("/formReservation")
        public String formReservation(Model model){
        model.addAttribute("reservationDto", new ReservationDto());
        return("formReservation");
    }

    @PostMapping("/addReservation")
    public String addReservation(@Valid @ModelAttribute ReservationDto reservationDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return("formReservation");
        }
        Reservation reservation = reservationConverter.toEntity(reservationDto);
        reservationService.saveReservation(reservation);

        return("addedReservation");
    }

    @GetMapping("/reservations")
        public String allReservations(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size, @RequestParam (defaultValue = "no") String sort, Model model){
            Page<Reservation> result = reservationService.selectPaging(page,size,sort);
            model.addAttribute("list", result);
            model.addAttribute("numbers",reservationService.createPageNumbers(page, result.getTotalPages()));
            model.addAttribute("sort",sort);
            model.addAttribute("size",size);
        return "reservations";
    }




}
