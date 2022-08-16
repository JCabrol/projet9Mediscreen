package mediscreen.medicalNotes.controller;

import lombok.extern.slf4j.Slf4j;
import mediscreen.medicalNotes.model.MedicalNote;
import mediscreen.medicalNotes.service.MedicalNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class MedicalNoteController {
    
    @Autowired
    MedicalNoteService medicalNoteService;

    /**
     * Read - Get the list of all registered notes for a patient, ordered by date. If there is no registered notes returns an empty list.
     *
     * @param patientId the id of the patient whose medical notes are researched
     * @return a list of MedicalNote objects
     */
    @GetMapping("/patHistory/getAllMedicalNotes/{patientId}")
    public ResponseEntity<List<MedicalNote>> getMedicalNotesByPatient(@PathVariable String patientId) {
        List<MedicalNote> allMedicalNotes = medicalNoteService.getAllPatientNotes(patientId);
        return new ResponseEntity<>(allMedicalNotes, HttpStatus.OK);
    }

    /**
     * Read - Get a medical note by its id, returns only date and content
     *
     * @param id the id of the researched note
     * @return a projection of the researched medical note, displaying date and content
     */
    @GetMapping("/patHistory/getMedicalNote/{id}")
    public ResponseEntity<MedicalNote> getMedicalNote(@PathVariable String id) {
        MedicalNote medicalNote = medicalNoteService.getMedicalNote(id);
        return new ResponseEntity<>(medicalNote, HttpStatus.OK);
    }

    /**
     * Read - Get a string containing all information from all medical notes about one patient
     *
     * @param patientId the id of the patient whose information is sought
     * @return a String containing all information
     */
    @GetMapping("/patHistory/getAllInformation/{patientId}")
    public ResponseEntity<String> getAllInformation(@PathVariable String patientId) {
        String result = medicalNoteService.getAllContentByPatient(patientId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Create - Create a medical note from given information (concerned patient and list of contents)
     *
     * @param patientId the id of the patient to which a note is added
     * @param content the content of the note
     * @return an information message indicating the medical note have been created
     */
    @PostMapping("/patHistory/add/{patientId}")
    public ResponseEntity<String> addMedicalNote(@PathVariable String patientId, @RequestBody String content) {
       medicalNoteService.addMedicalNote(patientId, content);
       String result = "A medical note have been added to the patient number " + patientId + ".";
       log.info(result);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Update - Update a medical note with and updated list of contents
     *
     * @param noteId the id of the note to update
     * @param content the updated content to put in the concerned note
     * @return an information message indicating the medical note have been updated
     */
    @PutMapping("/patHistory/update/{noteId}")
    public ResponseEntity<String> updateMedicalNote(@PathVariable String noteId, @RequestBody String content) {
        medicalNoteService.updateMedicalNote(noteId, content);
        String result = "The medical note number " + noteId + " have been updated.";
        log.info(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete - Delete a medical note from its id
     *
     * @param noteId the id of the note to delete
     * @return an information message indicating the medical note have been deleted
     */
    @DeleteMapping("/patHistory/delete/{noteId}")
    public ResponseEntity<String> deleteMedicalNote(@PathVariable String noteId) {
        medicalNoteService.deleteMedicalNote(noteId);
        String result = "The medical note number " + noteId + " have been deleted.";
        log.info(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
