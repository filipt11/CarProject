import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


public class MyCustomeValidator implements
        ConstraintValidator<NoDoubleReservation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

    }
}