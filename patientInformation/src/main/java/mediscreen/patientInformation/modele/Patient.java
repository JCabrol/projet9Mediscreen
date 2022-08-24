package mediscreen.patientInformation.modele;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediscreen.patientInformation.validation.ValidPhone;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "patient_information")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private int patientId;

    @Column(name = "family")
    @NotBlank(message = "Family name is mandatory.")
    @Size(max = 50, message = "Family name cannot be over 50 characters.")
    private String familyName;

    @Column(name = "given")
    @NotBlank(message = "Given name is mandatory.")
    @Size(max = 50, message = "Given name cannot be over 50 characters.")
    private String givenName;

    @Column(name = "dob")
    @NotNull(message = "Date of birth is mandatory.")
    private LocalDate dateOfBirth;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Sex is mandatory.")
    private Sex sex;

    @Column(name = "phone")
    @ValidPhone
    private String phone;

    @Column(name = "address")
    @Size(max = 256, message = "Address cannot be over 256 characters.")
    private String address;


    public Patient(String familyName, String givenName, LocalDate dateOfBirth, Sex sex, String phone, String address) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.phone = phone;
        this.address = address;
    }
}
