package mediscreen.patientUI.unitTest;

import lombok.extern.slf4j.Slf4j;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import mediscreen.patientUI.patientUIService.PatientUIService;
import mediscreen.patientUI.proxy.PatientInformationProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
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
    private PatientInformationProxy patientInformationProxy;
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
            when(patientInformationProxy.getAllPatient()).thenReturn(patientList);
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
            assertTrue(response.getResponse().getContentAsString().contains("Select a patient in the list to visualize his card :"));
            assertFalse(response.getResponse().getContentAsString().contains("There is not any patient registered yet."));
            assertFalse(response.getResponse().getContentAsString().contains("Result for searching"));
            assertFalse(response.getResponse().getContentAsString().contains("Sorry, there is no result for your research."));
            assertFalse(response.getResponse().getContentAsString().contains("See all patient list"));
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getAllPatient();
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
            when(patientInformationProxy.getAllPatient()).thenReturn(patientList);
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
            verify(patientInformationProxy, Mockito.times(1)).getAllPatient();
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
            when(patientInformationProxy.getAllPatient()).thenReturn(patientList);
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
            assertTrue(response.getResponse().getContentAsString().contains("Select a patient in the list to visualize his card :"));
            assertFalse(response.getResponse().getContentAsString().contains("There is not any patient registered yet."));
            assertFalse(response.getResponse().getContentAsString().contains("Result for searching"));
            assertFalse(response.getResponse().getContentAsString().contains("Sorry, there is no result for your research."));
            assertFalse(response.getResponse().getContentAsString().contains("See all patient list"));
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getAllPatient();
            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(5, 6, patientList);
        }
    }

//    @Nested
//    @Tag("PatientControllerTests")
//    @DisplayName("getPatientByNameTest")
//    class GetPatientByNameTests {
//        @Test
//        @DisplayName("GIVEN a list of patient to display and no given page " +
//                "WHEN the uri \"/patient/getPatient\" is called, " +
//                "THEN the correct template is returned with correct information attached to model.")
//        void getPatientByNameTest() throws Exception {
//            //GIVEN
//            ModelAndView model;
//            List<PatientBean> patientList = new ArrayList<>();
//            String familyName = "familyName1";
//            String givenName = "givenName1";
//            model = new ModelAndView();
//            model.addObject("familyName", familyName);
//            model.addObject("givenName", givenName);
//            PatientBean patientBean = new PatientBean(1, familyName, givenName, "dateOfBirth1", "M", "phone1", "address1", 1);
//            patientList.add(patientBean);
//            System.out.println(patientList + "size : " + patientList.size());
//            System.out.println(patientBean);
//            Integer[] listPages = new Integer[3];
//            listPages[0] = 1;
//            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 1, 1, listPages);
//            when(patientInformationProxy.getPatientByName(familyName, givenName)).thenReturn(patientList);
//            when(patientUIService.getPatientsToDisplay(anyInt(), anyInt(), anyList())).thenReturn(listOfPatientsToDisplay);
//            RequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .get("/patient/getPatient");
//            // WHEN
//            //the uri "/login" is called,
//            MvcResult response = mockMvc.perform(requestBuilder)
//                    // THEN
//                    //the correct template is returned
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("selectPatient"))
//                    //and correct information is attached to model
//                    .andExpect(model().hasNoErrors())
//                    .andExpect(model().attribute("linkedFrom", "research"))
//                    .andExpect(model().attribute("familyName", familyName))
//                    .andExpect(model().attribute("givenName", givenName))
//                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
//                    .andExpect(model().attributeExists("patientSearch"))
//                    .andReturn();
//            //and correct information is displayed by view
//            assertTrue(response.getResponse().getContentAsString().contains("Result for searching familyName1 givenName1"));
//            assertTrue(response.getResponse().getContentAsString().contains("Select a patient in the list to visualize his card :"));
//            assertTrue(response.getResponse().getContentAsString().contains("1"));
//            assertTrue(response.getResponse().getContentAsString().contains("See all patient list"));
//            assertFalse(response.getResponse().getContentAsString().contains("2"));
//            assertFalse(response.getResponse().getContentAsString().contains("3"));
//            assertFalse(response.getResponse().getContentAsString().contains("All patients list"));
//            assertFalse(response.getResponse().getContentAsString().contains("There is not any patient registered yet."));
//            assertFalse(response.getResponse().getContentAsString().contains("Sorry, there is no result for your research."));
//
//            //and the expected methods have been called with expected arguments
//            verify(patientInformationProxy, Mockito.times(1)).getPatientByName(familyName, givenName);
//            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(1, 6, patientList);
//        }
//
//        @Test
//        @DisplayName("GIVEN an empty list of patient to display and no given page " +
//                "WHEN the uri \"/patient/getAllPatients\" is called, " +
//                "THEN the correct template is returned with correct information attached to model.")
//        void getAllPatientsEmptyListTest() throws Exception {
//            //GIVEN
//            List<PatientBean> patientList = new ArrayList<>();
//            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 1, 0, new Integer[3]);
//            when(patientInformationProxy.getAllPatient()).thenReturn(patientList);
//            when(patientUIService.getPatientsToDisplay(1, 6, patientList)).thenReturn(listOfPatientsToDisplay);
//            RequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .get("/patient/getAllPatients");
//            // WHEN
//            //the uri "/login" is called,
//            MvcResult response = mockMvc.perform(requestBuilder)
//                    // THEN
//                    //the correct template is returned
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("selectPatient"))
//                    //and correct information is attached to model
//                    .andExpect(model().hasNoErrors())
//                    .andExpect(model().attribute("linkedFrom", "allPatients"))
//                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
//                    .andExpect(model().attributeExists("patientSearch"))
//                    .andReturn();
//            //and correct information is displayed by view
//            assertTrue(response.getResponse().getContentAsString().contains("All patients list"));
//            assertTrue(response.getResponse().getContentAsString().contains("There is not any patient registered yet."));
//            assertFalse(response.getResponse().getContentAsString().contains("Result for searching"));
//            assertFalse(response.getResponse().getContentAsString().contains("Sorry, there is no result for your research."));
//            assertFalse(response.getResponse().getContentAsString().contains("See all patient list"));
//            //and the expected methods have been called with expected arguments
//            verify(patientInformationProxy, Mockito.times(1)).getAllPatient();
//            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(1, 6, patientList);
//        }
//
//        @Test
//        @DisplayName("GIVEN a list of patient to display and a given page " +
//                "WHEN the uri \"/patient/getAllPatients\" is called, " +
//                "THEN the correct template is returned with correct information attached to model.")
//        void getAllPatientsPageNumberTest() throws Exception {
//            //GIVEN
//            List<PatientBean> patientList = new ArrayList<>();
//            for (int i = 0; i < 6; i++) {
//                PatientBean patientBean = new PatientBean(i, "familyName" + i, "givenName" + i, "dateOfBirth" + i, "sex" + i, "phone" + i, "address" + i, i);
//                patientList.add(patientBean);
//            }
//            Integer[] listPages = new Integer[3];
//            listPages[0] = 4;
//            listPages[1] = 5;
//            listPages[2] = 6;
//            ListOfPatientsToDisplay listOfPatientsToDisplay = new ListOfPatientsToDisplay(patientList, 5, 6, listPages);
//            when(patientInformationProxy.getAllPatient()).thenReturn(patientList);
//            when(patientUIService.getPatientsToDisplay(5, 6, patientList)).thenReturn(listOfPatientsToDisplay);
//            RequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .get("/patient/getAllPatients").param("currentPage", String.valueOf(5));
//            // WHEN
//            //the uri "/login" is called,
//            MvcResult response = mockMvc.perform(requestBuilder)
//                    // THEN
//                    //the correct template is returned
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("selectPatient"))
//                    //and correct information is attached to model
//                    .andExpect(model().hasNoErrors())
//                    .andExpect(model().attribute("linkedFrom", "allPatients"))
//                    .andExpect(model().attribute("listOfPatientsToDisplay", listOfPatientsToDisplay))
//                    .andExpect(model().attributeExists("patientSearch"))
//                    .andReturn();
//            //and correct information is displayed by view
//            assertTrue(response.getResponse().getContentAsString().contains("All patients list"));
//            assertTrue(response.getResponse().getContentAsString().contains("4"));
//            assertTrue(response.getResponse().getContentAsString().contains("5"));
//            assertTrue(response.getResponse().getContentAsString().contains("6"));
//            assertTrue(response.getResponse().getContentAsString().contains("Select a patient in the list to visualize his card :"));
//            assertFalse(response.getResponse().getContentAsString().contains("There is not any patient registered yet."));
//            assertFalse(response.getResponse().getContentAsString().contains("Result for searching"));
//            assertFalse(response.getResponse().getContentAsString().contains("Sorry, there is no result for your research."));
//            assertFalse(response.getResponse().getContentAsString().contains("See all patient list"));
//            //and the expected methods have been called with expected arguments
//            verify(patientInformationProxy, Mockito.times(1)).getAllPatient();
//            verify(patientUIService, Mockito.times(1)).getPatientsToDisplay(5, 6, patientList);
//        }
//    }
//
//
//    @Nested
//    @Tag("PatientControllerTests")
//    @DisplayName("ShowInscriptionTest")
//    class ShowInscriptionTests {
//        @Test
//        @DisplayName("GIVEN no connected user" +
//                "WHEN the uri \"/inscription\" is called, " +
//                "THEN the status is \"isOk\" and correct information is attached to model.")
//        void showInscriptionTest() throws Exception {
//            //GIVEN
//            //no connected user
//            RequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .get("/inscription");
//            when(patientInformationProxy.getCurrentUserMail()).thenReturn(null);
//            // WHEN
//            //the uri "/inscription" is called,
//            mockMvc.perform(requestBuilder)
//                    // THEN
//                    //the status is "isOk" and correct information is attached to model.
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("inscription"))
//                    .andExpect(model().hasNoErrors())
//                    .andExpect(model().attribute("activePage", "Inscription"))
//                    .andExpect(model().attributeExists("personDTO"));
//        }
//    }
//
//
//    @Nested
//    @Tag("PatientControllerTests")
//    @DisplayName("ContactTest")
//    class ContactTests {
//        @Test
//        @DisplayName("GIVEN a connected user found by service and no error message " +
//                "WHEN the uri /home is called, " +
//                "THEN the status is isOk and correct information is attached to model.")
//        void contactTest() throws Exception {
//            //GIVEN
//            //a connected user found by service and no error message
//            RequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .get("/home/contact");
//            // WHEN
//            //the uri "/home" is called,
//            mockMvc.perform(requestBuilder)
//                    // THEN
//                    //the status is "isOk" and correct information is attached to model.
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("contact"))
//                    .andExpect(model().hasNoErrors())
//                    .andExpect(model().attribute("activePage", "Contact"))
//                    .andExpect(model().attributeExists("messageDTO"))
//                    .andExpect(model().attributeDoesNotExist("personDTO"))
//                    .andExpect(model().attributeDoesNotExist("message"));
//            //and the expected methods have been called with expected arguments
//            verify(patientInformationProxy, Mockito.times(1)).getCurrentUserMail();
//            verify(patientInformationProxy, Mockito.times(0)).getPersonDTO(anyString());
//        }
//
//        @Test
//        @DisplayName("GIVEN a connected user found by service and no error message " +
//                "WHEN the uri /home is called, " +
//                "THEN the status is isOk and correct information is attached to model.")
//        void contactConnectedTest() throws Exception {
//            //GIVEN
//            //a connected user found by service and no error message
//            String email = "person1@mail.fr";
//            String password = "password1";
//            String firstName = "firstName1";
//            String lastname = "lastName1";
//            PersonDTO personDTOcreated = new PersonDTO(email, password, firstName, lastname);
//            when(patientInformationProxy.getCurrentUserMail()).thenReturn(email);
//            when(patientInformationProxy.getPersonDTO(email)).thenReturn(personDTOcreated);
//            RequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .get("/home/contact");
//            // WHEN
//            //the uri "/home" is called,
//            mockMvc.perform(requestBuilder)
//                    // THEN
//                    //the status is "isOk" and correct information is attached to model.
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("contact"))
//                    .andExpect(model().hasNoErrors())
//                    .andExpect(model().attribute("activePage", "Contact"))
//                    .andExpect(model().attributeExists("messageDTO"))
//                    .andExpect(model().attributeExists("personDTO"))
//                    .andExpect(model().attributeDoesNotExist("message"));
//            //and the expected methods have been called with expected arguments
//            verify(patientInformationProxy, Mockito.times(1)).getCurrentUserMail();
//            verify(patientInformationProxy, Mockito.times(1)).getPersonDTO(anyString());
//        }
//
//        @Test
//        @DisplayName("GIVEN a connected user found by service and no error message " +
//                "WHEN the uri /home is called, " +
//                "THEN the status is isOk and correct information is attached to model.")
//        void contactErrorTest() throws Exception {
//            //GIVEN
//            //a connected user found by service and no error message
//            String email = "person1@mail.fr";
//            String password = "password1";
//            String firstName = "firstName1";
//            String lastname = "lastName1";
//            PersonDTO personDTO = new PersonDTO(email, password, firstName, lastname);
//            when(patientInformationProxy.getCurrentUserMail()).thenReturn(email);
//            when(patientInformationProxy.getPersonDTO(email)).thenReturn(personDTO);
//            RequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .get("/home/contact")
//                    .param("message", "error message");
//            // WHEN
//            //the uri "/home" is called,
//            mockMvc.perform(requestBuilder)
//                    // THEN
//                    //the status is "isOk" and correct information is attached to model.
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("contact"))
//                    .andExpect(model().hasNoErrors())
//                    .andExpect(model().attribute("activePage", "Contact"))
//                    .andExpect(model().attribute("message", "error message"))
//                    .andExpect(model().attributeExists("messageDTO"))
//                    .andExpect(model().attributeExists("personDTO"));
//            //and the expected methods have been called with expected arguments
//            verify(patientInformationProxy, Mockito.times(1)).getCurrentUserMail();
//            verify(patientInformationProxy, Mockito.times(1)).getPersonDTO(anyString());
//        }
//    }
//
//    @Nested
//    @Tag("PatientControllerTests")
//    @DisplayName("SendMessageTest")
//    class SendMessageTests {
//        @Test
//        @DisplayName("GIVEN a connected user found by service and no error message " +
//                "WHEN the uri /home is called, " +
//                "THEN the status is isOk and correct information is attached to model.")
//        void sendMessageWrongEmailTest() throws Exception {
//            //GIVEN
//            //a connected user found by service and no error message
//            String subject = "subject";
//            String message = "message";
//            String firstName = "firstName";
//            String lastName = "lastName";
//            String email = "email";
//            RequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .post("/home/contact")
//                    .param("subject", subject)
//                    .param("message", message)
//                    .param("firstName", firstName)
//                    .param("lastName", lastName)
//                    .param("email", email);
//            when(patientInformationProxy.getCurrentUserMail()).thenReturn(null);
//            // WHEN
//            //the uri "/home" is called,
//            mockMvc.perform(requestBuilder)
//                    // THEN
//                    //the status is "isOk" and correct information is attached to model.
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("contact"))
//                    .andExpect(model().hasErrors())
//                    .andExpect(model().errorCount(1))
//                    .andExpect(model().attribute("activePage", "Contact"))
//                    .andExpect(model().attributeDoesNotExist("personDTO"))
//                    .andExpect(model().attributeExists("messageDTO"));
//            //and the expected methods have been called with expected arguments
//            verify(patientInformationProxy, Mockito.times(1)).getCurrentUserMail();
//            verify(patientInformationProxy, Mockito.times(0)).getPersonDTO(anyString());
//        }
//
//        @Test
//        @DisplayName("GIVEN a connected user found by service and no error message " +
//                "WHEN the uri /home is called, " +
//                "THEN the status is isOk and correct information is attached to model.")
//        void sendMessageMissingInformationTest() throws Exception {
//            //GIVEN
//            //a connected user found by service and no error message
//            MessageDTO messageDTO = new MessageDTO();
//            RequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .post("/home/contact")
//                    .param("subject", messageDTO.getSubject())
//                    .param("message", messageDTO.getMessage())
//                    .param("firstName", messageDTO.getFirstName())
//                    .param("lastName", messageDTO.getLastName())
//                    .param("email", messageDTO.getEmail());
//            when(patientInformationProxy.getCurrentUserMail()).thenReturn(null);
//            // WHEN
//            //the uri "/home" is called,
//            mockMvc.perform(requestBuilder)
//                    // THEN
//                    //the status is "isOk" and correct information is attached to model.
//                    .andExpect(status().isOk())
//                    .andExpect(view().name("contact"))
//                    .andExpect(model().hasErrors())
//                    .andExpect(model().errorCount(3))
//                    .andExpect(model().attribute("activePage", "Contact"))
//                    .andExpect(model().attributeDoesNotExist("personDTO"))
//                    .andExpect(model().attributeExists("messageDTO"));
//            //and the expected methods have been called with expected arguments
//            verify(patientInformationProxy, Mockito.times(1)).getCurrentUserMail();
//            verify(patientInformationProxy, Mockito.times(0)).getPersonDTO(anyString());
//        }
//    }
}