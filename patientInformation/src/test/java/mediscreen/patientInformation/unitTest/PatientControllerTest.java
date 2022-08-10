package mediscreen.patientInformation.unitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import mediscreen.patientInformation.exception.ObjectNotFoundException;
import mediscreen.patientInformation.modele.InfoPatientToUpdateDTO;
import mediscreen.patientInformation.modele.PatientDTO;
import mediscreen.patientInformation.service.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("patientControllerTest")
@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @MockBean
    private PatientService patientService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @Tag("GetAllPatientTest")
    @DisplayName("getAllPatients tests:")
    class GetAllPatientsTest {


        @DisplayName("GIVEN a list of patientDTO " +
                "WHEN the uri \"/patient/getAll\" is called " +
                "THEN the expected list is returned.")
        @Test
        void getAllPatientsTest() throws Exception {

            //GIVEN
            //a list of PatientDTO
            List<PatientDTO> patientList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                PatientDTO patient = new PatientDTO("family" + i, "given" + i, "200" + i + "-0" + (i + 1) + "-0" + (i + 1), "M", "phone" + i, "address" + i);
                patientList.add(patient);
            }
            when(patientService.getAllPatients()).thenReturn(patientList);

            //WHEN
            // the uri "/patient/getAll" is called
            mockMvc.perform(get("/patient/getAll"))

                    //THEN
                    //the expected list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(5)))
                    .andExpect(jsonPath("$.[0].familyName", is("family0")))
                    .andExpect(jsonPath("$.[1].familyName", is("family1")))
                    .andExpect(jsonPath("$.[2].familyName", is("family2")))
                    .andExpect(jsonPath("$.[3].familyName", is("family3")))
                    .andExpect(jsonPath("$.[4].familyName", is("family4")))
                    .andExpect(jsonPath("$.[0].givenName", is("given0")))
                    .andExpect(jsonPath("$.[1].givenName", is("given1")))
                    .andExpect(jsonPath("$.[2].givenName", is("given2")))
                    .andExpect(jsonPath("$.[3].givenName", is("given3")))
                    .andExpect(jsonPath("$.[4].givenName", is("given4")));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).getAllPatients();
        }

        @DisplayName("GIVEN an empty list of patientDTO " +
                "WHEN the uri \"/patient/getAll\" is called " +
                "THEN an empty list is returned.")
        @Test
        void getAllPatientsEmptyListTest() throws Exception {

            //GIVEN
            //an empty list of PatientDTO
            List<PatientDTO> patientList = new ArrayList<>();
            when(patientService.getAllPatients()).thenReturn(patientList);

            //WHEN
            // the uri "/patient/getAll" is called
            mockMvc.perform(get("/patient/getAll"))

                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).getAllPatients();
        }
    }

    @Nested
    @Tag("GetPatientByIdTest")
    @DisplayName("getPatientById tests:")
    class GetPatientByIdTest {


        @DisplayName("GIVEN an existing patient " +
                "WHEN the uri \"/patient/getPatient/{patientId}\" is called " +
                "THEN the expected patientDTO is returned.")
        @Test
        void getPatientByIdTest() throws Exception {

            //GIVEN
            //an existing patient

            PatientDTO patient = new PatientDTO(1, "family", "given", "2000-01-01", "M", "phone", "address", 5);
            when(patientService.getPatientById(1)).thenReturn(patient);

            //WHEN
            // the uri "/patient/getPatient/{patientId}" is called
            mockMvc.perform(get("/patient/getPatient/1"))

                    //THEN
                    //the expected patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.familyName", is("family")))
                    .andExpect(jsonPath("$.givenName", is("given")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2000-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("phone")))
                    .andExpect(jsonPath("$.address", is("address")));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).getPatientById(1);
        }

        @DisplayName("GIVEN a non-existing patient " +
                "WHEN the uri \"/patient/getPatient/{patientId}\" is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getPatientByIdNonExistingTest() throws Exception {

            //GIVEN
            //a non-existing patient

            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(patientService.getPatientById(1)).thenThrow(objectNotFoundException);

            //WHEN
            // the uri "/patient/getPatient/{patientId}" is called
            mockMvc.perform(get("/patient/getPatient/1"))

                    //THEN
                    //an ObjectNotFoundException is thrown with the expected error message
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("error message"));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).getPatientById(1);
        }
    }

    @Nested
    @Tag("GetPatientByNameTest")
    @DisplayName("getPatientByName tests:")
    class GetPatientByNameTest {


        @DisplayName("GIVEN existing familyName and givenName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @Test
        void getPatientByNameTest() throws Exception {

            //GIVEN
            // existing familyName and givenName
            String familyName = "family";
            String givenName = "given";
            PatientDTO patient = new PatientDTO(familyName, givenName, "2000-01-01", "M", "phone", "address");
            when(patientService.getPatientsByName(familyName, givenName)).thenReturn(List.of(patient));

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName)
                            .param("givenName", givenName))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$.[0].familyName", is("family")))
                    .andExpect(jsonPath("$.[0].givenName", is("given")))
                    .andExpect(jsonPath("$.[0].dateOfBirth", is("2000-01-01")))
                    .andExpect(jsonPath("$.[0].sex", is("M")))
                    .andExpect(jsonPath("$.[0].phone", is("phone")))
                    .andExpect(jsonPath("$.[0].address", is("address")));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).getPatientsByName(familyName, givenName);
        }

        @DisplayName("GIVEN existing familyName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @Test
        void getPatientByNameOnlyFamilyNameTest() throws Exception {

            //GIVEN
            // existing familyName
            String familyName = "family";
            PatientDTO patient = new PatientDTO(familyName, "given", "2000-01-01", "M", "phone", "address");
            when(patientService.getPatientsByName(familyName, null)).thenReturn(List.of(patient));

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$.[0].familyName", is("family")))
                    .andExpect(jsonPath("$.[0].givenName", is("given")))
                    .andExpect(jsonPath("$.[0].dateOfBirth", is("2000-01-01")))
                    .andExpect(jsonPath("$.[0].sex", is("M")))
                    .andExpect(jsonPath("$.[0].phone", is("phone")))
                    .andExpect(jsonPath("$.[0].address", is("address")));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).getPatientsByName(familyName, null);
        }

        @DisplayName("GIVEN existing givenName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @Test
        void getPatientByNameOnlyGivenNameTest() throws Exception {

            //GIVEN
            // existing  givenName
            String givenName = "given";
            PatientDTO patient = new PatientDTO("family", givenName, "2000-01-01", "M", "phone", "address");
            when(patientService.getPatientsByName(null, givenName)).thenReturn(List.of(patient));

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("givenName", givenName))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$.[0].familyName", is("family")))
                    .andExpect(jsonPath("$.[0].givenName", is("given")))
                    .andExpect(jsonPath("$.[0].dateOfBirth", is("2000-01-01")))
                    .andExpect(jsonPath("$.[0].sex", is("M")))
                    .andExpect(jsonPath("$.[0].phone", is("phone")))
                    .andExpect(jsonPath("$.[0].address", is("address")));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).getPatientsByName(null, givenName);
        }

        @DisplayName("GIVEN no parameter " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @Test
        void getPatientByNameNoParameterTest() throws Exception {

            //GIVEN
            // existing no parameter
            when(patientService.getPatientsByName(null, null)).thenReturn(List.of());

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient"))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).getPatientsByName(null, null);
        }
    }

    @Nested
    @Tag("AddNewPatientTest")
    @DisplayName("addNewPatient tests:")
    class AddNewPatientTest {

        @DisplayName("GIVEN a patientDTO containing all information " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected message is returned.")
        @Test
        void addNewPatientTest() throws Exception {

            //GIVEN
            // a patientDTO containing all information
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            final ArgumentCaptor<PatientDTO> arg = ArgumentCaptor.forClass(PatientDTO.class);
            doNothing().when(patientService).addNewPatient(any(PatientDTO.class));
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isCreated())
                    .andExpect(content().string("The patient named " + familyName + " " + givenName + " has been created."));
            // and the expected methods have been called with expected arguments
            verify(patientService).addNewPatient(arg.capture());
            assertEquals(familyName, arg.getValue().getFamilyName());
            assertEquals(givenName, arg.getValue().getGivenName());
            assertEquals(dateOfBirth, arg.getValue().getDateOfBirth());
            assertEquals(sex, arg.getValue().getSex());
            assertEquals(phone, arg.getValue().getPhone());
            assertEquals(address, arg.getValue().getAddress());
        }

        @DisplayName("GIVEN a patientDTO containing only required parameters " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected message is returned.")
        @Test
        void addNewPatientOnlyRequiredParametersTest() throws Exception {

            //GIVEN
            // a patientDTO containing only required parameters
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex);
            final ArgumentCaptor<PatientDTO> arg = ArgumentCaptor.forClass(PatientDTO.class);
            doNothing().when(patientService).addNewPatient(any(PatientDTO.class));
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isCreated())
                    .andExpect(content().string("The patient named " + familyName + " " + givenName + " has been created."));
            // and the expected methods have been called with expected arguments
            verify(patientService).addNewPatient(arg.capture());
            assertEquals(familyName, arg.getValue().getFamilyName());
            assertEquals(givenName, arg.getValue().getGivenName());
            assertEquals(dateOfBirth, arg.getValue().getDateOfBirth());
            assertEquals(sex, arg.getValue().getSex());
            assertNull(arg.getValue().getPhone());
            assertNull(arg.getValue().getAddress());
        }

        @DisplayName("GIVEN a patientDTO with missing family name " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientMissingFamilyNameTest() throws Exception {

            //GIVEN
            // a patientDTO with missing family name
            String givenName = "given";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(null, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Family name is mandatory."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with too long family name " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientTooLongFamilyNameTest() throws Exception {

            //GIVEN
            // a patientDTO with too long family name
            String familyName = "familyName123456789012345678901234567890123456789012345678901234567890";
            String givenName = "given";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Family name cannot be over 50 characters."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with missing given name " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientMissingGivenNameTest() throws Exception {

            //GIVEN
            // a patientDTO with missing given name
            String familyName = "family";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, null, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Given name is mandatory."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with too long given name " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientTooLongGivenNameTest() throws Exception {

            //GIVEN
            // a patientDTO with too long given name
            String familyName = "familyName";
            String givenName = "givenName123456789012345678901234567890123456789012345678901234567890";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Given name cannot be over 50 characters."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with missing date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientMissingDateOfBirthTest() throws Exception {

            //GIVEN
            // a patientDTO with missing family name
            String familyName = "family";
            String givenName = "given";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, null, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Date of birth is mandatory."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with wrong date of birth format " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientWrongFormatDateOfBirthTest() throws Exception {

            //GIVEN
            // a patientDTO with missing family name
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "dateOfBirth";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "The date of birth is not correct :"
                            + "\n The format must be yyyy/mm/dd."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with wrong month for date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientWrongMonthDateOfBirthTest() throws Exception {

            //GIVEN
            // a patientDTO with wrong month for date of birth
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-55-10";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "The date of birth is not correct :"
                            + "\n The month cannot be over 12."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with wrong day for date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientWrongDay1DateOfBirthTest() throws Exception {

            //GIVEN
            // a patientDTO with wrong month for date of birth
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-01-38";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "The date of birth is not correct :"
                            + "\n The number of the day is not correct for this month."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with wrong day for date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientWrongDay2DateOfBirthTest() throws Exception {

            //GIVEN
            // a patientDTO with wrong month for date of birth
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-02-30";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "The date of birth is not correct :"
                            + "\n The number of the day is not correct for this month."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with wrong day for date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientWrongDay3DateOfBirthTest() throws Exception {

            //GIVEN
            // a patientDTO with wrong month for date of birth
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-11-31";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "The date of birth is not correct :"
                            + "\n The number of the day is not correct for this month."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with wrong sex " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientWrongSexTest() throws Exception {

            //GIVEN
            // a patientDTO with wrong sex
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-11-11";
            String sex = "itDepends";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Please enter \"M\" or \"F\" for sex."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with missing sex " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientMissingSexTest() throws Exception {

            //GIVEN
            // a patientDTO with missing sex
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-11-11";
            String phone = "111-111-1111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, null, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Please enter \"M\" or \"F\" for sex."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with wrong phone number " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientWrongPhoneNumberTest() throws Exception {

            //GIVEN
            // a patientDTO with wrong phone number
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-11-11";
            String sex = "M";
            String phone = "1111111111";
            String address = "address";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Please enter a phone number with the following format: \"***-***-****\"."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN a patientDTO with wrong address " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void addNewPatientWrongAddressTest() throws Exception {

            //GIVEN
            // a patientDTO with wrong address
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-11-11";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Address cannot be over 256 characters."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }

        @DisplayName("GIVEN no parameters " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected message is returned.")
        @Test
        void addNewPatientWithoutParametersTest() throws Exception {

            //GIVEN
            // no parameters

            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add"))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : please verify the request's body.\n"));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).addNewPatient(any(PatientDTO.class));
        }
    }

    @Nested
    @Tag("UpdatePatientTest")
    @DisplayName("updatePatient tests:")
    class UpdatePatientTest {

        @DisplayName("GIVEN an infoPatientToUpdateDTO containing all information " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        void updatePatientTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO containing all information
            int id = 1;
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            doNothing().when(patientService).updatePatient(eq(id), any(InfoPatientToUpdateDTO.class));
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The patient number " + id + " has been updated."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).updatePatient(eq(1), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO containing only some parameters " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        void updatePatientOnlySomeParametersTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO containing only some parameters
            int id = 1;
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, null, null);
            doNothing().when(patientService).updatePatient(eq(id), any(InfoPatientToUpdateDTO.class));
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The patient number " + id + " has been updated."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).updatePatient(eq(1), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO containing only some parameters " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        void updatePatientOnlyPhoneAndAddressParametersTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO containing only some parameters
            int id = 1;

            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO();
            patientDTO.setAddress("address");
            patientDTO.setPhone("111-111-1111");
            doNothing().when(patientService).updatePatient(eq(id), any(InfoPatientToUpdateDTO.class));
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The patient number " + id + " has been updated."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).updatePatient(eq(1), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO containing no parameter " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        void updatePatientNoParameterTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO containing no parameter
            int id = 1;

            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO();
            doNothing().when(patientService).updatePatient(eq(id), any(InfoPatientToUpdateDTO.class));
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The patient number " + id + " has been updated."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).updatePatient(eq(1), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with too long family name " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void updatePatientTooLongFamilyNameTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with too long family name
            String familyName = "familyName123456789012345678901234567890123456789012345678901234567890";
            String givenName = "given";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Family name cannot be over 50 characters."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }


        @DisplayName("GIVEN an infoPatientToUpdateDTO with too long given name " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void updatePatientTooLongGivenNameTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with too long given name
            String familyName = "familyName";
            String givenName = "givenName123456789012345678901234567890123456789012345678901234567890";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Given name cannot be over 50 characters."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong date of birth format " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void updatePatientWrongFormatDateOfBirthTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong date of birth format
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "dateOfBirth";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "The date of birth is not correct :"
                            + "\n The format must be yyyy/mm/dd."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong month for date of birth " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void updatePatientWrongMonthDateOfBirthTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong month for date of birth
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-55-10";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "The date of birth is not correct :"
                            + "\n The month cannot be over 12."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong day for date of birth " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void updatePatientWrongDayDateOfBirthTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong day for date of birth
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-01-38";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "The date of birth is not correct :"
                            + "\n The number of the day is not correct for this month."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong year for date of birth " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void updatePatientWrongYearDateOfBirthTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong year for date of birth
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "1054-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "The date of birth is not correct :"
                            + "\n The year is not correct."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong sex " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void updatePatientWrongSexTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong sex
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-11-11";
            String sex = "itDepends";
            String phone = "111-111-1111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Please enter \"M\" or \"F\" for sex."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }


        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong phone number " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void updatePatientWrongPhoneNumberTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong phone number
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-11-11";
            String sex = "M";
            String phone = "1111111111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Please enter a phone number with the following format: \"***-***-****\"."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong address " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        void updatePatientWrongAddressTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong address
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-11-11";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("There is a problem with the following given data : "
                            + "\n- "
                            + "Address cannot be over 256 characters."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN no request body " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        void updatePatientWithoutRequestBodyTest() throws Exception {

            //GIVEN
            // no request body

            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1"))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : please verify the request's body.\n"));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }

        @DisplayName("GIVEN no pathVariable " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        void updatePatientWithoutPathVariableTest() throws Exception {

            //GIVEN
            // no path variable
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            String phone = "111-111-1111";
            String address = "address";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ow.writeValueAsString(patientDTO)))
                    //THEN
                    //the expected message is returned
                    .andExpect(status().isNotFound());
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(0)).updatePatient(anyInt(), any(InfoPatientToUpdateDTO.class));
        }
    }

    @Nested
    @Tag("DeletePatientTest")
    @DisplayName("deletePatient tests:")
    class DeletePatientTest {

        @DisplayName("GIVEN an existing patient " +
                "WHEN the uri \"/patient/delete/{patientId}\" is called " +
                "THEN the patient list have been decremented and the expected success message is returned .")
        @Test
        void deletePatientTest() throws Exception {

            //GIVEN
            //an existing patient
            doNothing().when(patientService).deletePatient(1);
            //WHEN
            // the uri "/patient/getPatient/{patientId}" is called
            mockMvc.perform(delete("/patient/delete/1"))

                    //THEN
                    //the expected patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The patient 1 has been deleted."));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).deletePatient(1);
        }

        @DisplayName("GIVEN a non-existing patient " +
                "WHEN the uri \"/patient/delete/{patientId}\" is called " +
                "THEN the patient list is still the same and the expected error message is returned.")
        @Test
        void deletePatientNonExisting() throws Exception {

            //GIVEN
            //a non-existing patient
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            doThrow(objectNotFoundException).when(patientService).deletePatient(1);

            //WHEN
            // the uri "/patient/getPatient/{patientId}" is called
            mockMvc.perform(delete("/patient/delete/1"))

                    //THEN
                    //an ObjectNotFoundException is thrown with the expected error message
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("error message"));
            // and the expected methods have been called with expected arguments
            verify(patientService, Mockito.times(1)).deletePatient(1);
        }
    }

}
