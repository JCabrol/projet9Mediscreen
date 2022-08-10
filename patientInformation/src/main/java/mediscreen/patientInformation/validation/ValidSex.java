package mediscreen.patientInformation.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SexValidator.class)
public @interface ValidSex {
    String message() default "Please enter \"M\" or \"F\" for sex.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

