package mediscreen.patientUI.patientUIService;

import mediscreen.patientUI.bean.MedicalNoteBean;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.modele.ListOfNotesToDisplay;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientUIService {

    List<MedicalNoteBean> createPreviewContentList(List<MedicalNoteBean> medicalNoteList);

    ListOfPatientsToDisplay getPatientsToDisplay(int pageNumber, int numberOfPatientByPage, List<PatientBean> patientList);


    ListOfNotesToDisplay getMedicalNotesToDisplay(int pageNumber, int numberOfNotesByPage, List<MedicalNoteBean> medicalNoteList);
}
