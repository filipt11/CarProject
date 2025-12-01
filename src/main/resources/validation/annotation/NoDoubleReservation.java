import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=ReservationValidator.class)
public @interface NoDoubleReservation {
    String message() default "{mycustom.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}