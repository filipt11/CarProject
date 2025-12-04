package com.example.CarProject.repositories;

import com.example.CarProject.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " + "FROM Reservation r " + "WHERE r.car.id = :carId " + "AND r.startDate <= :endDate " + "AND r.endDate >= :startDate")
    boolean isReservationInTimePeriodExists(Long carId, LocalDate startDate, LocalDate endDate);


}
