package mediscreen.patientRisk.service;

import feign.FeignException;
import mediscreen.patientRisk.bean.PatientBean;
import mediscreen.patientRisk.exception.ObjectNotFoundException;
import mediscreen.patientRisk.model.DiabetesRisk;
import mediscreen.patientRisk.model.PatientCategory;
import mediscreen.patientRisk.model.TriggerTerm;
import mediscreen.patientRisk.proxy.MedicalNoteProxy;
import mediscreen.patientRisk.proxy.PatientInformationProxy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientRiskServiceImpl implements PatientRiskService {

    private final PatientInformationProxy patientInformationProxy;
    private final MedicalNoteProxy medicalNoteProxy;

    public PatientRiskServiceImpl(PatientInformationProxy patientInformationProxy, MedicalNoteProxy medicalNoteProxy) {
        this.patientInformationProxy = patientInformationProxy;
        this.medicalNoteProxy = medicalNoteProxy;
    }

    /**
     * Get a diabetes risk for a patient by its id.
     *
     * @param patId the id of the patient whose risk is calculated
     * @return a String containing the patient's name, age and diabetes risk
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    @Override
    public String getAssessById(int patId) throws ObjectNotFoundException {

        PatientBean patient;
        try {
            patient = patientInformationProxy.getPatientById(patId);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patId + " was not found.");
        }
        DiabetesRisk diabetesRisk = getDiabetesRisk(patient);
        return getResultString(patient, diabetesRisk);
    }

    /**
     * Get a diabetes risk for patients by its family name.
     * If there are several patients having the same name, returns diabetes risk for all patients.
     * If there is not any patient with this name, returns an empty list
     *
     * @param familyName the researched family name
     * @return a list of String containing the patient's name, age and diabetes risk for each patient having the researched name
     * @throws ObjectNotFoundException if there is not any patient with this name
     */
    @Override
    public List<String> getAssessByName(String familyName) throws ObjectNotFoundException {
        List<PatientBean> patientList = patientInformationProxy.getPatientsByName(familyName, null);
        if (patientList.size() != 0) {
            List<String> result = new ArrayList<>();
            for (PatientBean patient : patientList) {
                DiabetesRisk diabetesRisk = getDiabetesRisk(patient);
                String toAdd = getResultString(patient, diabetesRisk);
                result.add(toAdd);
            }
            return result;
        } else {
            throw new ObjectNotFoundException("There is not any patient named " + familyName);
        }
    }

    /**
     * Construct a String containing information about patient's name, age and diabetes risk
     *
     * @param patient the concerned patient
     * @param diabetesRisk the DiabetesRisk object corresponding to the patient's situation
     * @return a String containing patient's name, age and diabetes risk
     */
    private String getResultString(PatientBean patient, DiabetesRisk diabetesRisk) {
        return "Patient: " + patient.getFamilyName()
                + " " + patient.getGivenName()
                + " (age " + patient.getAge() + ") "
                + "diabetes assessment is: " + diabetesRisk.name();
    }

    /**
     * Get a diabetes risk for a patient by its id.
     *
     * @param patId the id of the patient whose risk is calculated
     * @return the DiabetesRisk object corresponding to the patient's situation
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    @Override
    public DiabetesRisk getDiabetesRiskById(int patId) throws ObjectNotFoundException {
        PatientBean patient;
        try {
            patient = patientInformationProxy.getPatientById(patId);
        } catch (FeignException.FeignClientException e) {
            throw new ObjectNotFoundException("The patient with id " + patId + " was not found.");
        }
        return getDiabetesRisk(patient);

    }

    /**
     * Calculate diabetes risk for a patient thanks to its information
     *
     * @param patient the patient whose risk is calculated
     * @return a DiabetesRisk object
     */
    private DiabetesRisk getDiabetesRisk(PatientBean patient) {

        String patientAllContent = medicalNoteProxy.getAllInformation(String.valueOf(patient.getPatientId()));

        DiabetesRisk diabetesRisk = DiabetesRisk.NONE;

        int numberOfTriggerTerm = getNumberOfTriggerTerm(patientAllContent);

        if (numberOfTriggerTerm >= 2) {
            PatientCategory patientCategory = getPatientCategory(patient);
            switch (patientCategory) {

                case OVER_30YEARS: {
                    if (numberOfTriggerTerm < 6) {
                        diabetesRisk = DiabetesRisk.BORDERLINE;
                    } else {
                        if (numberOfTriggerTerm < 8) {
                            diabetesRisk = DiabetesRisk.IN_DANGER;
                        } else {
                            diabetesRisk = DiabetesRisk.EARLY_ONSET;
                        }
                    }
                    break;
                }

                case WOMAN_MINUS_30YEARS: {
                    if (numberOfTriggerTerm >= 4) {
                        if (numberOfTriggerTerm < 7) {
                            diabetesRisk = DiabetesRisk.IN_DANGER;
                        } else {
                            diabetesRisk = DiabetesRisk.EARLY_ONSET;
                        }
                    }
                    break;
                }

                case MAN_MINUS_30YEARS: {
                    if (numberOfTriggerTerm >= 3) {
                        if (numberOfTriggerTerm < 5) {
                            diabetesRisk = DiabetesRisk.IN_DANGER;
                        } else {
                            diabetesRisk = DiabetesRisk.EARLY_ONSET;
                        }
                    }
                    break;
                }
            }
        }
        return diabetesRisk;
    }

    /**
     * Calculate patient's category from its age and its sex
     *
     * @param patient the concerned patient
     * @return a PatientCategory object
     */
    private PatientCategory getPatientCategory(PatientBean patient) {
        if (patient.getAge() < 30) {
            if (patient.getSex().equals("M")) {
                return PatientCategory.MAN_MINUS_30YEARS;
            } else {
                return PatientCategory.WOMAN_MINUS_30YEARS;
            }
        } else {
            return PatientCategory.OVER_30YEARS;
        }
    }

    /**
     * Calculate the number of trigger terms contained in the given String
     *
     * @param patientAllContent a String containing contents of patient's notes
     * @return an int which is the number of terms found
     */
    private int getNumberOfTriggerTerm(String patientAllContent) {
        int numberOfTerms = 0;
        if (patientAllContent != null) {
            TriggerTerm[] triggerTerms = TriggerTerm.values();
            for (TriggerTerm term : triggerTerms) {
                if (patientAllContent.toLowerCase().replaceAll(" ", "").contains(term.name().toLowerCase())) {
                    numberOfTerms++;
                }
            }
        }
        return numberOfTerms;
    }
}
