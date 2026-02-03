package com.example.CarProject.repositories;

import com.example.CarProject.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r " +
            "WHERE r.car.id = :carId " +
            "AND (:excludeId IS NULL OR r.id != :excludeId) " +
            "AND ((r.startDate <= :endDate AND r.endDate >= :startDate))")
    boolean isReservationInTimePeriodExists(Long carId, LocalDate startDate, LocalDate endDate, Long excludeId);

    Optional<Reservation> findByStartDateAndEndDateAndCarId(LocalDate startDate, LocalDate endDate, Long carId);


}
