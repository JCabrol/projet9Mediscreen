package mediscreen.patientInformation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SexValidator implements ConstraintValidator<ValidSex, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.equals("")) {
            return true;
        } else {
            return (value.equals("M") || value.equals("F"));
        }
    }
}
