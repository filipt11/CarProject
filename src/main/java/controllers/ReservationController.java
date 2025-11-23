package controllers;

import dto.CarDto;
import dto.ReservationDto;
import entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import services.ReservationService;
import utils.ReservationConverter;

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
        model.addAttribute("ReservationDto", new ReservationDto());
        return("formReservation");
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
