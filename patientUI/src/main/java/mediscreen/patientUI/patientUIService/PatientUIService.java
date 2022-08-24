package mediscreen.patientUI.patientUIService;

import mediscreen.patientUI.bean.MedicalNoteBean;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.exception.ObjectNotFoundException;
import mediscreen.patientUI.modele.ListOfNotesToDisplay;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientUIService {

    /**
     * Get a patient by id
     *
     * @param patientId the id of the researched patient
     * @return a patientBean object containing information about the patient
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    PatientBean getPatientById(int patientId) throws ObjectNotFoundException;

    /**
     * Get the list of all patient. If there is not any patient returns empty list
     *
     * @return a list of patientBean object containing information about patients
     */
    List<PatientBean> getAllPatient();

    /**
     * Get the list of all patient having the researched name. If there is not any corresponding patient returns empty list
     *
     * @return a list of patientBean object containing information about corresponding patients
     */
    List<PatientBean> getPatientByName(String familyName, String givenName);

    /**
     * Add a new patient
     *
     * @param patientBean  the patient to add
     */
    void addNewPatient(PatientBean patientBean);

    /**
     * Update a patient
     *
     * @param patientId the id of the patient to update
     * @param patientBean the information patient to update
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    void updatePatient(int patientId, PatientBean patientBean) throws ObjectNotFoundException;

    /**
     * Delete a patient
     *
     * @param patientId the id of the patient to delete
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    void deletePatient(int patientId) throws ObjectNotFoundException;

    /**
     * Get the diabetes risk for a patient
     *
     * @param patientId the id of the patient whose diabetes risk is sought
     * @return a String indicating the diabetes risk for the patient
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    String getDiabetesRisk(int patientId) throws ObjectNotFoundException;

    /**
     * Get a medical note by its id
     *
     * @param noteId the id of researched medical note
     * @return a medicalNoteBean object containing all information from the note
     * @throws ObjectNotFoundException if the medicalNote is not found by its id
     */
    MedicalNoteBean getMedicalNote(String noteId) throws ObjectNotFoundException;

    /**
     * Get the list of all medical notes about a patient. If there is not any note, returns an empty list
     *
     * @param patientId the id of the patient whose medical notes are sought
     * @return a list of medicalNoteBean objects containing all information from the notes
     */
    List<MedicalNoteBean> getMedicalNotesByPatient(String patientId);

    /**
     * Update a medical note
     *
     * @param noteId the id of the note to update
     * @param noteContent the content to update
     * @throws ObjectNotFoundException if the note is not found by its id
     */
    void updateMedicalNote(String noteId, String noteContent) throws ObjectNotFoundException;

    /**
     * Add a medical note
     *
     * @param patientId the id of the patient to whom note is added
     * @param noteContent the content of the new note
     */
    void addMedicalNote(String patientId, String noteContent);

    /**
     * Delete a medicalNote by its id
     *
     * @param noteId the id of note to delete
     * @throws ObjectNotFoundException if the note is not found by its id
     */
    void deleteMedicalNote(String noteId) throws ObjectNotFoundException;

    /**
     * Replace the content of medicalNotes in a list of medicalNotes by a preview limited at 90 characters and without tags
     *
     * @param medicalNoteList a list of medicalNoteBean
     */
    List<MedicalNoteBean> createPreviewContentList(List<MedicalNoteBean> medicalNoteList);

    /**
     * Calculate the elements of a list to display in function of the list's size, the number of elements by page and the active page
     *
     * @param pageNumber the active page
     * @param numberOfPatientByPage the number of elements to display in a page
     * @param patientList the list of elements
     * @return a ListOfPatientsToDisplay object containing the sublist to display, the active page, the total number of pages and the number of the pages to display in the pagination
     */
    ListOfPatientsToDisplay getPatientsToDisplay(int pageNumber, int numberOfPatientByPage, List<PatientBean> patientList);

    /**
     * Calculate the elements of a list to display in function of the list's size, the number of elements by page and the active page
     *
     * @param pageNumber the active page
     * @param numberOfNotesByPage the number of elements to display in a page
     * @param medicalNoteList the list of elements
     * @return a ListOfNotesToDisplay object containing the sublist to display, the active page, the total number of pages and the number of the pages to display in the pagination
     */
    ListOfNotesToDisplay getMedicalNotesToDisplay(int pageNumber, int numberOfNotesByPage, List<MedicalNoteBean> medicalNoteList);
}
