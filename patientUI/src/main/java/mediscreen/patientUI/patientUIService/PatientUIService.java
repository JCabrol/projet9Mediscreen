package mediscreen.patientUI.patientUIService;

import mediscreen.patientUI.bean.MedicalNoteBean;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.modele.ListOfNotesToDisplay;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientUIService {

    PatientBean getPatientById(int patientId);

    List<PatientBean> getAllPatient();

    List<PatientBean> getPatientByName(String familyName, String givenName);

    void addNewPatient(PatientBean patientBean);

    void updatePatient(int patientId, PatientBean patientBean);

    void deletePatient(int patientId);

    String getDiabetesRisk(int patientId);

    MedicalNoteBean getMedicalNote(String noteId);

    List<MedicalNoteBean> getMedicalNotesByPatient(String patientId);

    void updateMedicalNote(String noteId, String noteContent);

    void addMedicalNote(String patientId, String noteContent);

    void deleteMedicalNote(String noteId);

    List<MedicalNoteBean> createPreviewContentList(List<MedicalNoteBean> medicalNoteList);

    ListOfPatientsToDisplay getPatientsToDisplay(int pageNumber, int numberOfPatientByPage, List<PatientBean> patientList);

    ListOfNotesToDisplay getMedicalNotesToDisplay(int pageNumber, int numberOfNotesByPage, List<MedicalNoteBean> medicalNoteList);
}
