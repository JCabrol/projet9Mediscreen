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

    /**
     * Get a patient by id
     *
     * @param patientId the id of the researched patient
     * @return a patientBean object containing information about the patient
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    @Override
    public PatientBean getPatientById(int patientId) throws ObjectNotFoundException{
        PatientBean patientBean;
        try {
            patientBean = patientInformationProxy.getPatientById(patientId);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found.");
        }
        return patientBean;
    }

    /**
     * Get the list of all patient. If there is not any patient returns empty list
     *
     * @return a list of patientBean object containing information about patients
     */
    @Override
    public List<PatientBean> getAllPatient() {
        return patientInformationProxy.getAllPatient();
    }

    /**
     * Get the list of all patient having the researched name. If there is not any corresponding patient returns empty list
     *
     * @return a list of patientBean object containing information about corresponding patients
     */
    @Override
    public List<PatientBean> getPatientByName(String familyName, String givenName) {
        return patientInformationProxy.getPatientByName(familyName, givenName);
    }

    /**
     * Add a new patient
     *
     * @param patientBean  the patient to add
     */
    @Override
    public void addNewPatient(PatientBean patientBean) {
        patientInformationProxy.addNewPatient(patientBean);
    }

    /**
     * Update a patient
     *
     * @param patientId the id of the patient to update
     * @param patientBean the information patient to update
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    @Override
    public void updatePatient(int patientId, PatientBean patientBean) throws ObjectNotFoundException{
        try {
            patientInformationProxy.updatePatient(patientId, patientBean);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found.");
        }
    }

    /**
     * Delete a patient
     *
     * @param patientId the id of the patient to delete
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    @Override
    public void deletePatient(int patientId) throws ObjectNotFoundException {
        try {
            patientInformationProxy.deletePatient(patientId);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found.");
        }
    }

    /**
     * Get the diabetes risk for a patient
     *
     * @param patientId the id of the patient whose diabetes risk is sought
     * @return a String indicating the diabetes risk for the patient
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    @Override
    public String getDiabetesRisk(int patientId) throws ObjectNotFoundException {
        String result;
        try {
            result = patientRiskProxy.getDiabetesRisk(patientId).name();
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found.");
        }
        return result;
    }

    /**
     * Get a medical note by its id
     *
     * @param noteId the id of researched medical note
     * @return a medicalNoteBean object containing all information from the note
     * @throws ObjectNotFoundException if the medicalNote is not found by its id
     */
    @Override
    public MedicalNoteBean getMedicalNote(String noteId) throws ObjectNotFoundException{
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

    /**
     * Get the list of all medical notes about a patient. If there is not any note, returns an empty list
     *
     * @param patientId the id of the patient whose medical notes are sought
     * @return a list of medicalNoteBean objects containing all information from the notes
     */
    @Override
    public List<MedicalNoteBean> getMedicalNotesByPatient(String patientId) {
        return medicalNoteProxy.getMedicalNotesByPatient(patientId);
    }

    /**
     * Update a medical note
     *
     * @param noteId the id of the note to update
     * @param noteContent the content to update
     * @throws ObjectNotFoundException if the note is not found by its id
     */
    @Override
    public void updateMedicalNote(String noteId, String noteContent) throws ObjectNotFoundException{
        try {
            medicalNoteProxy.updateMedicalNote(noteId, noteContent);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The medical note with id " + noteId + " was not found.");
        }
    }

    /**
     * Add a medical note
     *
     * @param patientId the id of the patient to whom note is added
     * @param noteContent the content of the new note
     */
    @Override
    public void addMedicalNote(String patientId, String noteContent) {
        medicalNoteProxy.addMedicalNote(patientId, noteContent);
    }

    /**
     * Delete a medicalNote by its id
     *
     * @param noteId the id of note to delete
     * @throws ObjectNotFoundException if the note is not found by its id
     */
    @Override
    public void deleteMedicalNote(String noteId) throws ObjectNotFoundException {
        try {
            medicalNoteProxy.deleteMedicalNote(noteId);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The medical note with id " + noteId + " was not found.");
        }
    }

    /**
     * Replace the content of medicalNotes in a list of medicalNotes by a preview limited at 90 characters and without tags
     *
     * @param medicalNoteList a list of medicalNoteBean
     */
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

    /**
     * Calculate the elements of a list to display in function of the list's size, the number of elements by page and the active page
     *
     * @param pageNumber the active page
     * @param numberOfPatientByPage the number of elements to display in a page
     * @param patientList the list of elements
     * @return a ListOfPatientsToDisplay object containing the sublist to display, the active page, the total number of pages and the number of the pages to display in the pagination
     */
    @Override
    public ListOfPatientsToDisplay getPatientsToDisplay(int pageNumber, int numberOfPatientByPage, List<PatientBean> patientList) {
        int totalOfElements = patientList.size();
        HashMap<String, Integer> elementsForList = getPaginatedElementList(totalOfElements, pageNumber, numberOfPatientByPage);
        patientList = patientList.subList(elementsForList.get("firstListElement"), elementsForList.get("lastListElement"));
        int totalNumberOfPages = elementsForList.get("totalNumberOfPages");
        Integer[] pagesToDisplay = getPagesToDisplay(totalNumberOfPages, pageNumber);
        return new ListOfPatientsToDisplay(patientList, pageNumber, totalNumberOfPages, pagesToDisplay);
    }

    /**
     * Calculate the elements of a list to display in function of the list's size, the number of elements by page and the active page
     *
     * @param pageNumber the active page
     * @param numberOfNotesByPage the number of elements to display in a page
     * @param medicalNoteList the list of elements
     * @return a ListOfNotesToDisplay object containing the sublist to display, the active page, the total number of pages and the number of the pages to display in the pagination
     */
    @Override
    public ListOfNotesToDisplay getMedicalNotesToDisplay(int pageNumber, int numberOfNotesByPage, List<MedicalNoteBean> medicalNoteList) {
        int totalOfElements = medicalNoteList.size();
        HashMap<String, Integer> elementsForList = getPaginatedElementList(totalOfElements, pageNumber, numberOfNotesByPage);
        medicalNoteList = medicalNoteList.subList(elementsForList.get("firstListElement"), elementsForList.get("lastListElement"));
        int totalNumberOfPages = elementsForList.get("totalNumberOfPages");
        Integer[] pagesToDisplay = getPagesToDisplay(totalNumberOfPages, pageNumber);
        return new ListOfNotesToDisplay(medicalNoteList, pageNumber, totalNumberOfPages, pagesToDisplay);
    }

    /**
     * Calculate the first and last elements to display in a list, and the total number of pages
     *
     * @param totalOfElements the number of elements in the list
     * @param pageNumber the active page
     * @param numberOfElementsByPage the number of elements to display in a page
     * @return a Hashmap containing the rank of first and last elements to display and the total number of pages
     */
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

    /**
     * Calculate the numbers of the pages to display in pagination
     *
     * @param totalNumberOfPage the total number of pages
     * @param pageNumber the active page
     * @return an array containing the 3 numbers to display
     */
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
                    for (int i = 0; i < 3; i++) {
                        pagesToDisplay[i] = i + pageNumber - 2;
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
