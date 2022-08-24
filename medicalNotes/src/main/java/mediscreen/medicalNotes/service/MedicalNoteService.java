package mediscreen.medicalNotes.service;

import mediscreen.medicalNotes.exception.ObjectNotFoundException;
import mediscreen.medicalNotes.model.MedicalNote;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MedicalNoteService {

    /**
     * Get the list of all registered medical notes for a patient, ordered by date.
     * If there is no registered medical note for the selected patient, the function returns an empty list.
     *
     * @return a list of MedicalNotes objects
     */
    List<MedicalNote> getAllPatientNotes(String patientId);

    /**
     * Get a medical note by its id.
     *
     * @param id the id of the researched medical note
     * @return a MedicalNote object
     * @throws ObjectNotFoundException if no medical note is found with this id
     */
    MedicalNote getMedicalNote(String id) throws ObjectNotFoundException;

    /**
     * Get all the contents from all the notes about a patient.
     * All contents are concatenated in a single String.
     * If there is not any note for the patient, returns an empty String.
     * This function is useful for searching trigger terms inside.
     *
     * @param patientId the id of the patient whose contents are sought
     * @return a String containing all contents about the patient.
     */
    String getAllContentByPatient(String patientId);

    /**
     * Create a new medical note from a patient id and a content. Sets automatically the date.
     *
     * @param patientId the id of the patient to whom a new medical note is created
     * @param content the content of the note
     */
    void addMedicalNote(String patientId, String content);

    /**
     * Update an existing medical note.
     *
     * @param id  the id of the note to update
     * @param content the content to update in the note
     * @throws ObjectNotFoundException if the note is not found by its id
     */
    void updateMedicalNote(String id, String content);

    /**
     * Delete - Delete a medical note.
     *
     * @param id the id of the medical note to delete
     * @throws ObjectNotFoundException if the note is not found by its id
     */
    void deleteMedicalNote(String id);
}
