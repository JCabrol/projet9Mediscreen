package mediscreen.medicalNotes.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "patientNotes")
public class MedicalNote {

    @Id
    private String id;
    @Field("patId")
    private String patientId;
    @Field("noteDate")
    private String noteDate;
    @Field("noteContent")
    private String noteContent;
}
