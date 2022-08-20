package mediscreen.patientRisk.integrationTest;

import feign.FeignException;
import mediscreen.patientRisk.bean.PatientBean;
import mediscreen.patientRisk.proxy.MedicalNoteProxy;
import mediscreen.patientRisk.proxy.PatientInformationProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Tag("patientRiskIntegrationTest")
@SpringBootTest
@AutoConfigureMockMvc
public class PatientRiskIntegrationTest {

    @MockBean
    private MedicalNoteProxy medicalNoteProxy;
    @MockBean
    private PatientInformationProxy patientInformationProxy;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @Tag("GetAssessByIdIntegrationTest")
    @DisplayName("getAssessById integration tests:")
    class GetAssessByIdIntegrationTest {


        @DisplayName("GIVEN an patient found " +
                "WHEN the uri \"/assess/id is called " +
                "THEN the expected string is returned.")
        @Test
        void getAssessByIdIntegrationTest() throws Exception {

            //GIVEN
            //a patient found
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
            //the uri "/assess/id is called
            mockMvc.perform(get("/assess/id").param("patId", "1"))

                    //THEN
                    //the expected string is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("Patient: family given (age 60) diabetes assessment is: NONE"));

        }

        @DisplayName("GIVEN a patient not found " +
                "WHEN the uri \"/assess/id is called " +
                "THEN the expected error message is returned.")
        @Test
        void getAssessByIdNotFoundIntegrationTest() throws Exception {

            //GIVEN
            //an exception returned by service
            int patientId = 1;
            when(patientInformationProxy.getPatientById(patientId)).thenThrow(FeignException.FeignClientException.class);
            //WHEN
            //the uri "/assess/id is called
            mockMvc.perform(get("/assess/id").param("patId", "1"))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("The patient with id " + patientId + " was not found."));
        }

        @DisplayName("GIVEN no given parameter " +
                "WHEN the uri \"/assess/id is called " +
                "THEN the expected error message is returned.")
        @Test
        void getAssessByIdWithoutParameterIntegrationTest() throws Exception {

            //GIVEN
            //no given parameter

            //WHEN
            //the uri "/assess/id is called
            mockMvc.perform(get("/assess/id"))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : a request parameter is missing or wrong.\n"));
        }
    }

    @Nested
    @Tag("GetAssessByNameIntegrationTest")
    @DisplayName("getAssessByName intergration tests:")
    class GetAssessByNameIntegrationTest {


        @DisplayName("GIVEN a list of patient found service " +
                "WHEN the uri \"/assess/familyName is called " +
                "THEN the expected list of string is returned.")
        @Test
        void getAssessByNameIntegrationTest() throws Exception {

            //GIVEN
            //a list of patient found service
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
            when(patientInformationProxy.getPatientsByName(familyName, null)).thenReturn(patientList);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the uri "/assess/familyName is called
            MvcResult mvcResult = mockMvc.perform(get("/assess/familyName").param("familyName", familyName))

                    //THEN
                    //the expected list of string is returned
                    .andExpect(status().isOk()).andReturn();
            assertTrue(mvcResult.getResponse().getContentAsString().contains("Patient: family given (age 60) diabetes assessment is: NONE"));
        }

        @DisplayName("GIVEN not any patient found " +
                "WHEN the uri \"/assess/familyName is called " +
                "THEN the expected error message is returned.")
        @Test
        void getAssessByNameNotFoundTest() throws Exception {

            //GIVEN
            //not any patient found
            String familyName = "family";
            when(patientInformationProxy.getPatientsByName(familyName, null)).thenReturn(new ArrayList<>());
            //WHEN
            //the uri "/assess/familyName is called
            mockMvc.perform(get("/assess/familyName").param("familyName", familyName))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("There is not any patient named " + familyName));
        }

        @DisplayName("GIVEN no given parameter " +
                "WHEN the uri \"/assess/familyName is called " +
                "THEN the expected error message is returned.")
        @Test
        void getAssessByNameWithoutParameterIntegrationTest() throws Exception {

            //GIVEN
            //no given parameter

            //WHEN
            //the uri "/assess/familyName is called
            mockMvc.perform(get("/assess/familyName"))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : a request parameter is missing or wrong.\n"));
        }
    }

    @Nested
    @Tag("GetDiabetesRiskIntegrationTest")
    @DisplayName("getDiabetesRisk integration tests:")
    class GetDiabetesRiskIntegrationTest {


        @DisplayName("GIVEN a patient found " +
                "WHEN the uri \"/diabetesRisk/{id} is called " +
                "THEN the DiabetesRisk is returned.")
        @Test
        void getDiabetesRiskIntegrationTest() throws Exception {

            //GIVEN
            //a patient found
            int patientId = 1;
            PatientBean patientBean = new PatientBean();
            patientBean.setPatientId(patientId);
            patientBean.setAge(40);
            String patientAllContents = "String containing 7 trigger terms : hemoglobin A1c , smoker,  ANORMAL, cholestérol,DizzinessandANTICORPS fumeur";
            when(patientInformationProxy.getPatientById(patientId)).thenReturn(patientBean);
            when(medicalNoteProxy.getAllInformation(String.valueOf(patientId))).thenReturn(patientAllContents);
            //WHEN
            //the uri "/diabetesRisk/{id} is called
            MvcResult mvcResult = mockMvc.perform(get("/diabetesRisk/{id}", "1"))
                    //THEN
                    //the DiabetesRisk is returned
                    .andExpect(status().isOk()).andReturn();
            assertTrue(mvcResult.getResponse().getContentAsString().contains("IN_DANGER"));
        }

        @DisplayName("GIVEN an exception returned by service " +
                "WHEN the uri \"/diabetesRisk/{id} is called " +
                "THEN the expected error message is returned.")
        @Test
        void getDiabetesRiskNotFoundIntegrationTest() throws Exception {

            //GIVEN
            //an exception returned by service
            int patientId = 1;
            when(patientInformationProxy.getPatientById(patientId)).thenThrow(FeignException.FeignClientException.class);
            //WHEN
            //the uri "/diabetesRisk/{id} is called
            mockMvc.perform(get("/diabetesRisk/{id}", "1"))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("The patient with id " + patientId + " was not found."));
        }
    }
}


