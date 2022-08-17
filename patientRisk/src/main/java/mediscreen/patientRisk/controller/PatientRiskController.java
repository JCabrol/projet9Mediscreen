package mediscreen.patientRisk.controller;

import mediscreen.patientRisk.model.DiabetesRisk;
import mediscreen.patientRisk.proxy.MedicalNoteProxy;
import mediscreen.patientRisk.proxy.PatientInformationProxy;
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


    @GetMapping("/assess/id")
    public ResponseEntity<String> getAssessById(@RequestParam int patId) {
        return new ResponseEntity<>(patientRiskService.getAssessById(patId), HttpStatus.OK);
    }

    @GetMapping("/assess/familyName")
    public ResponseEntity<List<String>> getAssessByName(@RequestParam String familyName) {
        return new ResponseEntity<>(patientRiskService.getAssessByName(familyName), HttpStatus.OK);
    }

    @GetMapping("/diabetesRisk/{id}")
    public ResponseEntity<DiabetesRisk> getDiabetesRisk(@PathVariable int id) {
        return new ResponseEntity<>(patientRiskService.getDiabetesRiskById(id), HttpStatus.OK);
    }
}
