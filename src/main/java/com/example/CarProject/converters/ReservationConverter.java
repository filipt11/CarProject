package com.example.CarProject.converters;

import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationConverter {

    public Reservation toEntity(ReservationDto dto){
        if(dto==null){
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());

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
