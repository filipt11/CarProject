package com.example.CarProject.exceptions;

public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException(){
        super("Car not found");
    }
}
