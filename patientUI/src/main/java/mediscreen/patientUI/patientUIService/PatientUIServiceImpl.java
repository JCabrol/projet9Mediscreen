package mediscreen.patientUI.patientUIService;

import feign.FeignException;
import mediscreen.patientUI.bean.MedicalNoteBean;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.exception.ObjectNotFoundException;
import mediscreen.patientUI.modele.ListOfNotesToDisplay;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import mediscreen.patientUI.proxy.MedicalNoteProxy;
import mediscreen.patientUI.proxy.PatientInformationProxy;
import mediscreen.patientUI.proxy.PatientRiskProxy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PatientUIServiceImpl implements PatientUIService {

    private final PatientInformationProxy patientInformationProxy;
    private final MedicalNoteProxy medicalNoteProxy;
    private final PatientRiskProxy patientRiskProxy;

    public PatientUIServiceImpl(PatientInformationProxy patientInformationProxy, MedicalNoteProxy medicalNoteProxy, PatientRiskProxy patientRiskProxy) {
        this.patientInformationProxy = patientInformationProxy;
        this.medicalNoteProxy = medicalNoteProxy;
        this.patientRiskProxy = patientRiskProxy;
    }

    @Override
    public PatientBean getPatientById(int patientId) {
        PatientBean patientBean;
        try {
            patientBean = patientInformationProxy.getPatientById(patientId);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found.");
        }
        return patientBean;
    }

    @Override
    public List<PatientBean> getAllPatient() {
        return patientInformationProxy.getAllPatient();
    }

    @Override
    public List<PatientBean> getPatientByName(String familyName, String givenName) {
        return patientInformationProxy.getPatientByName(familyName, givenName);
    }

    @Override
    public void addNewPatient(PatientBean patientBean) {
        patientInformationProxy.addNewPatient(patientBean);
    }

    @Override
    public void updatePatient(int patientId, PatientBean patientBean) {
        try {
            patientInformationProxy.updatePatient(patientId, patientBean);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found.");
        }
    }

    @Override
    public void deletePatient(int patientId) {
        try {
            patientInformationProxy.deletePatient(patientId);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found.");
        }
    }

    @Override
    public String getDiabetesRisk(int patientId) {
        String result;
        try {
            result = patientRiskProxy.getDiabetesRisk(patientId).name();
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found.");
        }
        return result;
    }

    @Override
    public MedicalNoteBean getMedicalNote(String noteId) {
        MedicalNoteBean result = null;
        if (noteId != null) {
            try {
                result = medicalNoteProxy.getMedicalNote(noteId);
            } catch (FeignException.FeignClientException e) {
                throw new ObjectNotFoundException("The medical note with id " + noteId + " was not found.");
            }
        }
        return result;
    }

    @Override
    public List<MedicalNoteBean> getMedicalNotesByPatient(String patientId) {
        return medicalNoteProxy.getMedicalNotesByPatient(patientId);
    }

    @Override
    public void updateMedicalNote(String noteId, String noteContent) {
        try {
            medicalNoteProxy.updateMedicalNote(noteId, noteContent);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The medical note with id " + noteId + " was not found.");
        }
    }

    @Override
    public void addMedicalNote(String patientId, String noteContent) {
        medicalNoteProxy.addMedicalNote(patientId, noteContent);
    }

    @Override
    public void deleteMedicalNote(String noteId) {
        try {
            medicalNoteProxy.deleteMedicalNote(noteId);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The medical note with id " + noteId + " was not found.");
        }
    }

    @Override
    public List<MedicalNoteBean> createPreviewContentList(List<MedicalNoteBean> medicalNoteList) {
        for (MedicalNoteBean medicalNote : medicalNoteList) {
            String preview = "";
            if (medicalNote.getNoteContent() != null) {
                preview = medicalNote.getNoteContent().replaceAll("<[^>]*>", " ");
                if (preview.chars().count() > 90) {
                    preview = preview.substring(0, 87) + "...";
                }
            }
            medicalNote.setNoteContent(preview);
        }
        return medicalNoteList;
    }

    @Override
    public ListOfPatientsToDisplay getPatientsToDisplay(int pageNumber, int numberOfPatientByPage, List<PatientBean> patientList) {
        int totalOfElements = patientList.size();
        HashMap<String, Integer> elementsForList = getPaginatedElementList(totalOfElements, pageNumber, numberOfPatientByPage);
        patientList = patientList.subList(elementsForList.get("firstListElement"), elementsForList.get("lastListElement"));
        int totalNumberOfPages = elementsForList.get("totalNumberOfPages");
        Integer[] pagesToDisplay = getPagesToDisplay(totalNumberOfPages, pageNumber);
        return new ListOfPatientsToDisplay(patientList, pageNumber, totalNumberOfPages, pagesToDisplay);
    }

    @Override
    public ListOfNotesToDisplay getMedicalNotesToDisplay(int pageNumber, int numberOfNotesByPage, List<MedicalNoteBean> medicalNoteList) {
        int totalOfElements = medicalNoteList.size();
        HashMap<String, Integer> elementsForList = getPaginatedElementList(totalOfElements, pageNumber, numberOfNotesByPage);
        medicalNoteList = medicalNoteList.subList(elementsForList.get("firstListElement"), elementsForList.get("lastListElement"));
        int totalNumberOfPages = elementsForList.get("totalNumberOfPages");
        Integer[] pagesToDisplay = getPagesToDisplay(totalNumberOfPages, pageNumber);
        return new ListOfNotesToDisplay(medicalNoteList, pageNumber, totalNumberOfPages, pagesToDisplay);
    }

    private HashMap<String, Integer> getPaginatedElementList(int totalOfElements, int pageNumber, int numberOfElementsByPage) {

        int totalNumberOfPage = totalOfElements / numberOfElementsByPage;
        int rest = totalOfElements % numberOfElementsByPage;
        if (rest != 0) {
            totalNumberOfPage++;
        }

        int firstListElement;
        int lastListElement;

        if (totalOfElements < numberOfElementsByPage) {
            firstListElement = 0;
            lastListElement = totalOfElements;
        } else {
            lastListElement = pageNumber * numberOfElementsByPage;
            firstListElement = lastListElement - numberOfElementsByPage;
            if (lastListElement > totalOfElements) {
                lastListElement = totalOfElements;
            }
        }

        HashMap<String, Integer> result = new HashMap<>();
        result.put("totalNumberOfPages", totalNumberOfPage);
        result.put("firstListElement", firstListElement);
        result.put("lastListElement", lastListElement);

        return result;
    }

    private Integer[] getPagesToDisplay(int totalNumberOfPage, int pageNumber) {
        Integer[] pagesToDisplay = new Integer[3];
        if (totalNumberOfPage < 3) {
            for (int i = 0; i < totalNumberOfPage; i++) {
                pagesToDisplay[i] = i + 1;
            }
        } else {
            if (pageNumber == 1) {
                for (int i = 0; i < 3; i++) {
                    pagesToDisplay[i] = i + 1;
                }
            } else {
                if (pageNumber == totalNumberOfPage) {
                    for (int i = totalNumberOfPage - 1; i >= totalNumberOfPage - 3; i--) {
                        pagesToDisplay[i] = i + 1;
                    }
                } else {

                    for (int i = 0; i < 3; i++) {
                        pagesToDisplay[i] = i + pageNumber - 1;

                    }
                }
            }
        }
        return pagesToDisplay;
    }

}
