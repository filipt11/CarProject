package com.example.CarProject.repositories;


import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    private Car car;

    @BeforeEach
    void init() {
        car = new Car();
        car.setBrand("BMW");
        car.setModel("M3");
        carRepository.save(car);
    }

    @Test
    void shouldReturnTrueWhenReservationOverlaps() {
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setStartDate(LocalDate.of(2025, 1, 10));
        reservation.setEndDate(LocalDate.of(2025, 1, 20));
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.isReservationInTimePeriodExists(car.getId(),LocalDate.of(2025, 1, 15),LocalDate.of(2025, 1, 18),null);

        Assertions.assertTrue(exists);
    }

    @Test
    void shouldReturnTrueWhenReservationOverlaps2() {
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setStartDate(LocalDate.of(2025, 1, 10));
        reservation.setEndDate(LocalDate.of(2025, 1, 20));
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.isReservationInTimePeriodExists(car.getId(),LocalDate.of(2025, 1, 20),LocalDate.of(2025, 1, 30),null);

        Assertions.assertTrue(exists);
    }

    @Test
    void shouldReturnTrueWhenReservationOverlaps3() {
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setStartDate(LocalDate.of(2025, 1, 10));
        reservation.setEndDate(LocalDate.of(2025, 1, 20));
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.isReservationInTimePeriodExists(car.getId(),LocalDate.of(2025, 1, 5),LocalDate.of(2025, 1, 10),null);

        Assertions.assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenNoOverlap() {
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setStartDate(LocalDate.of(2025, 1, 10));
        reservation.setEndDate(LocalDate.of(2025, 1, 20));
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.isReservationInTimePeriodExists(car.getId(),LocalDate.of(2025, 1, 21),LocalDate.of(2025, 1, 25),null);

        Assertions.assertFalse(exists);
    }

    @Test
    void shouldReturnFalseWhenNoOverlap2() {
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setStartDate(LocalDate.of(2025, 1, 10));
        reservation.setEndDate(LocalDate.of(2025, 1, 20));
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.isReservationInTimePeriodExists(car.getId(),LocalDate.of(2025, 1, 4),LocalDate.of(2025, 1, 9),null);

        Assertions.assertFalse(exists);
    }

    @Test
    void shouldIgnoreReservationWhenExcluded() {
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setStartDate(LocalDate.of(2025, 1, 10));
        reservation.setEndDate(LocalDate.of(2025, 1, 20));
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.isReservationInTimePeriodExists(car.getId(),LocalDate.of(2025, 1, 15),LocalDate.of(2025, 1, 18),reservation.getId());

        Assertions.assertFalse(exists);
    }

    @Test
    void shouldReturnFalseForDifferentCar() {
        Car otherCar = new Car();
        otherCar.setBrand("Audi");
        otherCar.setModel("A4");
        carRepository.save(otherCar);

        Reservation reservation = new Reservation();
        reservation.setCar(otherCar);
        reservation.setStartDate(LocalDate.of(2025, 1, 10));
        reservation.setEndDate(LocalDate.of(2025, 1, 20));
        reservationRepository.save(reservation);

        boolean exists = reservationRepository.isReservationInTimePeriodExists(car.getId(),LocalDate.of(2025, 1, 15),LocalDate.of(2025, 1, 18),null);

        Assertions.assertFalse(exists);
    }



}
