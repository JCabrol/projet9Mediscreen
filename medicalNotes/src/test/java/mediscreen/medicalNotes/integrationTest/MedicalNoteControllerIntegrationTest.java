package mediscreen.medicalNotes.integrationTest;

import mediscreen.medicalNotes.model.MedicalNote;
import mediscreen.medicalNotes.service.MedicalNoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@Tag("medicalNoteControllerTest")
@SpringBootTest
@AutoConfigureMockMvc
public class MedicalNoteControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicalNoteService medicalNoteService;

    @Nested
    @Tag("GetMedicalNotesByPatientTest")
    @DisplayName("getMedicalNotesByPatient tests:")
    class GetMedicalNotesByPatientTest {


        @DisplayName("GIVEN a list of medicalNote " +
                "WHEN the uri \"/patHistory/getAllMedicalNotes/{patientId}\" is called " +
                "THEN the expected list is returned.")
        @Test
        void getAllMedicalNotesTest() throws Exception {

            //GIVEN

            // WHEN the uri "/patHistory/getAllMedicalNotes/{patientId}" is called
            mockMvc.perform(get("/patHistory/getAllMedicalNotes/1"))
                    //THEN
                    //the expected list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));
        }

        @DisplayName("GIVEN an empty list of medicalNote " +
                "WHEN WHEN the uri \"/patHistory/getAllMedicalNotes/{patientId}\" is called " +
                "THEN an empty list is returned.")
        @Test
        void getAllMedicalNotesEmptyListTest() throws Exception {

            //GIVEN
            //an empty list of MedicalNote


            //WHEN
            // the uri "/medicalNote/getAll" is called
            mockMvc.perform(get("/patHistory/getAllMedicalNotes/3"))
                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @Tag("GetMedicalNoteTest")
    @DisplayName("getMedicalNote tests:")
    class GetMedicalNoteTest {


        @DisplayName("GIVEN an existing medicalNote " +
                "WHEN the uri \"/patHistory/getMedicalNote/{id}\" is called " +
                "THEN the expected medicalNote is returned.")
        @Test
        void getMedicalNoteByIdTest() throws Exception {

            //GIVEN
            //an existing medicalNote

            //WHEN
            // the uri "/patHistory/getMedicalNote/{id}" is called
            mockMvc.perform(get("/patHistory/getMedicalNote/62fb9fcc1d066d776f7ef342"))

                    //THEN
                    //the expected medicalNote is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.noteDate", is("2022-08-15")))
                    .andExpect(jsonPath("$.patientId", is("1")))
                    .andExpect(jsonPath("$.noteContent", is("content1 patient1")));
        }

        @DisplayName("GIVEN a non-existing medicalNote " +
                "WHEN the uri \"/patHistory/getMedicalNote/{id}\" is called  " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getMedicalNoteByIdNonExistingTest() throws Exception {

            //GIVEN
            //a non-existing medicalNote


            //WHEN
            // the uri "/patHistory/getMedicalNote/{id}" is called
            mockMvc.perform(get("/patHistory/getMedicalNote/idNonExisting"))

                    //THEN
                    //an ObjectNotFoundException is thrown with the expected error message
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("The medical note number idNonExisting was not found."));
        }
    }

    @Nested
    @Tag("GetAllInformationTest")
    @DisplayName("getAllInformation tests:")
    class GetAllInformationTest {


        @DisplayName("GIVEN a patient with several notes" +
                "WHEN the uri \"/patHistory/getAllInformation/{patientId}\" is called " +
                "THEN the expected string is returned.")
        @Test
        void getAllInformationTest() throws Exception {

            //GIVEN
            // a patient with several notes

            //WHEN
            // the uri "/patHistory/getAllInformation/{patientId}" is called
            mockMvc.perform(get("/patHistory/getAllInformation/1"))

                    //THEN
                    //the expected string is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string(" content1 patient1 content2 patient1 content3 patient1"));
        }

        @DisplayName("GIVEN a patient without notes" +
                "WHEN the uri \"/patHistory/getAllInformation/{patientId}\" is called " +
                "THEN the expected string is returned.")
        @Test
        void getAllInformationEmptyTest() throws Exception {

            //GIVEN
            // a patient without note

            //WHEN
            // the uri "/patHistory/getAllInformation/{patientId}" is called
            mockMvc.perform(get("/patHistory/getAllInformation/3"))

                    //THEN
                    //the expected string is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
        }
    }

    @Nested
    @Tag("AddMedicalNoteTest")
    @DisplayName("addMedicalNote tests:")
    class AddMedicalNoteTest {

        @DisplayName("GIVEN content and patient id " +
                "WHEN the uri \"/patHistory/add/{patientId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        void addMedicalNoteTest() throws Exception {

            //GIVEN
            // content and patient id
            String patientId = "patientId";
            String content = "content";
            //WHEN
            // the uri "/patHistory/add/{patientId}" is called
            mockMvc.perform(post("/patHistory/add/" + patientId)
                            .content(content))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isCreated())
                    .andExpect(content().string("A medical note have been added to the patient number " + patientId + "."));
            // and the note have been added
            mockMvc.perform(get("/patHistory/getAllMedicalNotes/patientId"))
                    .andExpect(jsonPath("$", hasSize(1)));


            //delete added document to keep database like before test
            medicalNoteService.deleteMedicalNote(medicalNoteService.getAllPatientNotes(patientId).get(0).getId());
        }


        @DisplayName("GIVEN no parameters " +
                "WHEN the uri \"/patHistory/add/{patientId}\" is called" +
                "THEN the expected message is returned.")
        @Test
        void addMedicalNoteWithoutParametersTest() throws Exception {

            //GIVEN
            // no parameters
            String patientId = "patientId";
            //WHEN
            // the uri "/patHistory/add/{patientId}" is called
            mockMvc.perform(post("/patHistory/add/" + patientId))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : please verify the request's body.\n"));
        }
    }

    @Nested
    @Tag("UpdateMedicalNoteTest")
    @DisplayName("updateMedicalNote tests:")
    class UpdateMedicalNoteTest {

        @DisplayName("GIVEN content and note id " +
                "WHEN the uri \"/patHistory/update/{noteId}\" is called " +
                "THEN the expected message is returned.")
        @Test
        void updateMedicalNoteTest() throws Exception {

            //GIVEN
            // content and note id
            String noteId = "62fba0001d066d776f7ef345";
            String content = "new content";

            //WHEN
            // the uri "/patHistory/update/{noteId}" is called
            mockMvc.perform(put("/patHistory/update/" + noteId)
                            .content(content))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The medical note number " + noteId + " have been updated."));
            // and the note have been updated with new content
            mockMvc.perform(get("/patHistory/getMedicalNote/" + noteId)).andExpect(jsonPath("$.noteContent", is(content)));

            //update note again with previous content to keep database unchanged
            medicalNoteService.updateMedicalNote(noteId, "content patient2");
        }


        @DisplayName("GIVEN no parameters " +
                "WHEN the uri \"/patHistory/update/{noteId}\" is called" +
                "THEN the expected message is returned.")
        @Test
        void addMedicalNoteWithoutParametersTest() throws Exception {

            //GIVEN
            // no parameters
            String patientId = "patientId";
            //WHEN
            // the uri "/patHistory/update/{noteId}" is called
            mockMvc.perform(put("/patHistory/update/" + patientId))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("The request is not correct : please verify the request's body.\n"));
        }
    }

    @Nested
    @Tag("DeleteMedicalNoteTest")
    @DisplayName("deleteMedicalNote tests:")
    class DeleteMedicalNoteTest {

        @DisplayName("GIVEN an existing medicalNote " +
                "WHEN the uri \"/patHistory/delete/{noteId}\" is called " +
                "THEN the medicalNote list have been decremented and the expected success message is returned .")
        @Test
        void deleteMedicalNoteTest() throws Exception {
//before testing add a new medicalNote to delete and assert it's present, so database will be unchanged at the test's end
            medicalNoteService.addMedicalNote("new patient", "content to delete");
            MedicalNote medicalNote = medicalNoteService.getAllPatientNotes("new patient").get(0);
            assertEquals("content to delete", medicalNote.getNoteContent());

            //GIVEN
            //an existing medicalNote
            String noteId = medicalNote.getId();
            //WHEN
            // the uri "/patHistory/delete/{noteId}" is called
            mockMvc.perform(delete("/patHistory/delete/" + noteId))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The medical note number " + noteId + " have been deleted."));
            // and expect the note doesn't exist anymore
            assertEquals(0, medicalNoteService.getAllPatientNotes("new patient").size());
        }

        @DisplayName("GIVEN a non-existing medicalNote " +
                "WHEN the uri \"/patHistory/delete/{noteId}\" is called " +
                "THEN the medicalNote list is still the same and the expected error message is returned.")
        @Test
        void deleteMedicalNoteNonExisting() throws Exception {

            //GIVEN
            //a non-existing medicalNote
            String noteId = "noteId";

            //WHEN
            // the uri "/patHistory/getMedicalNote/{medicalNoteId}" is called
            mockMvc.perform(delete("/patHistory/delete/" + noteId))

                    //THEN
                    //an ObjectNotFoundException is thrown with the expected error message
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("The medical note number " + noteId + " was not found."));

        }
    }
}
