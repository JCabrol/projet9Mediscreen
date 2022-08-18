package mediscreen.patientRisk.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientBean {

    private int patientId;
    private String familyName;
    private String givenName;
    private String dateOfBirth;
    private String sex;
    private String phone;
    private String address;
    private int age;
}
