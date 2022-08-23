package mediscreen.patientUI.unitTest;

import lombok.extern.slf4j.Slf4j;
import mediscreen.patientUI.bean.MedicalNoteBean;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.exception.ObjectNotFoundException;
import mediscreen.patientUI.modele.ListOfNotesToDisplay;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import mediscreen.patientUI.modele.PatientSearch;
import mediscreen.patientUI.patientUIService.PatientUIService;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("ControllerTests")
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientUIService patientUIService;

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("HomeTest")
    class HomeTests {

        @Test
        @DisplayName("GIVEN  " +
                "WHEN the uri \"/\" is called, " +
                "THEN the status is isOk and correct template is returned.")
        void getHomeTest() throws Exception {
            //GIVEN
            //
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/");
            // WHEN
            //the uri "/home" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the status is "isOk" and correct template is returned.
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(model().hasNoErrors());
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("GetAllPatientsTest")
    class GetAllPatientsTests {
        @Test
        @DisplayName("GIVEN a list of patient to display and no given page " +
                "WHEN the uri \"/patient/getAllPatients\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getAllPatientsTest() throws Exception {
            //GIVEN
            List<PatientBean> patientList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                PatientBean patientBean = new PatientBean(i, "familyName" + i, "givenName" + i, "dateOfBirth" + i, "sex" + i, "phone" + i, "address" + i, i);
                patientList.add(patientBean);
            }
            Integer[] listPages = new Integer[3];
            listPages[0] = 1;
            listPages[1] = 2;
            listPages[2] = 3;
            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 1, 4, listPages);
            when(patientUIService.getAllPatient()).thenReturn(patientList);
            when(patientUIService.getPatientsToDisplay(1, 6, patientList)).thenReturn(listOfPatientsToDisplay);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getAllPatients");
            // WHEN
            //the uri "/login" is called,
            MvcResult response = mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("selectPatient"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("linkedFrom", "allPatients"))
                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
                    .andExpect(model().attributeExists("patientSearch"))
                    .andReturn();
            //and correct information is displayed by view
            assertTrue(response.getResponse().getContentAsString().contains("All patients list"));
            assertTrue(response.getResponse().getContentAsString().contains("1"));
            assertTrue(response.getResponse().getContentAsString().contains("2"));
            assertTrue(response.getResponse().getContentAsString().contains("3"));
            assertFalse(response.getResponse().getContentAsString().contains("There is not any patient registered yet."));
            assertFalse(response.getResponse().getContentAsString().contains("Result for searching"));
            assertFalse(response.getResponse().getContentAsString().contains("Sorry, there is no result for your research."));
            assertFalse(response.getResponse().getContentAsString().contains("See all patient list"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getAllPatient();
            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(1, 6, patientList);
        }

        @Test
        @DisplayName("GIVEN an empty list of patient to display and no given page " +
                "WHEN the uri \"/patient/getAllPatients\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getAllPatientsEmptyListTest() throws Exception {
            //GIVEN
            List<PatientBean> patientList = new ArrayList<>();
            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 1, 0, new Integer[3]);
            when(patientUIService.getAllPatient()).thenReturn(patientList);
            when(patientUIService.getPatientsToDisplay(1, 6, patientList)).thenReturn(listOfPatientsToDisplay);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getAllPatients");
            // WHEN
            //the uri "/login" is called,
            MvcResult response = mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("selectPatient"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("linkedFrom", "allPatients"))
                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
                    .andExpect(model().attributeExists("patientSearch"))
                    .andReturn();
            //and correct information is displayed by view
            assertTrue(response.getResponse().getContentAsString().contains("All patients list"));
            assertTrue(response.getResponse().getContentAsString().contains("There is not any patient registered yet."));
            assertFalse(response.getResponse().getContentAsString().contains("Result for searching"));
            assertFalse(response.getResponse().getContentAsString().contains("Sorry, there is no result for your research."));
            assertFalse(response.getResponse().getContentAsString().contains("See all patient list"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getAllPatient();
            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(1, 6, patientList);
        }

        @Test
        @DisplayName("GIVEN a list of patient to display and a given page " +
                "WHEN the uri \"/patient/getAllPatients\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getAllPatientsPageNumberTest() throws Exception {
            //GIVEN
            List<PatientBean> patientList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                PatientBean patientBean = new PatientBean(i, "familyName" + i, "givenName" + i, "dateOfBirth" + i, "sex" + i, "phone" + i, "address" + i, i);
                patientList.add(patientBean);
            }
            Integer[] listPages = new Integer[3];
            listPages[0] = 4;
            listPages[1] = 5;
            listPages[2] = 6;
            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 5, 6, listPages);
            when(patientUIService.getAllPatient()).thenReturn(patientList);
            when(patientUIService.getPatientsToDisplay(5, 6, patientList)).thenReturn(listOfPatientsToDisplay);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getAllPatients").param("currentPage", String.valueOf(5));
            // WHEN
            //the uri "/login" is called,
            MvcResult response = mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("selectPatient"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("linkedFrom", "allPatients"))
                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
                    .andExpect(model().attributeExists("patientSearch"))
                    .andReturn();
            //and correct information is displayed by view
            assertTrue(response.getResponse().getContentAsString().contains("All patients list"));
            assertTrue(response.getResponse().getContentAsString().contains("4"));
            assertTrue(response.getResponse().getContentAsString().contains("5"));
            assertTrue(response.getResponse().getContentAsString().contains("6"));
            assertFalse(response.getResponse().getContentAsString().contains("There is not any patient registered yet."));
            assertFalse(response.getResponse().getContentAsString().contains("Result for searching"));
            assertFalse(response.getResponse().getContentAsString().contains("Sorry, there is no result for your research."));
            assertFalse(response.getResponse().getContentAsString().contains("See all patient list"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getAllPatient();
            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(5, 6, patientList);
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("getPatientByNameTest")
    class GetPatientByNameTests {

        @Test
        @DisplayName("GIVEN a givenName and a lastName with a result " +
                "WHEN the uri \"/patient/getPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientByNameTest() throws Exception {
            //GIVEN
            //a givenName and a lastName with a result
            List<PatientBean> patientList = new ArrayList<>();
            String familyName = "familyName1";
            String givenName = "givenName1";
            PatientBean patientBean = new PatientBean(1, familyName, givenName, "dateOfBirth1", "M", "phone1", "address1", 1);
            patientList.add(patientBean);
            Integer[] listPages = new Integer[3];
            listPages[0] = 1;
            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 1, 1, listPages);
            when(patientUIService.getPatientByName(familyName, givenName)).thenReturn(patientList);
            when(patientUIService.getPatientsToDisplay(anyInt(), anyInt(), anyList())).thenReturn(listOfPatientsToDisplay);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getPatient")
                    .param("familyName", familyName)
                    .param("givenName", givenName);
            // WHEN
            //the uri "/patient/getPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("selectPatient"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("linkedFrom", "research"))
                    .andExpect(model().attribute("familyName", familyName))
                    .andExpect(model().attribute("givenName", givenName))
                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
                    .andExpect(model().attributeExists("patientSearch"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getPatientByName(familyName, givenName);
            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(1, 6, patientList);
        }

        @Test
        @DisplayName("GIVEN a givenName with a result " +
                "WHEN the uri \"/patient/getPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientByNameOnlyGivenNameTest() throws Exception {
            //GIVEN
            //a givenName with a result
            List<PatientBean> patientList = new ArrayList<>();
            String givenName = "givenName1";
            PatientBean patientBean = new PatientBean(1, "familyName", givenName, "dateOfBirth1", "M", "phone1", "address1", 1);
            patientList.add(patientBean);
            Integer[] listPages = new Integer[3];
            listPages[0] = 1;
            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 1, 1, listPages);
            when(patientUIService.getPatientByName(null, givenName)).thenReturn(patientList);
            when(patientUIService.getPatientsToDisplay(anyInt(), anyInt(), anyList())).thenReturn(listOfPatientsToDisplay);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getPatient")
                    .param("givenName", givenName);
            // WHEN
            //the uri "/patient/getPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("selectPatient"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("linkedFrom", "research"))
                    .andExpect(model().attribute("givenName", givenName))
                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
                    .andExpect(model().attributeExists("patientSearch"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getPatientByName(null, givenName);
            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(1, 6, patientList);
        }

        @Test
        @DisplayName("GIVEN a familyName with a result " +
                "WHEN the uri \"/patient/getPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientByNameOnlyFamilyNameTest() throws Exception {
            //GIVEN
            //a familyName with a result
            List<PatientBean> patientList = new ArrayList<>();
            String familyName = "familyName1";
            PatientBean patientBean = new PatientBean(1, familyName, "givenName", "dateOfBirth1", "M", "phone1", "address1", 1);
            patientList.add(patientBean);
            Integer[] listPages = new Integer[3];
            listPages[0] = 1;
            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 1, 1, listPages);
            when(patientUIService.getPatientByName(familyName, null)).thenReturn(patientList);
            when(patientUIService.getPatientsToDisplay(anyInt(), anyInt(), anyList())).thenReturn(listOfPatientsToDisplay);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getPatient")
                    .param("familyName", familyName);
            // WHEN
            //the uri "/patient/getPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("selectPatient"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("linkedFrom", "research"))
                    .andExpect(model().attribute("familyName", familyName))
                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
                    .andExpect(model().attributeExists("patientSearch"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getPatientByName(familyName, null);
            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(1, 6, patientList);
        }

        @Test
        @DisplayName("GIVEN no familyName nor givenName " +
                "WHEN the uri \"/patient/getPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientByNameNoNameTest() throws Exception {
            //GIVEN
            //no familyName nor givenName
            List<PatientBean> patientList = new ArrayList<>();
            Integer[] listPages = new Integer[3];
            listPages[0] = 1;
            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 1, 1, listPages);
            when(patientUIService.getPatientByName(null, null)).thenReturn(patientList);
            when(patientUIService.getPatientsToDisplay(anyInt(), anyInt(), anyList())).thenReturn(listOfPatientsToDisplay);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getPatient");
            // WHEN
            //the uri "/patient/getPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("selectPatient"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("linkedFrom", "research"))
                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
                    .andExpect(model().attributeExists("patientSearch"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getPatientByName(null, null);
            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(1, 6, patientList);
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("searchPatientByNameTest")
    class SearchPatientsByNameTests {

        @Test
        @DisplayName("GIVEN a patientSearch " +
                "WHEN the uri \"/patient/searchPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientByNameTest() throws Exception {
            //GIVEN
            //a patientSearch
            PatientSearch patientSearch = new PatientSearch();
            patientSearch.setFamilyName("family");
            patientSearch.setGivenName("given");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/searchPatient").flashAttr("patientSearch", patientSearch);
            // WHEN
            //the uri "/patient/searchPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getPatient?familyName=family&givenName=given"))
                    .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("GIVEN a patientSearch with only familyName" +
                "WHEN the uri \"/patient/searchPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientByNameOnlyFamilyNameTest() throws Exception {
            //GIVEN
            //a patientSearch with only familyName
            PatientSearch patientSearch = new PatientSearch();
            patientSearch.setFamilyName("family");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/searchPatient").flashAttr("patientSearch", patientSearch);
            // WHEN
            //the uri "/patient/searchPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getPatient?familyName=family"))
                    .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("GIVEN a patientSearch with only givenName " +
                "WHEN the uri \"/patient/searchPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientByNameOnlyGivenTest() throws Exception {
            //GIVEN
            //a patientSearch with only givenName
            PatientSearch patientSearch = new PatientSearch();
            patientSearch.setGivenName("given");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/searchPatient").flashAttr("patientSearch", patientSearch);
            // WHEN
            //the uri "/patient/searchPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getPatient?givenName=given"))
                    .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("GIVEN a patientSearch empty " +
                "WHEN the uri \"/patient/searchPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientByNameEmptyTest() throws Exception {
            //GIVEN
            //a patientSearch empty
            PatientSearch patientSearch = new PatientSearch();
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/searchPatient").flashAttr("patientSearch", patientSearch);
            // WHEN
            //the uri "/patient/searchPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getPatient"))
                    .andExpect(model().hasNoErrors());
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("getPatientTest")
    class GetPatientTests {

        @Test
        @DisplayName("GIVEN an existing patient " +
                "WHEN the uri \"/patient/getPatient/{patientId}\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientTest() throws Exception {
            //GIVEN
            //an existing patient
            int patientId = 1;
            PatientBean patientBean = new PatientBean(patientId, "familyName", "givenName", "dateOfBirth1", "M", "phone1", "address1", 1);
            List<MedicalNoteBean> medicalNoteList = new ArrayList<>();
            ListOfNotesToDisplay listOfNotesToDisplay = new ListOfNotesToDisplay(medicalNoteList, 1, 1, new Integer[3]);
            String diabetesRisk = "NONE";
            when(patientUIService.getPatientById(patientId)).thenReturn(patientBean);
            when(patientUIService.getDiabetesRisk(patientId)).thenReturn(diabetesRisk);
            when(patientUIService.getMedicalNotesByPatient(String.valueOf(patientId))).thenReturn(medicalNoteList);
            when(patientUIService.createPreviewContentList(medicalNoteList)).thenReturn(medicalNoteList);
            when(patientUIService.getMedicalNotesToDisplay(1, 5, medicalNoteList)).thenReturn(listOfNotesToDisplay);
            when(patientUIService.getMedicalNote(null)).thenReturn(null);

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getPatient/{patientId}", patientId);
            // WHEN
            //the uri "/patient/getPatient/{patientId}" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("patientCard"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("patientBean", patientBean))
                    .andExpect(model().attribute("patientBeanToModify", patientBean))
                    .andExpect(model().attribute("bindingError", false))
                    .andExpect(model().attribute("medicalNotesToDisplay", listOfNotesToDisplay))
                    .andExpect(model().attribute("diabetesRisk", "NONE"))
                    .andExpect(model().attributeDoesNotExist("readingNote"))
                    .andExpect(model().attributeDoesNotExist("updatingNote"))
                    .andExpect(model().attributeExists("writingNote"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getPatientById(patientId);
            verify(patientUIService, Mockito.times(1)).getDiabetesRisk(patientId);
            verify(patientUIService, Mockito.times(1)).getMedicalNotesByPatient(String.valueOf(patientId));
            verify(patientUIService, Mockito.times(1)).createPreviewContentList(medicalNoteList);
            verify(patientUIService, Mockito.times(1)).getMedicalNotesToDisplay(1, 5, medicalNoteList);
            verify(patientUIService, Mockito.times(2)).getMedicalNote(null);
        }

        @Test
        @DisplayName("GIVEN a note to update " +
                "WHEN the uri \"/patient/getPatient/{patientId}\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientUpdatingNoteTest() throws Exception {
            //GIVEN
            //a note to update
            int patientId = 1;
            PatientBean patientBean = new PatientBean(patientId, "familyName", "givenName", "dateOfBirth1", "M", "phone1", "address1", 1);
            List<MedicalNoteBean> medicalNoteList = new ArrayList<>();
            ListOfNotesToDisplay listOfNotesToDisplay = new ListOfNotesToDisplay(medicalNoteList, 1, 1, new Integer[3]);
            String diabetesRisk = "NONE";
            String noteId = "noteId";
            MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
            when(patientUIService.getPatientById(patientId)).thenReturn(patientBean);
            when(patientUIService.getDiabetesRisk(patientId)).thenReturn(diabetesRisk);
            when(patientUIService.getMedicalNotesByPatient(String.valueOf(patientId))).thenReturn(medicalNoteList);
            when(patientUIService.createPreviewContentList(medicalNoteList)).thenReturn(medicalNoteList);
            when(patientUIService.getMedicalNotesToDisplay(1, 5, medicalNoteList)).thenReturn(listOfNotesToDisplay);
            when(patientUIService.getMedicalNote(null)).thenReturn(null);
            when(patientUIService.getMedicalNote(noteId)).thenReturn(medicalNoteBean);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getPatient/{patientId}", patientId).param("updateNoteId", noteId);
            // WHEN
            //the uri "/patient/getPatient/{patientId}" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("patientCard"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("patientBean", patientBean))
                    .andExpect(model().attribute("patientBeanToModify", patientBean))
                    .andExpect(model().attribute("bindingError", false))
                    .andExpect(model().attribute("medicalNotesToDisplay", listOfNotesToDisplay))
                    .andExpect(model().attribute("diabetesRisk", "NONE"))
                    .andExpect(model().attribute("updatingNote", medicalNoteBean))
                    .andExpect(model().attributeDoesNotExist("readingNote"))
                    .andExpect(model().attributeExists("writingNote"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getPatientById(patientId);
            verify(patientUIService, Mockito.times(1)).getDiabetesRisk(patientId);
            verify(patientUIService, Mockito.times(1)).getMedicalNotesByPatient(String.valueOf(patientId));
            verify(patientUIService, Mockito.times(1)).createPreviewContentList(medicalNoteList);
            verify(patientUIService, Mockito.times(1)).getMedicalNotesToDisplay(1, 5, medicalNoteList);
            verify(patientUIService, Mockito.times(1)).getMedicalNote(noteId);
            verify(patientUIService, Mockito.times(1)).getMedicalNote(null);
        }

        @Test
        @DisplayName("GIVEN a note to read " +
                "WHEN the uri \"/patient/getPatient/{patientId}\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientReadingNoteTest() throws Exception {
            //GIVEN
            //a note to read
            int patientId = 1;
            PatientBean patientBean = new PatientBean(patientId, "familyName", "givenName", "dateOfBirth1", "M", "phone1", "address1", 1);
            List<MedicalNoteBean> medicalNoteList = new ArrayList<>();
            ListOfNotesToDisplay listOfNotesToDisplay = new ListOfNotesToDisplay(medicalNoteList, 1, 1, new Integer[3]);
            String diabetesRisk = "NONE";
            String noteId = "noteId";
            MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
            when(patientUIService.getPatientById(patientId)).thenReturn(patientBean);
            when(patientUIService.getDiabetesRisk(patientId)).thenReturn(diabetesRisk);
            when(patientUIService.getMedicalNotesByPatient(String.valueOf(patientId))).thenReturn(medicalNoteList);
            when(patientUIService.createPreviewContentList(medicalNoteList)).thenReturn(medicalNoteList);
            when(patientUIService.getMedicalNotesToDisplay(1, 5, medicalNoteList)).thenReturn(listOfNotesToDisplay);
            when(patientUIService.getMedicalNote(noteId)).thenReturn(medicalNoteBean);
            when(patientUIService.getMedicalNote(null)).thenReturn(null);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getPatient/{patientId}", patientId).param("noteId", noteId);
            // WHEN
            //the uri "/patient/getPatient/{patientId}" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("patientCard"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("patientBean", patientBean))
                    .andExpect(model().attribute("patientBeanToModify", patientBean))
                    .andExpect(model().attribute("bindingError", false))
                    .andExpect(model().attribute("medicalNotesToDisplay", listOfNotesToDisplay))
                    .andExpect(model().attribute("diabetesRisk", "NONE"))
                    .andExpect(model().attribute("readingNote", medicalNoteBean))
                    .andExpect(model().attributeDoesNotExist("updatingNote"))
                    .andExpect(model().attributeExists("writingNote"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getPatientById(patientId);
            verify(patientUIService, Mockito.times(1)).getDiabetesRisk(patientId);
            verify(patientUIService, Mockito.times(1)).getMedicalNotesByPatient(String.valueOf(patientId));
            verify(patientUIService, Mockito.times(1)).createPreviewContentList(medicalNoteList);
            verify(patientUIService, Mockito.times(1)).getMedicalNotesToDisplay(1, 5, medicalNoteList);
            verify(patientUIService, Mockito.times(1)).getMedicalNote(noteId);
            verify(patientUIService, Mockito.times(1)).getMedicalNote(null);
        }

        @Test
        @DisplayName("GIVEN a given page " +
                "WHEN the uri \"/patient/getPatient/{patientId}\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientGivenPageTest() throws Exception {
            //GIVEN
            //an existing patient
            int patientId = 1;
            PatientBean patientBean = new PatientBean(patientId, "familyName", "givenName", "dateOfBirth1", "M", "phone1", "address1", 1);
            List<MedicalNoteBean> medicalNoteList = new ArrayList<>();
            ListOfNotesToDisplay listOfNotesToDisplay = new ListOfNotesToDisplay(medicalNoteList, 4, 5, new Integer[3]);
            String diabetesRisk = "NONE";
            when(patientUIService.getPatientById(patientId)).thenReturn(patientBean);
            when(patientUIService.getDiabetesRisk(patientId)).thenReturn(diabetesRisk);
            when(patientUIService.getMedicalNotesByPatient(String.valueOf(patientId))).thenReturn(medicalNoteList);
            when(patientUIService.createPreviewContentList(medicalNoteList)).thenReturn(medicalNoteList);
            when(patientUIService.getMedicalNotesToDisplay(4, 5, medicalNoteList)).thenReturn(listOfNotesToDisplay);
            when(patientUIService.getMedicalNote(null)).thenReturn(null);

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getPatient/{patientId}", patientId).param("currentPage", "4");
            // WHEN
            //the uri "/patient/getPatient/{patientId}" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("patientCard"))
                    //and correct information is attached to model
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("patientBean", patientBean))
                    .andExpect(model().attribute("patientBeanToModify", patientBean))
                    .andExpect(model().attribute("bindingError", false))
                    .andExpect(model().attribute("medicalNotesToDisplay", listOfNotesToDisplay))
                    .andExpect(model().attribute("diabetesRisk", "NONE"))
                    .andExpect(model().attributeDoesNotExist("readingNote"))
                    .andExpect(model().attributeDoesNotExist("updatingNote"))
                    .andExpect(model().attributeExists("writingNote"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getPatientById(patientId);
            verify(patientUIService, Mockito.times(1)).getDiabetesRisk(patientId);
            verify(patientUIService, Mockito.times(1)).getMedicalNotesByPatient(String.valueOf(patientId));
            verify(patientUIService, Mockito.times(1)).createPreviewContentList(medicalNoteList);
            verify(patientUIService, Mockito.times(1)).getMedicalNotesToDisplay(4, 5, medicalNoteList);
            verify(patientUIService, Mockito.times(2)).getMedicalNote(null);
        }

        @Test
        @DisplayName("GIVEN a non-existing patient " +
                "WHEN the uri \"/patient/getPatient/{patientId}\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void getPatientNonExistingTest() throws Exception {
            //GIVEN
            //a non-existing patient
            int patientId = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(patientUIService.getPatientById(patientId)).thenThrow(objectNotFoundException);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/getPatient/{patientId}", patientId);
            // WHEN
            //the uri "/patient/getPatient/{patientId}" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct template is returned
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"));
            //and the expected methods have been called with expected arguments
            verify(patientUIService, Mockito.times(1)).getPatientById(patientId);
            verify(patientUIService, Mockito.times(0)).getDiabetesRisk(anyInt());
            verify(patientUIService, Mockito.times(0)).getMedicalNotesByPatient(anyString());
            verify(patientUIService, Mockito.times(0)).createPreviewContentList(anyList());
            verify(patientUIService, Mockito.times(0)).getMedicalNotesToDisplay(anyInt(), anyInt(), anyList());
            verify(patientUIService, Mockito.times(0)).getMedicalNote(anyString());
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("submitNewNoteTest")
    class SubmitNewNoteTests {

        @Test
        @DisplayName("GIVEN a medicalNoteBean " +
                "WHEN the uri \"/patient/addNote\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void submitNewNoteTest() throws Exception {
            //GIVEN
            //a medicalNoteBean
            MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
            String id = "1";
            String content = "content";
            medicalNoteBean.setPatientId(id);
            medicalNoteBean.setNoteContent(content);
            doNothing().when(patientUIService).addMedicalNote(id, content);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/addNote").flashAttr("medicalNoteBean", medicalNoteBean);
            // WHEN
            //the uri "/patient/addNote" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getPatient/" + id))
                    .andExpect(model().hasNoErrors());
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("submitUpdateNoteTest")
    class SubmitUpdateNoteTests {

        @Test
        @DisplayName("GIVEN a medicalNoteBean " +
                "WHEN the uri \"/patient/updateNote\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void submitUpdateNoteTest() throws Exception {
            //GIVEN
            //a medicalNoteBean
            MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
            String id = "id";
            String patientId = "1";
            String content = "content";
            medicalNoteBean.setId(id);
            medicalNoteBean.setNoteContent(content);
            medicalNoteBean.setPatientId(patientId);
            doNothing().when(patientUIService).updateMedicalNote(id, content);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/updateNote").flashAttr("medicalNoteBean", medicalNoteBean);
            // WHEN
            //the uri "/patient/updateNote" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getPatient/" + patientId))
                    .andExpect(model().hasNoErrors());
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("deleteNoteTest")
    class DeleteNoteTests {

        @Test
        @DisplayName("GIVEN a medicalNote id and a patient id " +
                "WHEN the uri \"/patient/deleteNote/{patientId}/{noteId}\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void deleteNoteTest() throws Exception {
            //GIVEN
            //a medicalNote id and a patient id
            String noteId = "id";
            int patientId = 1;
            doNothing().when(patientUIService).deleteMedicalNote(noteId);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/deleteNote/{patientId}/{noteId}", patientId, noteId);
            // WHEN
            //the uri "/patient/deleteNote/{patientId}/{noteId}" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getPatient/" + patientId))
                    .andExpect(model().hasNoErrors());
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("deletePatientTest")
    class DeletePatientTests {

        @Test
        @DisplayName("GIVEN a patient id " +
                "WHEN the uri \"/patient/delete/{patientId}\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void deleteNoteTest() throws Exception {
            //GIVEN
            // a patient id
            int patientId = 1;
            doNothing().when(patientUIService).deletePatient(patientId);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/delete/{patientId}", patientId);
            // WHEN
            //the uri "/patient/delete/{patientId}" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getAllPatients"))
                    .andExpect(model().hasNoErrors());
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("addNewPatientTest")
    class AddNewPatientTests {

        @Test
        @DisplayName("GIVEN " +
                "WHEN the uri \"/patient/addPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void addNewPatientTest() throws Exception {
            //GIVEN
            //

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/patient/addPatient");
            // WHEN
            //the uri "/patient/addPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("patientBean"))
                    .andExpect(view().name("newPatientForm"));
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("createPatientTest")
    class CreatePatientTests {

        @Test
        @DisplayName("GIVEN a patientBean with no error " +
                "WHEN the uri \"/patient/addPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientTest() throws Exception {
            //GIVEN
            //a patientBean with no error
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family");
            patientBean.setGivenName("given");
            patientBean.setSex("M");
            patientBean.setDateOfBirth("2000-01-01");
            patientBean.setPhone("111-111-1111");
            patientBean.setAddress("address");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/addPatient").flashAttr("patientBean", patientBean);
            doNothing().when(patientUIService).addNewPatient(patientBean);
            // WHEN
            //the uri "/patient/addPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getAllPatients"))
                    .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("GIVEN a patientBean with only required information " +
                "WHEN the uri \"/patient/addPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientOnlyRequiredTest() throws Exception {
            //GIVEN
            //a patientBean with only required information
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family");
            patientBean.setGivenName("given");
            patientBean.setSex("M");
            patientBean.setDateOfBirth("2000-01-01");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/addPatient").flashAttr("patientBean", patientBean);
            doNothing().when(patientUIService).addNewPatient(patientBean);
            // WHEN
            //the uri "/patient/addPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getAllPatients"))
                    .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("GIVEN a patientBean with errors " +
                "WHEN the uri \"/patient/addPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientErrorsTest() throws Exception {
            //GIVEN
            //a patientBean with  error
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family");
            patientBean.setGivenName("given");
            patientBean.setSex("sex");
            patientBean.setDateOfBirth("7777777");
            patientBean.setPhone("111111");
            patientBean.setAddress("address");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/addPatient").flashAttr("patientBean", patientBean);
            // WHEN
            //the uri "/patient/addPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(3))
                    .andExpect(view().name("newPatientForm"));
        }

        @Test
        @DisplayName("GIVEN a patientBean with missing information " +
                "WHEN the uri \"/patient/addPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientMissingTest() throws Exception {
            //GIVEN
            //a patientBean with missing information
            PatientBean patientBean = new PatientBean();
            patientBean.setPhone("111-111-1111");
            patientBean.setAddress("address");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/addPatient").flashAttr("patientBean", patientBean);
            // WHEN
            //the uri "/patient/addPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(4))
                    .andExpect(view().name("newPatientForm"));
        }

        @Test
        @DisplayName("GIVEN a patientBean with too long information " +
                "WHEN the uri \"/patient/addPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientTooLongTest() throws Exception {
            //GIVEN
            //a patientBean with too long information
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family123456789012345678901234567890123456789012345678901234567890");
            patientBean.setGivenName("given123456789012345678901234567890123456789012345678901234567890");
            patientBean.setSex("M");
            patientBean.setDateOfBirth("2000-01-01");
            patientBean.setPhone("111-111-1111");
            patientBean.setAddress("address123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/addPatient").flashAttr("patientBean", patientBean);
            // WHEN
            //the uri "/patient/addPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(3))
                    .andExpect(view().name("newPatientForm"));
        }

        @Test
        @DisplayName("GIVEN a patientBean with error date day" +
                "WHEN the uri \"/patient/addPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientErrorDateDayTest() throws Exception {
            //GIVEN
            //a patientBean with error date day
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family");
            patientBean.setGivenName("given");
            patientBean.setSex("M");
            patientBean.setDateOfBirth("2000-01-56");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/addPatient").flashAttr("patientBean", patientBean);
            // WHEN
            //the uri "/patient/addPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(1))
                    .andExpect(view().name("newPatientForm"));
        }

        @Test
        @DisplayName("GIVEN a patientBean with error date month" +
                "WHEN the uri \"/patient/addPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientErrorDateMonthTest() throws Exception {
            //GIVEN
            //a patientBean with error date month
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family");
            patientBean.setGivenName("given");
            patientBean.setSex("M");
            patientBean.setDateOfBirth("2000-48-01");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/addPatient").flashAttr("patientBean", patientBean);
            // WHEN
            //the uri "/patient/addPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(1))
                    .andExpect(view().name("newPatientForm"));
        }

        @Test
        @DisplayName("GIVEN a patientBean with error date year" +
                "WHEN the uri \"/patient/addPatient\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientErrorDateYearTest() throws Exception {
            //GIVEN
            //a patientBean with error date year
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family");
            patientBean.setGivenName("given");
            patientBean.setSex("M");
            patientBean.setDateOfBirth("1000-01-01");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/addPatient").flashAttr("patientBean", patientBean);
            // WHEN
            //the uri "/patient/addPatient" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(1))
                    .andExpect(view().name("newPatientForm"));
        }
    }

    @Nested
    @Tag("PatientControllerTests")
    @DisplayName("updatePatientTest")
    class UpdatePatientTests {

        @Test
        @DisplayName("GIVEN a patientBean with no error " +
                "WHEN the uri \"/patient/updatePatient/{patientId}\" is called, " +
                "THEN the correct template is returned with correct information attached to model.")
        void updatePatientTest() throws Exception {
            //GIVEN
            //a patientBean with no error
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family");
            patientBean.setGivenName("given");
            patientBean.setSex("M");
            patientBean.setDateOfBirth("2000-01-01");
            patientBean.setPhone("111-111-1111");
            patientBean.setAddress("address");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/updatePatient/{patientId}",1).flashAttr("patientBeanToModify", patientBean);
            doNothing().when(patientUIService).updatePatient(1,patientBean);
            // WHEN
            //the uri "/patient/updatePatient/{patientId}" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getPatient/1"))
                    .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("GIVEN a patientBean with only required information " +
                "WHEN the uri \"/patient/updatePatient/{patientId}\" is called," +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientOnlyRequiredTest() throws Exception {
            //GIVEN
            //a patientBean with only required information
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family");
            patientBean.setGivenName("given");
            patientBean.setSex("M");
            patientBean.setDateOfBirth("2000-01-01");
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/updatePatient/{patientId}",1).flashAttr("patientBeanToModify", patientBean);
            doNothing().when(patientUIService).updatePatient(1,patientBean);
            // WHEN
            //the uri "/patient/updatePatient/{patientId}" is called,
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/patient/getPatient/1"))
                    .andExpect(model().hasNoErrors());
        }

        @Test
        @DisplayName("GIVEN a patientBean with errors " +
                "WHEN the uri \"/patient/updatePatient/{patientId}\" is called" +
                "THEN the correct template is returned with correct information attached to model.")
        void createPatientErrorsTest() throws Exception {
            //GIVEN
            //a patientBean with  error
            PatientBean patientBean = new PatientBean();
            patientBean.setFamilyName("family");
            patientBean.setGivenName("given");
            patientBean.setSex("sex");
            patientBean.setDateOfBirth("7777777");
            patientBean.setPhone("111111");
            patientBean.setAddress("address");


            PatientBean patientBean2 = new PatientBean();
            patientBean2.setFamilyName("family");
            patientBean2.setGivenName("given");
            patientBean2.setSex("M");
            patientBean2.setDateOfBirth("2000-01-01");
            patientBean2.setPhone("111-111-1111");
            patientBean2.setAddress("address");

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/patient/updatePatient/{patientId}",1).flashAttr("patientBeanToModify", patientBean).flashAttr("medicalNotesToDisplay", new ArrayList<>()).flashAttr("diabetesRisk", "NONE");

            when(patientUIService.getPatientById(1)).thenReturn(patientBean2);
            when(patientUIService.getDiabetesRisk(1)).thenReturn("NONE");
            when(patientUIService.getMedicalNotesByPatient("1")).thenReturn(new ArrayList<>());
            when(patientUIService.createPreviewContentList(anyList())).thenReturn(new ArrayList<>());
            when(patientUIService.getMedicalNotesToDisplay(anyInt(), anyInt(), anyList())).thenReturn(new ListOfNotesToDisplay(new ArrayList<>(),1,1,new Integer[3]));
            // WHEN
            //the uri \"/patient/updatePatient/{patientId}\" is called",
            mockMvc.perform(requestBuilder)
                    // THEN
                    //the correct information is returned
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(3))
                    .andExpect(view().name("patientCard"))
                    .andExpect(model().attributeExists("patientBean"))
                    .andExpect(model().attribute("bindingError", true));
        }
    }

}