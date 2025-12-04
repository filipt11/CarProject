package com.example.CarProject.validation;

import com.example.CarProject.annotation.NoDoubleReservation;
import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.repositories.ReservationRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ReservationValidator implements ConstraintValidator<NoDoubleReservation, ReservationDto> {

    private final ReservationRepository reservationRepository;

    public ReservationValidator(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public boolean isValid(ReservationDto dto, ConstraintValidatorContext context) {
        if(dto.getCar() == null || dto.getStartDate() == null || dto.getEndDate() == null) {
            return true;
        }
        boolean exists = reservationRepository.isReservationInTimePeriodExists(
                dto.getCar().getId(), dto.getStartDate(), dto.getEndDate()
        );
        return !exists;

    }
}