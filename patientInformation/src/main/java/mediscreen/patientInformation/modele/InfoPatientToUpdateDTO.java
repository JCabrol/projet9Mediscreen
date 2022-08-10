package mediscreen.patientInformation.modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediscreen.patientInformation.validation.ValidDateOfBirth;
import mediscreen.patientInformation.validation.ValidPhone;
import mediscreen.patientInformation.validation.ValidSex;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoPatientToUpdateDTO {

    @Size(max = 50, message = "Family name cannot be over 50 characters.")
    private String familyName;

    @Size(max = 50, message = "Given name cannot be over 50 characters.")
    private String givenName;

    @ValidDateOfBirth
    private String dateOfBirth;

    @ValidSex
    private String sex;

    @ValidPhone
    private String phone;

    @Size(max = 256, message = "Address cannot be over 256 characters.")
    private String address;

}

