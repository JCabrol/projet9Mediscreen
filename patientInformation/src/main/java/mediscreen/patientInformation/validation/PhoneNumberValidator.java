package mediscreen.patientInformation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.equals("")) {
            return true;
        } else {
            Pattern patternPhoneNumber = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
            return patternPhoneNumber.matcher(value).find()&& value.chars().count()==12;
        }
    }
}
