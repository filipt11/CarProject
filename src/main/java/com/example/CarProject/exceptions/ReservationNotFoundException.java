package com.example.CarProject.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException() {
        super("Reservation not found");
    }
}
