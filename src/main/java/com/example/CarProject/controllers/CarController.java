package com.example.CarProject.controllers;

import com.example.CarProject.dto.CarDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.exceptions.CarNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.CarProject.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.CarProject.utils.CarConverter;

import java.io.IOException;
import java.io.PrintWriter;
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
        carService.saveCar(carDto);

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

        long totalElements = result.getTotalElements();
        int startItem = (page * size) + 1;
        int endItem = Math.min(startItem + size - 1, (int) totalElements);

        model.addAttribute("list", result);
        model.addAttribute("numbers",carService.createPageNumbers(page, result.getTotalPages()));
        model.addAttribute("sort",sort);
        model.addAttribute("size",size);
        model.addAttribute("brands",carService.selectBrands());
        model.addAttribute("selectedBrands", brand);
        
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("startItem", startItem);
        model.addAttribute("endItem", endItem);
        return "allCars";
    }

    @GetMapping("/carList")
    public String carList(Model model){
        model.addAttribute("list",carService.selectHighlighted());
        return "carList";
    }

    @GetMapping("/carExport")
    public void carExportToCsv(
            @RequestParam(required = false) List<String> brand,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=cars.csv");

        String csv = carService.generateCsv(brand);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(csv);
            }
        }

    @GetMapping("/carDetails/{id}")
    public String carDetails(@PathVariable Long id, Model model){
        Car car = carService.findById(id).orElseThrow(() -> new CarNotFoundException());
        String owner = car.getUser().getUsername();
        model .addAttribute("car",car);
        model .addAttribute("owner",owner);
        return "carDetails";
        }

    @GetMapping("/administration/carUpdate/{id}")
    public String carUpdate(@PathVariable Long id, Model model){
        Car car = carService.findById(id).orElseThrow(() -> new CarNotFoundException());

        CarDto dto = carConverter.toDto(car);
        model.addAttribute("carDto",dto);

        return "carUpdate";
    }

    @PostMapping("/administration/carUpdate/process")
    public String updateCar(@Valid @ModelAttribute CarDto carDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "carUpdate";
        }
        Long id = carDto.getId();

        carService.updateEntity(carDto);

        return "redirect:/carDetails/" +id;
    }

    @GetMapping("/administration/carDelete/{id}")
    public String carDelete(@PathVariable Long id){
        carService.deleteEntity(id);

        return "redirect:/carList";
    }

}
