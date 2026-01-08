package com.example.CarProject.converters;

import com.example.CarProject.converters.CarConverter;
import com.example.CarProject.dto.CarDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.MyUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class CarConverterTest {

    @InjectMocks
    private CarConverter carConverter;

    private Car car;
    private CarDto carDto;
    private MyUser myUser;

    @BeforeEach
    void init(){
        carConverter = new CarConverter();
        myUser = new MyUser(10L,"admin","admin@example.com","123","ROLE_ADMIN",null,null,false);

        car = new Car(10L,"Bmw","M3",2008,4.2,400,"short descr","img1.png",true,null,myUser);

        carDto = new CarDto(20L,"Audi","Rs7",2020,4.0,250,"description",null,myUser.getId(),false);

    }

    @Test
    void shouldReturnNullWhenDtoIsNull(){
        Assertions.assertNull(carConverter.toEntity(null));
    }

    @Test
    void shouldConvertToEntity(){
        Car car1 = carConverter.toEntity(carDto);

        Assertions.assertEquals("Audi",car1.getBrand());
        Assertions.assertEquals("Rs7",car1.getModel());
        Assertions.assertEquals(2020,car1.getProdYear());
        Assertions.assertEquals(4.0,car1.getEngineSize());
        Assertions.assertEquals(250,car1.getHp());
        Assertions.assertEquals("description",car1.getDescription());
        Assertions.assertNull(car1.getImage());
    }

    @Test
    void shouldReturnNullWhenCarIsNull(){
        Assertions.assertNull(carConverter.toDto(null));
    }

    @Test
    void shouldConvertToDto(){
        CarDto carDto1 = carConverter.toDto(car);

        Assertions.assertEquals(10,carDto1.getId());
        Assertions.assertEquals("Bmw",carDto1.getBrand());
        Assertions.assertEquals("M3",carDto1.getModel());
        Assertions.assertEquals(2008,carDto1.getProdYear());
        Assertions.assertEquals(4.2,carDto1.getEngineSize());
        Assertions.assertEquals(400,carDto1.getHp());
        Assertions.assertEquals("short descr",carDto1.getDescription());
        Assertions.assertEquals("img1.png",carDto1.getImage());
        Assertions.assertTrue(carDto1.isHighlighted());
        Assertions.assertEquals(myUser.getId(),carDto1.getUser());
    }

}
