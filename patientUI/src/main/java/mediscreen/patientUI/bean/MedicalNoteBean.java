package mediscreen.patientUI.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalNoteBean {
    private String id;
    private String patientId;
    private String noteDate;
    private String noteContent;
}
