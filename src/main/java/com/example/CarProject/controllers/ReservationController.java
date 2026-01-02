package com.example.CarProject.controllers;

import com.example.CarProject.dto.CarDto;
import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.Reservation;
import com.example.CarProject.exceptions.CarNotFoundException;
import com.example.CarProject.exceptions.ReservationNotFoundException;
import com.example.CarProject.repositories.CarRepository;
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
    private final CarRepository carRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, CarRepository carRepository) {
        this.reservationService = reservationService;
        this.carRepository = carRepository;
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

    @GetMapping("/reservationDetails/{id}")
    public String reservationDetails(@PathVariable Long id, Model model){
        Reservation reservation = reservationService.findById(id).orElseThrow(() -> new ReservationNotFoundException());
        String owner = reservation.getUser().getUsername();
        String brand = reservation.getCar().getBrand();
        String carModel = reservation.getCar().getModel();
        Long carId = reservation.getCar().getId();

        model.addAttribute("reservation",reservation);
        model.addAttribute("owner",owner);
        model.addAttribute("brand",brand);
        model.addAttribute("carModel",carModel);
        model.addAttribute("carId",carId);

        return "reservationDetails";
    }

    @GetMapping("/administration/reservationUpdate/{id}")
    public String reservationUpdate(@PathVariable Long id, Model model){
        Reservation reservation = reservationService.findById(id).orElseThrow(() -> new ReservationNotFoundException());
        Car car = carRepository.findById(reservation.getCar().getId()).orElseThrow(() -> new CarNotFoundException());

        ReservationDto dto = reservationConverter.toDto(reservation);

        Long owner = dto.getUserId();
        String brand = car.getBrand();
        String carModel = car.getModel();
        Long carId = dto.getCarId();

        model.addAttribute("reservationDto",dto);
        model.addAttribute("owner",owner);
        model.addAttribute("brand",brand);
        model.addAttribute("carModel",carModel);
        model.addAttribute("carId",carId);

        return "reservationUpdate";
    }

    @PostMapping("/administration/reservationUpdate/process")
    public String updateReservation(@Valid @ModelAttribute ReservationDto reservationDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Long owner = reservationDto.getUserId();
            Long carId = reservationDto.getCarId();
            Car car = carRepository.findById(carId).orElseThrow(()-> new CarNotFoundException());

            model.addAttribute("owner", owner);
            model.addAttribute("brand", car.getBrand());
            model.addAttribute("carModel", car.getModel());
            model.addAttribute("carId", carId);

            return "reservationUpdate";
        }

        reservationService.updateReservation(reservationDto);
        Long id = reservationDto.getId();

        return "redirect:/reservationDetails/" + id ;

    }

    @GetMapping("/administration/reservationDelete/{id}")
    public String reservationDelete(@PathVariable Long id){
            reservationService.deleteReservation(id);

            return "redirect:/reservations";
        }


}
