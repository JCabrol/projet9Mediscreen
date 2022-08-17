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

    private String getResultString(PatientBean patient, DiabetesRisk diabetesRisk) {
        return "Patient: " + patient.getFamilyName()
                + " " + patient.getGivenName()
                + " (age " + patient.getAge() + ") "
                + "diabetes assessment is: " + diabetesRisk.name();
    }

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
