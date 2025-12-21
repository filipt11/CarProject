package com.example.CarProject.dto;

import com.example.CarProject.annotation.NoDoubleReservation;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.Users;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@NoDoubleReservation
public class ReservationDto {

    @NotNull(message = "{reservation.start.date}")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    @FutureOrPresent(message = "{reservation.start.date}")
    private LocalDate startDate;

    @NotNull(message = "{reservation.end.date}")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    @FutureOrPresent(message = "{reservation.end.date}")
    private LocalDate endDate;

    private Users user;

    @NotNull(message = "{reservation.empty.car}")
    private Car car;

}
