package controllers;

import entities.Car;
import services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @RequestMapping("/formAdding")
    public String formAdding(Car car) {
        //car = new Car();
        return "formAdding";
    }
    @RequestMapping("/addCar")
    public String addCar(Car car){
        carService.saving(car);
        return "added";
    }
    @RequestMapping("/carList")
    public String carList(Model model){
        model.addAttribute("list",carService.selectAll());
        return "carList";
    }
}
