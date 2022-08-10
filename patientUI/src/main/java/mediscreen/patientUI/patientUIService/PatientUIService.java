package mediscreen.patientUI.patientUIService;

import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientUIService {
    ListOfPatientsToDisplay getPatientsToDisplay(int pageNumber, int numberOfPatientByPage, List<PatientBean> patientList);
}
