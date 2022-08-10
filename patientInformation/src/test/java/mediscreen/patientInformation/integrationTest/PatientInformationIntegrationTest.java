package mediscreen.patientInformation.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import mediscreen.patientInformation.modele.InfoPatientToUpdateDTO;
import mediscreen.patientInformation.modele.PatientDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("patientControllerIntegrationTest")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PatientInformationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DirtiesContext(classMode = BEFORE_CLASS)
    @Nested
    @Tag("GetAllPatientIntegrationTest")
    @DisplayName("getAllPatients integration tests:")
    class GetAllPatientsTest {

        @DisplayName("GIVEN a database with patients " +
                "WHEN the uri \"/patient/getAll\" is called " +
                "THEN the expected list is returned, ordered by family name.")
        @Sql({"/import-test.sql"})
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getAllPatientsIntegrationTest() throws Exception {

            //GIVEN
            //a list of PatientDTO

            //WHEN
            // the uri "/patient/getAll" is called
            mockMvc.perform(get("/patient/getAll"))

                    //THEN
                    //the expected list is returned, ordered by family name
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(6)))
                    .andExpect(jsonPath("$.[0].familyName", is("familyName1")))
                    .andExpect(jsonPath("$.[1].familyName", is("familyName2")))
                    .andExpect(jsonPath("$.[2].familyName", is("familyName2")))
                    .andExpect(jsonPath("$.[3].familyName", is("familyName3")))
                    .andExpect(jsonPath("$.[4].familyName", is("familyName3")))
                    .andExpect(jsonPath("$.[5].familyName", is("familyName5")))
                    .andExpect(jsonPath("$.[0].givenName", is("givenName1")))
                    .andExpect(jsonPath("$.[1].givenName", is("givenName2")))
                    .andExpect(jsonPath("$.[2].givenName", is("givenName2")))
                    .andExpect(jsonPath("$.[3].givenName", is("givenName3")))
                    .andExpect(jsonPath("$.[4].givenName", is("givenName4")))
                    .andExpect(jsonPath("$.[5].givenName", is("givenName3")));

        }

        @DisplayName("GIVEN an empty database " +
                "WHEN the uri \"/patient/getAll\" is called " +
                "THEN an empty list is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getAllPatientsEmptyListIntegrationTest() throws Exception {

            //GIVEN
            //an empty list of PatientDTO


            //WHEN
            // the uri "/patient/getAll" is called
            mockMvc.perform(get("/patient/getAll"))

                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    @Tag("GetPatientByIdIntegrationTest")
    @DisplayName("getPatientById integration tests:")
    @Sql({"/import-test.sql"})
    class GetPatientByIdIntegrationTest {


        @DisplayName("GIVEN an existing patient " +
                "WHEN the uri \"/patient/getPatient/{patientId}\" is called " +
                "THEN the expected patientDTO is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByIdIntegrationTest() throws Exception {

            //GIVEN
            //an existing patient

            //WHEN
            // the uri "/patient/getPatient/{patientId}" is called
            mockMvc.perform(get("/patient/getPatient/1"))

                    //THEN
                    //the expected patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")))
                    .andExpect(jsonPath("$.age", is(LocalDate.now().compareTo(LocalDate.of(2001, 1, 1)))));
        }

        @DisplayName("GIVEN a non-existing patient " +
                "WHEN the uri \"/patient/getPatient/{patientId}\" is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByIdNonExistingIntegrationTest() throws Exception {

            //GIVEN
            //a non-existing patient


            //WHEN
            // the uri "/patient/getPatient/{patientId}" is called
            mockMvc.perform(get("/patient/getPatient/0"))

                    //THEN
                    //an ObjectNotFoundException is thrown with the expected error message
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("The patient with id 0 was not found."));
        }
    }

    @Nested
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    @Sql({"/import-test.sql"})
    @Tag("GetPatientByNameIntegrationTest")
    @DisplayName("getPatientByName integration tests:")
    class GetPatientByNameIntegrationTest {


        @DisplayName("GIVEN existing familyName and givenName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameIntegrationTest() throws Exception {

            //GIVEN
            // existing familyName and givenName
            String familyName = "familyName1";
            String givenName = "givenName1";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName)
                            .param("givenName", givenName))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$.[0].familyName", is("familyName1")))
                    .andExpect(jsonPath("$.[0].givenName", is("givenName1")))
                    .andExpect(jsonPath("$.[0].dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.[0].sex", is("M")))
                    .andExpect(jsonPath("$.[0].phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.[0].address", is("address1")));
        }

        @DisplayName("GIVEN two patients with the same family name and the same given name " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameTwoSameNamesIntegrationTest() throws Exception {

            //GIVEN
            // existing familyName and givenName
            String familyName = "familyName2";
            String givenName = "givenName2";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName)
                            .param("givenName", givenName))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$.[0].familyName", is("familyName2")))
                    .andExpect(jsonPath("$.[0].givenName", is("givenName2")))
                    .andExpect(jsonPath("$.[1].familyName", is("familyName2")))
                    .andExpect(jsonPath("$.[1].givenName", is("givenName2")));
        }

        @DisplayName("GIVEN non-existing familyName and givenName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN an empty list is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameNonExistingIntegrationTest() throws Exception {

            //GIVEN
            // existing familyName and givenName
            String familyName = "familyName0";
            String givenName = "givenName0";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName)
                            .param("givenName", givenName))

                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @DisplayName("GIVEN non-existing familyName and existing givenName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN an empty list is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameFamilyNameNonExistingGivenNameExistingIntegrationTest() throws Exception {

            //GIVEN
            // existing non-existing familyName and existing givenName
            String familyName = "familyName0";
            String givenName = "givenName1";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName)
                            .param("givenName", givenName))

                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @DisplayName("GIVEN non-existing givenName and existing familyName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN an empty list is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameNonExistingGivenNameExistingFamilyNameIntegrationTest() throws Exception {

            //GIVEN
            // existing familyName and non-existing givenName
            String familyName = "familyName1";
            String givenName = "givenName0";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName)
                            .param("givenName", givenName))

                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }


        @DisplayName("GIVEN existing familyName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameOnlyFamilyNameIntegrationTest() throws Exception {

            //GIVEN
            // existing familyName
            String familyName = "familyName1";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$.[0].familyName", is("familyName1")))
                    .andExpect(jsonPath("$.[0].givenName", is("givenName1")))
                    .andExpect(jsonPath("$.[0].dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.[0].sex", is("M")))
                    .andExpect(jsonPath("$.[0].phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.[0].address", is("address1")));
        }

        @DisplayName("GIVEN non-existing familyName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN an empty list is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameOnlyFamilyNameNonExistingIntegrationTest() throws Exception {

            //GIVEN
            // non-existing familyName and givenName
            String familyName = "familyName0";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName))

                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @DisplayName("GIVEN two patients with the same family name " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameOnlyFamilyNameTwoSameNamesIntegrationTest() throws Exception {

            //GIVEN
            //two patients with the same family name
            String familyName = "familyName3";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("familyName", familyName))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$.[0].familyName", is("familyName3")))
                    .andExpect(jsonPath("$.[0].givenName", is("givenName3")))
                    .andExpect(jsonPath("$.[1].familyName", is("familyName3")))
                    .andExpect(jsonPath("$.[1].givenName", is("givenName4")));
        }

        @DisplayName("GIVEN existing givenName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameOnlyGivenNameIntegrationTest() throws Exception {

            //GIVEN
            // existing  givenName
            String givenName = "givenName1";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("givenName", givenName))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$.[0].familyName", is("familyName1")))
                    .andExpect(jsonPath("$.[0].givenName", is("givenName1")))
                    .andExpect(jsonPath("$.[0].dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.[0].sex", is("M")))
                    .andExpect(jsonPath("$.[0].phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.[0].address", is("address1")));
        }

        @DisplayName("GIVEN two patients with the same given name  " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN the expected list of patientDTO is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameTwoSameGivenNamesIntegrationTest() throws Exception {

            //GIVEN
            // two patients with the same given name
            String givenName = "givenName3";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("givenName", givenName))

                    //THEN
                    //the expected list of patientDTO is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$.[0].familyName", is("familyName3")))
                    .andExpect(jsonPath("$.[0].givenName", is("givenName3")))
                    .andExpect(jsonPath("$.[1].familyName", is("familyName5")))
                    .andExpect(jsonPath("$.[1].givenName", is("givenName3")));
        }


        @DisplayName("GIVEN non-existing givenName " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN an empty list is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameOnlyGivenNameNonExistingIntegrationTest() throws Exception {

            //GIVEN
            // non-existing familyName and givenName
            String givenName = "givenName0";

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient")
                            .param("givenName", givenName))

                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @DisplayName("GIVEN no parameter " +
                "WHEN the uri \"/patient/getPatient\" is called " +
                "THEN an empty list os returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void getPatientByNameNoParameterIntegrationTest() throws Exception {

            //GIVEN
            //  no parameter

            //WHEN
            // the uri "/patient/getPatient" is called
            mockMvc.perform(get("/patient/getPatient"))

                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    @Sql({"/import-test.sql"})
    @Tag("AddNewPatientIntegrationTest")
    @DisplayName("addNewPatient integration tests:")
    class AddNewPatientIntegrationTest {

        @DisplayName("GIVEN a patientDTO containing all information " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected message is returned and the number of patients have been incremented.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientIntegrationTest() throws Exception {

            //GIVEN
            // a patientDTO containing all information
            String familyName = "family";
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
                    //the expected message is returned
                    .andExpect(status().isCreated())
                    .andExpect(content().string("The patient named " + familyName + " " + givenName + " has been created."));
            // and the number of patients have been incremented
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(7)));
        }

        @DisplayName("GIVEN a patientDTO containing only required parameters " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected message is returned and the number of patients have been incremented.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientOnlyRequiredParametersIntegrationTest() throws Exception {

            //GIVEN
            // a patientDTO containing only required parameters
            String familyName = "family";
            String givenName = "given";
            String dateOfBirth = "2000-01-01";
            String sex = "M";
            PatientDTO patientDTO = new PatientDTO(familyName, givenName, dateOfBirth, sex);
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
            // and the number of patients have been incremented
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(7)));
        }

        @DisplayName("GIVEN a patientDTO with missing family name " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientMissingFamilyNameIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with too long family name " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientTooLongFamilyNameIntegrationTest() throws Exception {

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

            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with missing given name " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientMissingGivenNameIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with too long given name " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientTooLongGivenNameIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with missing date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientMissingDateOfBirthIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with wrong date of birth format " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientWrongFormatDateOfBirthIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with wrong month for date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientWrongMonthDateOfBirthIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with wrong day for date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientWrongDay1DateOfBirthIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with wrong day for date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientWrongDay2DateOfBirthIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with wrong day for date of birth " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientWrongDay3DateOfBirthIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with wrong sex " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientWrongSexIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with missing sex " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientMissingSexIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with wrong phone number " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientWrongPhoneNumberIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN a patientDTO with wrong address " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected error message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientWrongAddressIntegrationTest() throws Exception {

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
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }

        @DisplayName("GIVEN no parameters " +
                "WHEN the uri \"/patient/add\" is called " +
                "THEN the expected message is returned and the number of patients is still the same.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void addNewPatientWithoutParametersIntegrationTest() throws Exception {

            //GIVEN
            // no parameters

            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(post("/patient/add"))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : please verify the request's body.\n"));
            // and the number of patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
        }
    }

    @Nested
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    @Sql({"/import-test.sql"})
    @Tag("UpdatePatientIntegrationTest")
    @DisplayName("updatePatient integration tests:")
    class UpdatePatientIntegrationTest {

        @DisplayName("GIVEN an infoPatientToUpdateDTO containing all information " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned and the patient have been updated.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO containing all information
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "2009-09-09";
            String sex = "F";
            String phone = "999-999-9999";
            String address = "address9";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/add" is called
            mockMvc.perform(put("/patient/update/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The patient number 1 has been updated."));
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient have been updated
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("family9")))
                    .andExpect(jsonPath("$.givenName", is("given9")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2009-09-09")))
                    .andExpect(jsonPath("$.sex", is("F")))
                    .andExpect(jsonPath("$.phone", is("999-999-9999")))
                    .andExpect(jsonPath("$.address", is("address9")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO containing only some parameters " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned and only given parameters have been updated.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientOnlySomeParametersIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO containing only some parameters
            int id = 1;
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "2009-09-09";
            String sex = "F";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, null, null);
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient have been updated
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("family9")))
                    .andExpect(jsonPath("$.givenName", is("given9")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2009-09-09")))
                    .andExpect(jsonPath("$.sex", is("F")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO containing only some parameters " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned and only given parameters have been updated.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientOnlyPhoneAndAddressParametersIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO containing only some parameters
            int id = 1;

            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO();
            patientDTO.setAddress("address9");
            patientDTO.setPhone("999-999-9999");
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and only given parameters have been updated
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("999-999-9999")))
                    .andExpect(jsonPath("$.address", is("address9")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO containing no parameter " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientNoParameterIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO containing no parameter
            int id = 1;

            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO();
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with too long family name " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientTooLongFamilyNameIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with too long family name
            String familyName = "familyName123456789012345678901234567890123456789012345678901234567890";
            String givenName = "given9";
            String dateOfBirth = "2009-09-09";
            String sex = "F";
            String phone = "999-999-9999";
            String address = "address9";
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }


        @DisplayName("GIVEN an infoPatientToUpdateDTO with too long given name " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientTooLongGivenNameIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with too long given name
            String familyName = "familyName9";
            String givenName = "givenName123456789012345678901234567890123456789012345678901234567890";
            String dateOfBirth = "2009-09-09";
            String sex = "F";
            String phone = "999-999-9999";
            String address = "address9";
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong date of birth format " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientWrongFormatDateOfBirthIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong date of birth format
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "dateOfBirth";
            String sex = "F";
            String phone = "999-999-9999";
            String address = "address9";
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong month for date of birth " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientWrongMonthDateOfBirthIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong month for date of birth
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "2000-55-10";
            String sex = "F";
            String phone = "999-999-9999";
            String address = "address9";
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong day for date of birth " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientWrongDayDateOfBirthIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong day for date of birth
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "2000-01-38";
            String sex = "F";
            String phone = "999-999-9999";
            String address = "address9";
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong year for date of birth " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientWrongYearDateOfBirthIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong year for date of birth
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "1054-01-01";
            String sex = "F";
            String phone = "999-999-9999";
            String address = "address9";
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong sex " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientWrongSexIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong sex
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "2009-11-11";
            String sex = "itDepends";
            String phone = "999-999-9999";
            String address = "address9";
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }


        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong phone number " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientWrongPhoneNumberIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong phone number
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "2009-11-11";
            String sex = "F";
            String phone = "111-111-1111-1111111111";
            String address = "address9";
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }

        @DisplayName("GIVEN an infoPatientToUpdateDTO with wrong address " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected error message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientWrongAddressIntegrationTest() throws Exception {

            //GIVEN
            // an infoPatientToUpdateDTO with wrong address
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "2009-11-11";
            String sex = "F";
            String phone = "999-999-9999";
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
            // and not any patient have been added
            mockMvc.perform(get("/patient/getAll")).andExpect(jsonPath("$", hasSize(6)));
            // and the patient still have previous information
            mockMvc.perform(get("/patient/getPatient/1"))
                    .andExpect(jsonPath("$.familyName", is("familyName1")))
                    .andExpect(jsonPath("$.givenName", is("givenName1")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2001-01-01")))
                    .andExpect(jsonPath("$.sex", is("M")))
                    .andExpect(jsonPath("$.phone", is("111-111-1111")))
                    .andExpect(jsonPath("$.address", is("address1")));
        }

        @DisplayName("GIVEN no request body " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientWithoutRequestBodyIntegrationTest() throws Exception {

            //GIVEN
            // no request body

            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/1"))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : please verify the request's body.\n"));
        }

        @DisplayName("GIVEN no pathVariable " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientWithoutPathVariableIntegrationTest() throws Exception {

            //GIVEN
            // no path variable
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "2009-01-01";
            String sex = "F";
            String phone = "999-999-9999";
            String address = "address9";
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
        }

        @DisplayName("GIVEN non-existing patient " +
                "WHEN the uri \"/patient/update/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        @DirtiesContext(methodMode = AFTER_METHOD)
        void updatePatientNonExistingIntegrationTest() throws Exception {

            //GIVEN
            // non-existing patient
            String familyName = "family9";
            String givenName = "given9";
            String dateOfBirth = "2009-01-01";
            String sex = "F";
            String phone = "999-999-9999";
            String address = "address9";
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO(familyName, givenName, dateOfBirth, sex, phone, address);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            //WHEN
            // the uri "/patient/update/{patientId}" is called
            mockMvc.perform(put("/patient/update/0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(ow.writeValueAsString(patientDTO)))
                    //THEN
                    //the expected message is returned
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("The patient with id 0 was not found, so it couldn't have been updated."));
        }
    }


    @Nested
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    @Tag("DeletePatientIntegrationTest")
    @DisplayName("deletePatient integration tests:")
    @Sql({"/import-test.sql"})
    class DeletePatientIntegrationTest {


        @DisplayName("GIVEN an existing patient " +
                "WHEN the uri \"/patient/delete/{patientId}\" is called " +
                "THEN the patient list have been decremented and the expected success message is returned .")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void deletePatientIntegrationTest() throws Exception {

            //GIVEN
            //an existing patient

            //WHEN
            // the uri "/patient/getPatient/{patientId}" is called
            mockMvc.perform(delete("/patient/delete/1"))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The patient 1 has been deleted."));
            //and the number of patient has been decremented
            mockMvc.perform(get("/patient/getAll")).andExpect((jsonPath("$", hasSize(5))));
        }

        @DisplayName("GIVEN a non-existing patient " +
                "WHEN the uri \"/patient/delete/{patientId}\" is called " +
                "THEN the patient list is still the same and the expected error message is returned.")
        @DirtiesContext(methodMode = AFTER_METHOD)
        @Test
        void deletePatientNonExistingIntegrationTest() throws Exception {

            //GIVEN
            //a non-existing patient


            //WHEN
            // the uri "/patient/getPatient/{patientId}" is called
            mockMvc.perform(delete("/patient/delete/0"))

                    //THEN
                    //the expected error message is returned
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("The patient with id 0 was not found, so it couldn't have been deleted."));
            //and the number of  patients is still the same
            mockMvc.perform(get("/patient/getAll")).andExpect((jsonPath("$", hasSize(6))));
        }
    }
}
