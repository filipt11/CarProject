package utils;

import dto.CarDto;
import entities.Car;
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
