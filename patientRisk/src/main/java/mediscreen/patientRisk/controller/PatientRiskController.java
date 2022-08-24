package mediscreen.patientRisk.controller;

import mediscreen.patientRisk.model.DiabetesRisk;
import mediscreen.patientRisk.service.PatientRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PatientRiskController {

    @Autowired
    private PatientRiskService patientRiskService;

    /**
     * Read - Get a diabetes risk for a patient by its id.
     *
     * @param patId the id of the patient whose risk is calculated
     * @return a String containing the patient's name, age and diabetes risk
     */
    @GetMapping("/assess/id")
    public ResponseEntity<String> getAssessById(@RequestParam int patId) {
        return new ResponseEntity<>(patientRiskService.getAssessById(patId), HttpStatus.OK);
    }

    /**
     * Read - Get a diabetes risk for patients by its family name.
     * If there are several patients having the same name, returns diabetes risk for all patients.
     *
     * @param familyName the researched family name
     * @return a list of String containing the patient's name, age and diabetes risk for each patient having the researched name
     */
    @GetMapping("/assess/familyName")
    public ResponseEntity<List<String>> getAssessByName(@RequestParam String familyName) {
        return new ResponseEntity<>(patientRiskService.getAssessByName(familyName), HttpStatus.OK);
    }

    /**
     * Read - Get a diabetes risk for a patient by its id.
     *
     * @param id the id of the patient whose risk is calculated
     * @return the DiabetesRisk object corresponding to the patient's situation
     */
    @GetMapping("/diabetesRisk/{id}")
    public ResponseEntity<DiabetesRisk> getDiabetesRisk(@PathVariable int id) {
        return new ResponseEntity<>(patientRiskService.getDiabetesRiskById(id), HttpStatus.OK);
    }
}
