package mediscreen.patientRisk.unitTest;

import feign.FeignException;
import mediscreen.patientRisk.bean.PatientBean;
import mediscreen.patientRisk.exception.ObjectNotFoundException;
import mediscreen.patientRisk.model.DiabetesRisk;
import mediscreen.patientRisk.proxy.MedicalNoteProxy;
import mediscreen.patientRisk.proxy.PatientInformationProxy;
import mediscreen.patientRisk.service.PatientRiskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("PatientRiskServiceTest")
@SpringBootTest
public class PatientRiskServiceTest {

    @Autowired
    private PatientRiskService patientRiskService;
    @MockBean
    PatientInformationProxy patientInformationProxy;
    @MockBean
    MedicalNoteProxy medicalNoteProxy;


    @Nested
    @Tag("GetDiabetesRiskByIdTest")
    @DisplayName("getDiabetesRiskById tests:")
    class GetDiabetesRiskByIdTest {

        @DisplayName("GIVEN a patient with no trigger term " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"NONE\" is returned.")
        @Test
        void getDiabetesRiskByIdNoTriggerTermTest() {

            //GIVEN
            //a patient with no trigger term
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            String patientAllContents = "String not containing any trigger term.";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "NONE" is returned
            assertEquals(result, DiabetesRisk.NONE);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN a patient over 30 years with 2 trigger terms " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"BORDERLINE\" is returned.")
        @Test
        void getDiabetesRiskByIdOver30Years2TriggerTermsTest() {

            //GIVEN
            //a patient over 30 years with 2 trigger terms
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(40);
            String patientAllContents = "String containing 2 trigger terms : hemoglobin A1c , smoker.";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "BORDERLINE" is returned
            assertEquals(result, DiabetesRisk.BORDERLINE);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN a patient over 30 years with 7 trigger terms " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"IN_DANGER\" is returned.")
        @Test
        void getDiabetesRiskByIdOver30Years7TriggerTermsTest() {

            //GIVEN
            //a patient over 30 years with 7 trigger terms
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(40);
            String patientAllContents = "String containing 7 trigger terms : hemoglobin A1c , smoker,  ANORMAL, cholestérol,DizzinessandANTICORPS fumeur";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "IN_DANGER" is returned
            assertEquals(result, DiabetesRisk.IN_DANGER);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN a patient over 30 years with 9 trigger terms " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"EARLY_ONSET\" is returned.")
        @Test
        void getDiabetesRiskByIdOver30Years9TriggerTermsTest() {

            //GIVEN
            //a patient over 30 years with 9 trigger terms
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(40);
            String patientAllContents = "String containing 9 trigger terms : hemoglobin A1c , smoker,  ANORMAL, cholestérol,DizzinessandANTICORPS fumeur relapse and reaction";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "EARLY_ONSET" is returned
            assertEquals(result, DiabetesRisk.EARLY_ONSET);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN a man under 30 years with 2 trigger terms " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"NONE\" is returned.")
        @Test
        void getDiabetesRiskByIdManUnder30Years2TriggerTermsTest() {

            //GIVEN
            //a man under 30 years with 2 trigger terms
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(25);
            patientBean.setSex("M");
            String patientAllContents = "String containing 2 trigger terms : hemoglobin A1c , smoker.";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "NONE" is returned
            assertEquals(result, DiabetesRisk.NONE);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN a woman under 30 years with 3 trigger terms " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"NONE\" is returned.")
        @Test
        void getDiabetesRiskByIdWomanUnder30Years3TriggerTermsTest() {

            //GIVEN
            //a woman under 30 years with 3 trigger terms
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(25);
            patientBean.setSex("F");
            String patientAllContents = "String containing 3 trigger terms : hemoglobin A1c , smoker, anormal.";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "NONE" is returned
            assertEquals(result, DiabetesRisk.NONE);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN a man under 30 years with 3 trigger terms " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"IN_DANGER\" is returned.")
        @Test
        void getDiabetesRiskByIdManUnder30Years3TriggerTermsTest() {

            //GIVEN
            //a man under 30 years with 3 trigger terms
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(25);
            patientBean.setSex("M");
            String patientAllContents = "String containing 3 trigger terms : hemoglobin A1c , smoker, anormal.";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "IN_DANGER" is returned
            assertEquals(result, DiabetesRisk.IN_DANGER);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN a woman under 30 years with 5 trigger terms " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"IN_DANGER\" is returned.")
        @Test
        void getDiabetesRiskByIdWomanUnder30Years5TriggerTermsTest() {

            //GIVEN
            //a woman under 30 years with 5 trigger terms
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(25);
            patientBean.setSex("F");
            String patientAllContents = "String containing 5 trigger terms : hemoglobin A1c , smoker, anormal, fumer, vertige.";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "IN_DANGER" is returned
            assertEquals(result, DiabetesRisk.IN_DANGER);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN a man under 30 years with 5 trigger terms " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"EARLY_ONSET\" is returned.")
        @Test
        void getDiabetesRiskByIdManUnder30Years5TriggerTermsTest() {

            //GIVEN
            //a man under 30 years with 5 trigger terms
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(25);
            patientBean.setSex("M");
            String patientAllContents = "String containing 5 trigger terms : hemoglobin A1c , smoker, anormal, fumer, vertige.";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "EARLY_ONSET" is returned
            assertEquals(result, DiabetesRisk.EARLY_ONSET);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));

        }

        @DisplayName("GIVEN a woman under 30 years with 7 trigger terms " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN \"EARLY_ONSET\" is returned.")
        @Test
        void getDiabetesRiskByIdWomanUnder30Years7TriggerTermsTest() {

            //GIVEN
            //a woman under 30 years with 7 trigger terms
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(25);
            patientBean.setSex("F");
            String patientAllContents = "String containing 7 trigger terms : hemoglobin A1c , smoker, anormal, fumer, vertige,rechuteanticorps.";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getDiabetesRiskById is called
            DiabetesRisk result = patientRiskService.getDiabetesRiskById(patientId);

            //THEN
            // "EARLY_ONSET" is returned
            assertEquals(result, DiabetesRisk.EARLY_ONSET);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN an exception returned by proxy " +
                "WHEN the function getDiabetesRiskById is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getDiabetesRiskByIdNotFoundTest() {

            //GIVEN
            //an exception returned by proxy
            int patientId = 1;
            when(patientInformationProxy.getPatientById(patientId)).thenThrow(FeignException.FeignClientException.class);

            //WHEN
            //the function getDiabetesRiskById is called


            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientRiskService.getDiabetesRiskById(patientId));
            assertEquals("The patient with id " + patientId + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(0)).getAllInformation(anyString());
        }
    }

    @Nested
    @Tag("GetAssessByIdTest")
    @DisplayName("getAssessById tests:")
    class GetAssessByIdTest {

        @DisplayName("GIVEN a patient " +
                "WHEN the function getAssessById is called " +
                "THEN the expected String is returned.")
        @Test
        void getAssessByIdTest() {

            //GIVEN
            //a patient
            int patientId = 1;
            PatientBean patient = new PatientBean();
            patient.setPatientId(patientId);
            patient.setAge(60);
            patient.setFamilyName("family");
            patient.setGivenName("given");
            String patientAllContents = "String not containing any trigger term.";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patient);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getAssessById is called
            String result = patientRiskService.getAssessById(patientId);

            //THEN
            // the expected String is returned
            assertEquals(result, "Patient: family given (age 60) diabetes assessment is: NONE");
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN an exception returned by the proxy " +
                "WHEN the function getAssessById is called " +
                "THEN the expected String is returned.")
        @Test
        void getAssessByIdNotFoundTest() {

            //GIVEN
            //an exception returned by the proxy
            int patientId = 1;
            when(patientInformationProxy.getPatientById(patientId)).thenThrow(FeignException.FeignClientException.class);
            //WHEN
            //the function getAssessById is called


            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientRiskService.getAssessById(patientId));
            assertEquals("The patient with id " + patientId + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(patientId);
            verify(medicalNoteProxy, Mockito.times(0)).getAllInformation(anyString());
        }
    }

    @Nested
    @Tag("GetAssessByNameTest")
    @DisplayName("getAssessByName tests:")
    class GetAssessByNameTest {

        @DisplayName("GIVEN a patient " +
                "WHEN the function getAssessByName is called " +
                "THEN the expected list of String is returned.")
        @Test
        void getAssessByNameTest() {

            //GIVEN
            //a patient
            int patientId = 1;
            String familyName = "family";
            PatientBean patient = new PatientBean();
            patient.setPatientId(patientId);
            patient.setAge(60);
            patient.setFamilyName(familyName);
            patient.setGivenName("given");
            List<PatientBean> patientList = new ArrayList<>();
            patientList.add(patient);
            String patientAllContents = "String not containing any trigger term.";
            when(patientInformationProxy.getPatientsByName(familyName,null)).thenReturn(patientList);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the function getAssessByName is called
            List<String> result = patientRiskService.getAssessByName(familyName);

            //THEN
            // the expected list of String is returned
            assertEquals(1, result.size());
            assertEquals(result.get(0), "Patient: family given (age 60) diabetes assessment is: NONE");
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientsByName(familyName, null);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
        }

        @DisplayName("GIVEN 3 patients with same family Name" +
                "WHEN the function getAssessByName is called " +
                "THEN the expected list of String is returned.")
        @Test
        void getAssessByName3HomonymsTest() {

            //GIVEN
            //a patient
            int patientId = 1;
            String familyName = "family";
            PatientBean patient = new PatientBean();
            patient.setPatientId(patientId);
            patient.setAge(60);
            patient.setFamilyName(familyName);
            patient.setGivenName("given");
            String familyName2 = "family2";
            PatientBean patient2 = new PatientBean();
            int patientId2 = 2;
            patient2.setPatientId(patientId2);
            patient2.setAge(20);
            patient2.setFamilyName(familyName2);
            patient2.setGivenName("given2");
            String familyName3 = "family3";
            PatientBean patient3 = new PatientBean();
            int patientId3 = 3;
            patient3.setPatientId(patientId3);
            patient3.setAge(30);
            patient3.setFamilyName(familyName3);
            patient3.setGivenName("given3");
            List<PatientBean> patientList = new ArrayList<>();
            patientList.add(patient);
            patientList.add(patient2);
            patientList.add(patient3);
            String patientAllContents = "String not containing any trigger term.";
            when(patientInformationProxy.getPatientsByName(familyName,null)).thenReturn(patientList);
            when(medicalNoteProxy.getAllInformation(anyString())).thenReturn(patientAllContents);
            //WHEN
            //the function getAssessByName is called
            List<String> result = patientRiskService.getAssessByName(familyName);

            //THEN
            // the expected list of String is returned
            assertEquals(3, result.size());
            assertEquals(result.get(0), "Patient: family given (age 60) diabetes assessment is: NONE");
            assertEquals(result.get(1), "Patient: family2 given2 (age 20) diabetes assessment is: NONE");
            assertEquals(result.get(2), "Patient: family3 given3 (age 30) diabetes assessment is: NONE");
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientsByName(familyName, null);
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId));
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId2));
            verify(medicalNoteProxy, Mockito.times(1)).getAllInformation(String.valueOf(patientId2));
        }

        @DisplayName("GIVEN a name non-existing " +
                "WHEN the function getAssessByName is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getAssessByNameNoExistingTest() {

            //GIVEN
            //a name non-existing

            String familyName = "family";
            when(patientInformationProxy.getPatientsByName(familyName,null)).thenReturn(new ArrayList<>());
            //WHEN
            //the function getAssessByName is called

            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientRiskService.getAssessByName(familyName));
            assertEquals("There is not any patient named "+familyName, exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientsByName(familyName, null);
            verify(medicalNoteProxy, Mockito.times(0)).getAllInformation(anyString());
        }
    }
}
