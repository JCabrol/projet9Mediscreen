package mediscreen.patientUI.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediscreen.patientUI.validation.ValidDateOfBirth;
import mediscreen.patientUI.validation.ValidPhone;
import mediscreen.patientUI.validation.ValidSex;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientBean {

    private int patientId;

    @Size(max = 50, message = "Family name cannot be over 50 characters.")
    @NotBlank(message = "Please enter a family name.")
    private String familyName;

    @Size(max = 50, message = "Given name cannot be over 50 characters.")
    @NotBlank(message = "Please enter a given name.")
    private String givenName;

    @NotBlank(message = "Please enter a date of birth.")
    @ValidDateOfBirth
    private String dateOfBirth;

    @ValidSex
    private String sex;

    @ValidPhone
    private String phone;

    @Size(max = 256, message = "Address cannot be over 256 characters.")
    private String address;

    private int age;
}
