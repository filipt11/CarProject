package com.example.CarProject.exceptions;

public class InvalidReservationDateException extends RuntimeException {
    public InvalidReservationDateException() {
        super("Invalid Reservation Date");
    }
}
