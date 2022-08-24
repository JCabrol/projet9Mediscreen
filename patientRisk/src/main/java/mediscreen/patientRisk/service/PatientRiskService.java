package mediscreen.patientRisk.service;

import mediscreen.patientRisk.exception.ObjectNotFoundException;
import mediscreen.patientRisk.model.DiabetesRisk;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientRiskService {

    /**
     * Get a diabetes risk for a patient by its id.
     *
     * @param patId the id of the patient whose risk is calculated
     * @return a String containing the patient's name, age and diabetes risk
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    String getAssessById(int patId) throws ObjectNotFoundException;

    /**
     * Get a diabetes risk for patients by its family name.
     * If there are several patients having the same name, returns diabetes risk for all patients.
     * If there is not any patient with this name, returns an empty list
     *
     * @param familyName the researched family name
     * @return a list of String containing the patient's name, age and diabetes risk for each patient having the researched name
     * @throws ObjectNotFoundException if there is not any patient with this name
     */
    List<String> getAssessByName(String familyName) throws ObjectNotFoundException;

    /**
     * Get a diabetes risk for a patient by its id.
     *
     * @param patId the id of the patient whose risk is calculated
     * @return the DiabetesRisk object corresponding to the patient's situation
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    DiabetesRisk getDiabetesRiskById(int patId) throws ObjectNotFoundException;
}
