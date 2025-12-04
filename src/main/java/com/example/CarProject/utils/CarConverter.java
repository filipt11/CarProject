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
}
