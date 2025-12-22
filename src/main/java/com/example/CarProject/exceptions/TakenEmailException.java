package com.example.CarProject.exceptions;

public class TakenEmailException extends RuntimeException {
    public TakenEmailException() {
        super("User with that email already exists");
    }
}
