package dto;

import entities.Car;
import entities.Users;
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

public class ReservationDto {

    @DateTimeFormat(pattern="MM/dd/yyyy")
    @FutureOrPresent
    private LocalDate startDate;

    @DateTimeFormat(pattern="MM/dd/yyyy")
    @FutureOrPresent
    private LocalDate endDate;

    private Users user;

    @NotNull
    private Car car;

}
