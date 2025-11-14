package controllers;


import entities.Car;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import services.ReservationService;

@Controller
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequestMapping("/reservations")
    public String reservationList(Model model){

        return "reservations";
    }


}
