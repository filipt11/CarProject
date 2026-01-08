package com.example.CarProject.converters;


import com.example.CarProject.converters.ReservationConverter;
import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.entities.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class ReservationConverterTest {

    @InjectMocks
    private ReservationConverter reservationConverter;

    @Mock
    private MyUser myUser;

    @Mock
    private Car car;

    private Reservation reservation;
    private ReservationDto reservationDto;

    @BeforeEach
    void init(){
        reservation = new Reservation(10L, LocalDate.now(),LocalDate.now().plusDays(2),null,null);
        reservationDto = new ReservationDto(LocalDate.now().plusDays(3),LocalDate.now().plusDays(5), null, null,20L);
    }

    @Test
    void shouldReturnNullWhenDtoIsNull(){
        Assertions.assertNull(reservationConverter.toEntity(null));
    }

    @Test
    void shouldConvertToEntity(){
        Reservation reservation1 = reservationConverter.toEntity(reservationDto);

        Assertions.assertEquals(LocalDate.now().plusDays(3),reservation1.getStartDate());
        Assertions.assertEquals(LocalDate.now().plusDays(5),reservation1.getEndDate());
    }

    @Test
    void shouldReturnNullWhenReservationIsNull(){
        Assertions.assertNull(reservationConverter.toDto(null));
    }

    @Test
    void shouldConvertToDto(){
        myUser = new MyUser(10L,"adminb","admin@example.com","123","ROLE_ADMIN",null,null,false);
        car = new Car(20L,"Bmw","M3",2008,4.0,420,"description",null,false,null,null);

        reservation.setCar(car);
        reservation.setUser(myUser);

        ReservationDto reservationDto1 = reservationConverter.toDto(reservation);


        Assertions.assertEquals(10,reservationDto1.getId());
        Assertions.assertEquals(LocalDate.now(),reservationDto1.getStartDate());
        Assertions.assertEquals(LocalDate.now().plusDays(2),reservationDto1.getEndDate());
        Assertions.assertEquals(10,reservationDto1.getUserId());
        Assertions.assertEquals(20,reservationDto1.getCarId());
    }

}
