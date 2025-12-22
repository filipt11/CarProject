package com.example.CarProject.exceptions;

public class PasswordsNotMatchingException extends RuntimeException {
    public PasswordsNotMatchingException() {
        super("Passwords are not matching");
    }
}
