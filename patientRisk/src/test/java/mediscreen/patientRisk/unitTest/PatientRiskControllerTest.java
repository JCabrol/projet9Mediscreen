package mediscreen.patientRisk.unitTest;


import mediscreen.patientRisk.exception.ObjectNotFoundException;
import mediscreen.patientRisk.model.DiabetesRisk;
import mediscreen.patientRisk.service.PatientRiskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("PatientRiskControllerTest")
@SpringBootTest
@AutoConfigureMockMvc
public class PatientRiskControllerTest {

    @MockBean
    private PatientRiskService patientRiskService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @Tag("GetAssessByIdTest")
    @DisplayName("getAssessById tests:")
    class GetAssessByTest {


        @DisplayName("GIVEN a String returned by service " +
                "WHEN the uri \"/assess/id is called " +
                "THEN the expected string is returned.")
        @Test
        void getAssessByIdTest() throws Exception {

            //GIVEN
            //a String returned by service
            int patientId = 1;
            String serviceReturn = "serviceReturn";
            when(patientRiskService.getAssessById(patientId)).thenReturn(serviceReturn);
            //WHEN
            //the uri "/assess/id is called
            mockMvc.perform(get("/assess/id").param("patId", "1"))

                    //THEN
                    //the expected string is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("serviceReturn"));
            // and the expected methods have been called with expected arguments
            verify(patientRiskService, Mockito.times(1)).getAssessById(patientId);
        }

        @DisplayName("GIVEN an exception returned by service " +
                "WHEN the uri \"/assess/id is called " +
                "THEN the expected error message is returned.")
        @Test
        void getAssessByIdNotFoundTest() throws Exception {

            //GIVEN
            //an exception returned by service
            int patientId = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(patientRiskService.getAssessById(patientId)).thenThrow(objectNotFoundException);
            //WHEN
            //the uri "/assess/id is called
            mockMvc.perform(get("/assess/id").param("patId", "1"))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("error message"));
            // and the expected methods have been called with expected arguments
            verify(patientRiskService, Mockito.times(1)).getAssessById(patientId);
        }

        @DisplayName("GIVEN no given parameter " +
                "WHEN the uri \"/assess/id is called " +
                "THEN the expected error message is returned.")
        @Test
        void getAssessByIdWithoutParameterTest() throws Exception {

            //GIVEN
            //no given parameter

            //WHEN
            //the uri "/assess/id is called
            mockMvc.perform(get("/assess/id"))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : a request parameter is missing or wrong.\n"));
            // and the expected methods have been called with expected arguments
            verify(patientRiskService, Mockito.times(0)).getAssessById(anyInt());
        }
    }

    @Nested
    @Tag("GetAssessByNameTest")
    @DisplayName("getAssessByName tests:")
    class GetAssessByNameTest {


        @DisplayName("GIVEN a list of string returned by service " +
                "WHEN the uri \"/assess/familyName is called " +
                "THEN the expected list of string is returned.")
        @Test
        void getAssessByNameTest() throws Exception {

            //GIVEN
            //a list of string returned by service
            String familyName = "familyName";
            List<String> serviceReturn = List.of("serviceReturn");
            when(patientRiskService.getAssessByName(familyName)).thenReturn(serviceReturn);
            //WHEN
            //the uri "/assess/familyName is called
            MvcResult mvcResult = mockMvc.perform(get("/assess/familyName").param("familyName", familyName))

                    //THEN
                    //the expected list of string is returned
                    .andExpect(status().isOk()).andReturn();
            assertTrue(mvcResult.getResponse().getContentAsString().contains("serviceReturn"));
            // and the expected methods have been called with expected arguments
            verify(patientRiskService, Mockito.times(1)).getAssessByName(familyName);
        }

        @DisplayName("GIVEN an exception returned by service " +
                "WHEN the uri \"/assess/familyName is called " +
                "THEN the expected error message is returned.")
        @Test
        void getAssessByNameNotFoundTest() throws Exception {

            //GIVEN
            //an exception returned by service
            String familyName = "familyName";
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(patientRiskService.getAssessByName(familyName)).thenThrow(objectNotFoundException);
            //WHEN
            //the uri "/assess/familyName is called
            mockMvc.perform(get("/assess/familyName").param("familyName", familyName))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("error message"));
            // and the expected methods have been called with expected arguments
            verify(patientRiskService, Mockito.times(1)).getAssessByName(familyName);
        }

        @DisplayName("GIVEN no given parameter " +
                "WHEN the uri \"/assess/familyName is called " +
                "THEN the expected error message is returned.")
        @Test
        void getAssessByNameWithoutParameterTest() throws Exception {

            //GIVEN
            //no given parameter

            //WHEN
            //the uri "/assess/familyName is called
            mockMvc.perform(get("/assess/familyName"))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : a request parameter is missing or wrong.\n"));
            // and the expected methods have been called with expected arguments
            verify(patientRiskService, Mockito.times(0)).getAssessByName(anyString());
        }
    }

    @Nested
    @Tag("GetDiabetesRiskTest")
    @DisplayName("getDiabetesRisk tests:")
    class GetDiabetesRiskTest {


        @DisplayName("GIVEN a DiabetesRisk object returned by service " +
                "WHEN the uri \"/diabetesRisk/{id} is called " +
                "THEN the DiabetesRisk is returned.")
        @Test
        void getDiabetesRiskTest() throws Exception {

            //GIVEN
            //a DiabetesRisk object returned by service
            int patientId = 1;
            DiabetesRisk diabetesRisk = DiabetesRisk.NONE;
            when(patientRiskService.getDiabetesRiskById(patientId)).thenReturn(diabetesRisk);
            //WHEN
            //the uri "/diabetesRisk/{id} is called
            MvcResult mvcResult = mockMvc.perform(get("/diabetesRisk/{id}", "1"))
                    //THEN
                    //the DiabetesRisk is returned
                    .andExpect(status().isOk()).andReturn();
            assertTrue(mvcResult.getResponse().getContentAsString().contains("NONE"));
            // and the expected methods have been called with expected arguments
            verify(patientRiskService, Mockito.times(1)).getDiabetesRiskById(patientId);
        }

        @DisplayName("GIVEN an exception returned by service " +
                "WHEN the uri \"/diabetesRisk/{id} is called " +
                "THEN the expected error message is returned.")
        @Test
        void getDiabetesRiskNotFoundTest() throws Exception {

            //GIVEN
            //an exception returned by service
            int patientId = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(patientRiskService.getDiabetesRiskById(patientId)).thenThrow(objectNotFoundException);
            //WHEN
            //the uri "/diabetesRisk/{id} is called
            mockMvc.perform(get("/diabetesRisk/{id}", "1"))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("error message"));
            // and the expected methods have been called with expected arguments
            verify(patientRiskService, Mockito.times(1)).getDiabetesRiskById(patientId);
        }
    }
}
