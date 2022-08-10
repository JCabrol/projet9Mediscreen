package mediscreen.patientInformation.controller;

import lombok.extern.slf4j.Slf4j;
import mediscreen.patientInformation.modele.PatientDTO;
import mediscreen.patientInformation.modele.InfoPatientToUpdateDTO;
import mediscreen.patientInformation.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class PatientController {

    @Autowired
    PatientService patientService;

    /**
     * Read - Get the list of all registered patients, ordered by family name. If there is no registered patient returns an empty list.
     *
     * @return a list of PatientDTO objects
     */
    @GetMapping("/patient/getAll")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> allPatients = patientService.getAllPatients();
        return new ResponseEntity<>(allPatients, HttpStatus.OK);
    }

    /**
     * Read - Get a patient by its id.
     *
     * @param patientId the id of the researched patient
     * @return a PatientDTO object
     */
    @GetMapping("/patient/getPatient/{patientId}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable int patientId) {
        PatientDTO patient = patientService.getPatientById(patientId);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    /**
     * Read - Get a list of patients by name.
     * The method search by familyName if there is only family name in parameter,
     * by givenName if there is only givenName in parameter,
     * by family name and given name if both parameters are present.
     * If there are several corresponding results, all are returned in the result list.
     * If there is not any corresponding result, an empty list is returned.
     *
     * @param familyName the familyName of the researched patient
     * @param givenName the givenName of the researched patient
     * @return a list of PatientDTO objects
     */
    @GetMapping("/patient/getPatient")
    public ResponseEntity<List<PatientDTO>> getPatientsByName(@RequestParam(required = false) String familyName,
                                                              @RequestParam(required = false) String givenName) {
        List<PatientDTO> patientsFound = patientService.getPatientsByName(familyName, givenName);
        return new ResponseEntity<>(patientsFound, HttpStatus.OK);
    }

    /**
     * Create - Create a new patient.
     *
     * @param patientDTO a PatientDTO object containing all information to create new patient
     * @return a success message to indicate the patient has been created
     */
    @PostMapping("/patient/add")
    public ResponseEntity<String> addNewPatient(@Valid @RequestBody PatientDTO patientDTO) {
        patientService.addNewPatient(patientDTO);
        String message = "The patient named " + patientDTO.getFamilyName() + " " + patientDTO.getGivenName() + " has been created.";
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    /**
     * Update - Update an existing patient.
     *
     * @param patientId the id of the patient to update
     * @param infoPatientToUpdateDTO an InfoPatientToUpdateDTO object containing all new information to update a patient
     * @return a success message to indicate the patient has been updated
     */
    @PutMapping("/patient/update/{patientId}")
    public ResponseEntity<String> updatePatient(@PathVariable int patientId,@Valid @RequestBody InfoPatientToUpdateDTO infoPatientToUpdateDTO) {
        patientService.updatePatient(patientId, infoPatientToUpdateDTO);
        String message = "The patient number " + patientId + " has been updated.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Delete - Delete a patient.
     *
     * @param patientId the id of the patient to delete
     * @return a success message to indicate the patient has been deleted
     */
    @DeleteMapping("/patient/delete/{patientId}")
    public ResponseEntity<String> deletePatient(@PathVariable int patientId) {
        patientService.deletePatient(patientId);
        String message = "The patient " + patientId + " has been deleted.";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
