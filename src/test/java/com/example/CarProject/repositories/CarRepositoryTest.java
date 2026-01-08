package com.example.CarProject.repositories;

import com.example.CarProject.entities.Car;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    void shouldReturnedSortedBrands(){
        carRepository.save(new Car("Bmw","E92",2020,4.0,420,"description"));
        carRepository.save(new Car("Audi","Rsq7",2019,4.0,420,"description"));
        carRepository.save(new Car("Mercedes-Benz","C400",2008,5.0,420,"description"));
        carRepository.save(new Car("Toyota","Celica",1999,3.0,250,"description"));

        List<String> brands = carRepository.findAllDistinctBrandsOrdered();
        List<String> expectedBrands = List.of("Audi","Bmw","Mercedes-Benz","Toyota");
        Assertions.assertEquals(expectedBrands,brands);
    }

    @Test
    void shouldReturnedSortedBrandsDistinct(){
        carRepository.save(new Car("Bmw","E92",2020,4.0,420,"description"));
        carRepository.save(new Car("Audi","Rsq7",2019,4.0,420,"description"));
        carRepository.save(new Car("Mercedes-Benz","C400",2008,5.0,420,"description"));
        carRepository.save(new Car("Toyota","Celica",1999,3.0,250,"description"));
        carRepository.save(new Car("Opel","Celica",1999,3.0,250,"description"));
        carRepository.save(new Car("Bmw","Celica",1999,3.0,250,"description"));
        carRepository.save(new Car("Audi","Celica",1999,3.0,250,"description"));

        List<String> brands = carRepository.findAllDistinctBrandsOrdered();
        List<String> expectedBrands = List.of("Audi","Bmw","Mercedes-Benz","Opel","Toyota");
        Assertions.assertEquals(expectedBrands,brands);
    }

}
