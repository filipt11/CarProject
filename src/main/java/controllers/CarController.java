package controllers;

import entities.Car;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping("/")
    public String carList1(Model model){
        model.addAttribute("list",carService.selectHighlighted());
        return "carList";
    }
    @RequestMapping("/formAdding")
    public String formAdding(Car car) {
        //car = new Car();
        return "formAdding";
    }
    @RequestMapping("/allCars")
    public String allCars(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size, @RequestParam (defaultValue = "no") String sort, Model model){
        Page<Car> result = carService.selectPaging(page,size,sort);
        model.addAttribute("list", result);
        model.addAttribute("numbers",carService.createPageNumbers(page, result.getTotalPages()));
        model.addAttribute("sort",sort);
        model.addAttribute("size",size);
        return "allCars";
    }

    @RequestMapping("/addCar")
    public String addCar(Car car){
        carService.saveCar(car);
        return "added";
    }
    @RequestMapping("/carList")
    public String carList(Model model){
        model.addAttribute("list",carService.selectHighlighted());
        return "carList";
    }
}
