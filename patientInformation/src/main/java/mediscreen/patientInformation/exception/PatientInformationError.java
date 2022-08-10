package mediscreen.patientInformation.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientInformationError implements Serializable {
    private static final long serialVersionUID = 1L;
    private HttpStatus status;
    private String message;

    PatientInformationError(HttpStatus status) {
        this.status = status;
    }
}
