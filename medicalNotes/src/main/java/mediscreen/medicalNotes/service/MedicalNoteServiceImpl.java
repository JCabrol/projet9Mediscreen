package mediscreen.medicalNotes.service;

import mediscreen.medicalNotes.exception.ObjectNotFoundException;
import mediscreen.medicalNotes.model.MedicalNote;
import mediscreen.medicalNotes.repository.MedicalNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalNoteServiceImpl implements MedicalNoteService {

    @Autowired
    MedicalNoteRepository medicalNoteRepository;

    /**
     * Get the list of all registered medical notes for a patient, ordered by date.
     * If there is no registered medical note for the selected patient, the function returns an empty list.
     *
     * @return a list of MedicalNotes objects
     */
    @Override
    public List<MedicalNote> getAllPatientNotes(String patientId) {
        return medicalNoteRepository.findByPatientIdOrderByNoteDateDesc(patientId);
    }

    /**
     * Get a medical note by its id.
     *
     * @param id the id of the researched medical note
     * @return a MedicalNote object
     * @throws ObjectNotFoundException if no medical note is found with this id
     */
    @Override
    public MedicalNote getMedicalNote(String id) throws ObjectNotFoundException {
        Optional<MedicalNote> medicalNoteOptional = medicalNoteRepository.findById(id);
        if (medicalNoteOptional.isPresent()) {
            return medicalNoteOptional.get();
        } else {
            throw new ObjectNotFoundException("The medical note number " + id + " was not found.");
        }
    }

    /**
     * Get all the contents from all the notes about a patient.
     * All contents are concatenated in a single String.
     * If there is not any note for the patient, returns an empty String.
     * This function is useful for searching trigger terms inside.
     *
     * @param patientId the id of the patient whose contents are sought
     * @return a String containing all contents about the patient.
     */
    @Override
    public String getAllContentByPatient(String patientId) {
        List<MedicalNote> medicalNoteList = getAllPatientNotes(patientId);

        StringBuilder result = new StringBuilder();
        for(MedicalNote medicalNote:medicalNoteList) {result.append(" ").append(medicalNote.getNoteContent());}
        return result.toString();
    }

    /**
     * Create a new medical note from a patient id and a content. Sets automatically the date.
     *
     * @param patientId the id of the patient to whom a new medical note is created
     * @param content the content of the note
     */
    @Override
    public void addMedicalNote(String patientId, String content)
    {
       MedicalNote medicalNote = new MedicalNote();
       medicalNote.setPatientId(patientId);
       medicalNote.setNoteDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
       medicalNote.setNoteContent(content);
       medicalNoteRepository.save(medicalNote);
    }

    /**
     * Update an existing medical note.
     *
     * @param id  the id of the note to update
     * @param content the content to update in the note
     * @throws ObjectNotFoundException if the note is not found by its id
     */
    @Override
    public void updateMedicalNote(String id, String content) throws ObjectNotFoundException
    {
        MedicalNote medicalNote = getMedicalNote(id);
        medicalNote.setNoteContent(content);
        medicalNoteRepository.save(medicalNote);
    }

    /**
     * Delete - Delete a medical note.
     *
     * @param id the id of the medical note to delete
     * @throws ObjectNotFoundException if the note is not found by its id
     */
    @Override
    public void deleteMedicalNote(String id)
    {
        MedicalNote medicalNote = getMedicalNote(id);
        medicalNoteRepository.delete(medicalNote);
    }
}
