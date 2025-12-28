package com.example.CarProject.utils;

import com.example.CarProject.dto.CarDto;
import com.example.CarProject.entities.Car;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CarConverter {

    public Car toEntity(CarDto dto) {
        if (dto == null) {
            return null;
        }

        Car car = new Car();
        car.setBrand(StringUtils.capitalize(dto.getBrand().toLowerCase()));
        car.setModel(StringUtils.capitalize(dto.getModel().toLowerCase()));
        car.setProdYear(dto.getProdYear());
        car.setEngineSize(dto.getEngineSize());
        car.setHp(dto.getHp());
        car.setDescription(dto.getDescription());
        car.setImage(dto.getImage());

        return car;
    }
    public CarDto toDto(Car car){
        if(car == null){
            return null;
        }

        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setProdYear(car.getProdYear());
        dto.setEngineSize(car.getEngineSize());
        dto.setHp(car.getHp());
        dto.setDescription(car.getDescription());
        dto.setImage(car.getImage());
        dto.setUser(car.getUser().getId());
        dto.setHighlighted(car.isHighlighted());

        return dto;
    }
}
