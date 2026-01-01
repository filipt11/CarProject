package com.example.CarProject.utils;

import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Reservation;
import com.example.CarProject.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.CarProject.repositories.MyUserRepository;

@Component
public class ReservationConverter {

    @Autowired
    private MyUserRepository usersRepository;

    @Autowired
    private CarRepository carRepository;

    public Reservation toEntity(ReservationDto dto){
        if(dto==null){
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setUser(usersRepository.findById(1L).orElse(null));
        reservation.setCar(carRepository.findById(dto.getCarId()).orElse(null));

        return reservation;
    }

    public ReservationDto toDto(Reservation reservation){
        if(reservation==null){
            return null;
        }

        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setStartDate(reservation.getStartDate());
        dto.setEndDate(reservation.getEndDate());
        dto.setUserId(reservation.getUser().getId());
        dto.setCarId(reservation.getCar().getId());

        return dto;
    }

}
