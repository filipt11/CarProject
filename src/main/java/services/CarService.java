package services;

import entities.Car;
import repositories.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void saving(Car car){
        carRepository.save(car);
    }
    public List<Car> selectAll(){
        return carRepository.findAll();
    }

}
