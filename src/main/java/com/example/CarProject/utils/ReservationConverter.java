package com.example.CarProject.utils;

import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.CarProject.repositories.UsersRepository;

@Component
public class ReservationConverter {

    @Autowired
    private UsersRepository usersRepository;

    public Reservation toEntity(ReservationDto dto){
        if(dto==null){
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setUser(usersRepository.findById(1L).orElse(null));
        reservation.setCar(dto.getCar());

        return reservation;
    }

}
