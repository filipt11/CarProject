package com.example.CarProject.exceptions;

public class TakenUsernameException extends RuntimeException {
    public TakenUsernameException() {
        super("User with that username already exists");
    }
}
