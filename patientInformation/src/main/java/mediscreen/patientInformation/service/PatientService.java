package mediscreen.patientInformation.service;

import mediscreen.patientInformation.exception.ObjectNotFoundException;
import mediscreen.patientInformation.modele.PatientDTO;
import mediscreen.patientInformation.modele.InfoPatientToUpdateDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientService {

    /**
     * Get the list of all registered patients, ordered by family name. If there is no registered patient returns an empty list.
     *
     * @return a list of PatientDTO objects
     */
    List<PatientDTO> getAllPatients();

    /**
     * Get a patient by its id.
     *
     * @param id the id of the researched patient
     * @throws ObjectNotFoundException if no patient is found with this id
     * @return a PatientDTO object
     */
    PatientDTO getPatientById(Integer id) throws ObjectNotFoundException;

    /**
     * Get a list of patients by name.
     * The method search by familyName if givenName is null, by givenName if familyName is null, by family name and given name if none parameter is null.
     * If there are several corresponding results, all are returned in the result list.
     * If there is not any corresponding result, an empty list is returned.
     *
     * @param familyName the familyName of the researched patient
     * @param givenName the givenName of the researched patient
     * @return a list of PatientDTO objects
     */
    List<PatientDTO> getPatientsByName(String familyName, String givenName);

    /**
     * Create a new patient from information given in patientDTO
     *
     * @param patientDTO a PatientDTO object containing all information to create new patient
     */
    void addNewPatient(PatientDTO patientDTO);

    /**
     * Update an existing patient.
     *
     * @param patientId the id of the patient to update
     * @param patientDTO an InfoPatientToUpdateDTO object containing all new information to update a patient
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    void updatePatient(int patientId, InfoPatientToUpdateDTO patientDTO) throws ObjectNotFoundException;

    /**
     * Delete - Delete a patient.
     *
     * @param patientId the id of the patient to delete
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    void deletePatient(int patientId) throws ObjectNotFoundException;
}
