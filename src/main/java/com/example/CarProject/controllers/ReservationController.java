package com.example.CarProject.controllers;

import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.Reservation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.CarProject.services.ReservationService;
import com.example.CarProject.utils.ReservationConverter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
//        Reservation reservation = reservationConverter.toEntity(reservationDto);
        reservationService.saveReservation(reservationDto);

        return("addedReservation");
    }

    @GetMapping("/reservations")
        public String allReservations(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size, @RequestParam (defaultValue = "no") String sort, Model model){
            Page<Reservation> result = reservationService.selectPaging(page,size,sort);
        long totalElements = result.getTotalElements();
        int startItem = (page * size) + 1;
        int endItem = Math.min(startItem + size - 1, (int) totalElements);

            model.addAttribute("list", result);
            model.addAttribute("numbers",reservationService.createPageNumbers(page, result.getTotalPages()));
            model.addAttribute("sort",sort);
            model.addAttribute("size",size);

            model.addAttribute("totalElements", totalElements);
            model.addAttribute("startItem", startItem);
            model.addAttribute("endItem", endItem);
        return "reservations";
    }

@GetMapping("/reservationExport")
public void reservationExportToCsv(HttpServletResponse response) throws IOException {

    response.setContentType("text/csv");
    response.setHeader("Content-Disposition", "attachment; filename=reservations.csv");

    String csv = reservationService.generateCsv();

    try (PrintWriter writer = response.getWriter()) {
        writer.write(csv);
        }
    }

}
