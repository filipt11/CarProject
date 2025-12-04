package com.example.CarProject.annotation;

import com.example.CarProject.validation.ReservationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= ReservationValidator.class)
public @interface NoDoubleReservation {
    String message() default "{mycustom.validation.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}