package mediscreen.patientUI.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && !Objects.equals(value, "")) {
            int month;
            int day;
            int year;
            Pattern patternDateOfBirth = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

            try {
                month = Integer.parseInt(value.substring(5, 7));
                day = Integer.parseInt(value.substring(8, 10));
                year = Integer.parseInt(value.substring(0, 4));
            } catch (NumberFormatException | StringIndexOutOfBoundsException exception) {
                String errorMessage = context.getDefaultConstraintMessageTemplate() + "The format must be yyyy/mm/dd.";
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(errorMessage)
                        .addConstraintViolation();
                return false;
            }

            boolean patternOk = patternDateOfBirth.matcher(value).find();
            boolean monthOK = month <= 12;
            boolean dayOk = ((List.of(1, 3, 5, 7, 8, 10, 12).contains(month) || !monthOK) && (day <= 31))
                    || ((month == 2) && (day <= 29))
                    || ((List.of(4, 6, 9, 11).contains(month)) && (day <= 30));
            boolean yearOk = year > 1900 && year < 3000;

            String errorMessage = context.getDefaultConstraintMessageTemplate();
            if (!patternOk) {
                errorMessage = errorMessage + "The format must be yyyy/mm/dd.";
            }
            if (!monthOK) {
                errorMessage = errorMessage + " The month cannot be over 12.";
            }
            if (!dayOk) {
                errorMessage = errorMessage + " The number of the day is not correct for this month.";
            }
            if (!yearOk) {
                errorMessage = errorMessage + " The year is not correct.";
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation();

            return patternOk && monthOK && dayOk && yearOk;
        } else {
            return true;
        }
    }
}
