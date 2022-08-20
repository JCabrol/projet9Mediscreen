package mediscreen.patientRisk.service;

import mediscreen.patientRisk.exception.ObjectNotFoundException;
import mediscreen.patientRisk.model.DiabetesRisk;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientRiskService {

    String getAssessById(int patId) throws ObjectNotFoundException;

    List<String> getAssessByName(String familyName) throws ObjectNotFoundException;

    DiabetesRisk getDiabetesRiskById(int patId) throws ObjectNotFoundException;
}
