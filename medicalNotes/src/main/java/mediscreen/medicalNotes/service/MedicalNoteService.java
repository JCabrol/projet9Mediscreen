package mediscreen.medicalNotes.service;

import mediscreen.medicalNotes.exception.ObjectNotFoundException;
import mediscreen.medicalNotes.model.MedicalNote;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MedicalNoteService {


    List<MedicalNote> getAllPatientNotes(String patientId);

    MedicalNote getMedicalNote(String id) throws ObjectNotFoundException;

    String getAllContentByPatient(String patientId);

    void addMedicalNote(String patientId, String content);

    void updateMedicalNote(String id, String content);

    void deleteMedicalNote(String id);
}
