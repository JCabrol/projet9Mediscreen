package mediscreen.patientInformation.modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediscreen.patientInformation.validation.ValidDateOfBirth;
import mediscreen.patientInformation.validation.ValidPhone;
import mediscreen.patientInformation.validation.ValidSex;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private int patientId;

    @Size(max = 50, message = "Family name cannot be over 50 characters.")
    @NotBlank(message = "Family name is mandatory.")
    private String familyName;

    @Size(max = 50, message = "Given name cannot be over 50 characters.")
    @NotBlank(message = "Given name is mandatory.")
    private String givenName;

    @NotBlank(message = "Date of birth is mandatory.")
    @ValidDateOfBirth
    private String dateOfBirth;

    @NotBlank(message = "Please enter \"M\" or \"F\" for sex.")
    @ValidSex
    private String sex;

    @ValidPhone
    private String phone;

    @Size(max = 256, message = "Address cannot be over 256 characters.")
    private String address;

    private int age;

    public PatientDTO(String familyName, String givenName, String dateOfBirth, String sex, String phone, String address) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.phone = phone;
        this.address = address;
    }

    public PatientDTO(String familyName, String givenName, String dateOfBirth, String sex) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }
}

