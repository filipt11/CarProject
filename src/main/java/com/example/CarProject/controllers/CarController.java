package com.example.CarProject.controllers;

import com.example.CarProject.dto.CarDto;
import com.example.CarProject.entities.Car;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.CarProject.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.CarProject.utils.CarConverter;

import java.util.List;

@Controller
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Autowired
    private CarConverter carConverter;

    @RequestMapping("/")
    public String carList1(Model model){
        model.addAttribute("list",carService.selectHighlighted());
        return "carList";
    }

    @GetMapping("/formAdding")
    public String formAdding(Model model) {
        model.addAttribute("carDto", new CarDto());
        return "formAdding";
    }

    @PostMapping("/addCar")
    public String addCar(@Valid @ModelAttribute CarDto carDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "formAdding";
        }
        Car car = carConverter.toEntity(carDto);
        carService.saveCar(car);
        return "redirect:/added";
    }

    @RequestMapping("/added")
    public String added123 (Model model){
        return "added";
    }

    @GetMapping("/allCars")
    public String allCars(
            @RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size, @RequestParam (defaultValue = "no") String sort, @RequestParam (required=false) List<String> brand, Model model){

        if (brand == null || brand.isEmpty()) {
            brand = carService.selectBrands();
        }

        Page<Car> result = carService.selectPaging(page, size, sort, brand);

        model.addAttribute("list", result);
        model.addAttribute("numbers",carService.createPageNumbers(page, result.getTotalPages()));
        model.addAttribute("sort",sort);
        model.addAttribute("size",size);
        model.addAttribute("brands",carService.selectBrands());
        model.addAttribute("selectedBrands", brand);
        return "allCars";
    }

    @RequestMapping("/carList")
    public String carList(Model model){
        model.addAttribute("list",carService.selectHighlighted());
        return "carList";
    }
}
