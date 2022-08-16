package mediscreen.medicalNotes.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedicalNoteError implements Serializable {
    private static final long serialVersionUID = 1L;
    private HttpStatus status;
    private String message;

    MedicalNoteError(HttpStatus status) {
        this.status = status;
    }
}
