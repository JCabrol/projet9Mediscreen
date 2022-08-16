package mediscreen.medicalNotes.unitTest;

import mediscreen.medicalNotes.exception.ObjectNotFoundException;
import mediscreen.medicalNotes.model.MedicalNote;
import mediscreen.medicalNotes.service.MedicalNoteService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("medicalNoteControllerTest")
@SpringBootTest
@AutoConfigureMockMvc
public class MedicalNoteControllerTest {

    @MockBean
    private MedicalNoteService medicalNoteService;

    @Autowired
    private MockMvc mockMvc;

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
            //a list of MedicalNote
            String patientId = "patientId";
            List<MedicalNote> medicalNoteList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                MedicalNote medicalNote = new MedicalNote();
                medicalNoteList.add(medicalNote);
            }
            when(medicalNoteService.getAllPatientNotes(patientId)).thenReturn(medicalNoteList);

            //WHEN
            // WHEN the uri "/patHistory/getAllMedicalNotes/{patientId}" is called
            mockMvc.perform(get("/patHistory/getAllMedicalNotes/" + patientId))

                    //THEN
                    //the expected list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(5)));
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(1)).getAllPatientNotes(patientId);
        }

        @DisplayName("GIVEN an empty list of medicalNote " +
                "WHEN WHEN the uri \"/patHistory/getAllMedicalNotes/{patientId}\" is called " +
                "THEN an empty list is returned.")
        @Test
        void getAllMedicalNotesEmptyListTest() throws Exception {

            //GIVEN
            //an empty list of MedicalNote
            String patientId = "patientId";
            List<MedicalNote> medicalNoteList = new ArrayList<>();
            when(medicalNoteService.getAllPatientNotes(patientId)).thenReturn(medicalNoteList);

            //WHEN
            // the uri "/medicalNote/getAll" is called
            mockMvc.perform(get("/patHistory/getAllMedicalNotes/" + patientId))
                    //THEN
                    //an empty list is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(1)).getAllPatientNotes(patientId);
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
            String noteId = "noteId";
            MedicalNote medicalNote = new MedicalNote();
            medicalNote.setId(noteId);
            medicalNote.setNoteDate("date");
            medicalNote.setPatientId("patientId");
            medicalNote.setNoteContent("content");

            when(medicalNoteService.getMedicalNote(noteId)).thenReturn(medicalNote);

            //WHEN
            // the uri "/patHistory/getMedicalNote/{id}" is called
            mockMvc.perform(get("/patHistory/getMedicalNote/" + noteId))

                    //THEN
                    //the expected medicalNote is returned
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.noteDate", is("date")))
                    .andExpect(jsonPath("$.patientId", is("patientId")))
                    .andExpect(jsonPath("$.noteContent", is("content")));
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(1)).getMedicalNote(noteId);
        }

        @DisplayName("GIVEN a non-existing medicalNote " +
                "WHEN the uri \"/patHistory/getMedicalNote/{id}\" is called  " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getMedicalNoteByIdNonExistingTest() throws Exception {

            //GIVEN
            //a non-existing medicalNote
            String noteId = "noteId";
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(medicalNoteService.getMedicalNote(noteId)).thenThrow(objectNotFoundException);

            //WHEN
            // the uri "/patHistory/getMedicalNote/{id}" is called
            mockMvc.perform(get("/patHistory/getMedicalNote/" + noteId))

                    //THEN
                    //an ObjectNotFoundException is thrown with the expected error message
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("error message"));
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(1)).getMedicalNote(noteId);
        }
    }

    @Nested
    @Tag("GetAllInformationTest")
    @DisplayName("getAllInformation tests:")
    class GetAllInformationTest {


        @DisplayName("GIVEN a String returned by the service" +
                "WHEN the uri \"/patHistory/getAllInformation/{patientId}\" is called " +
                "THEN the expected string is returned.")
        @Test
        void getAllInformationTest() throws Exception {

            //GIVEN
            // a String returned by the service
            String patientId = "patientId";
            String contents = "contents";
            when(medicalNoteService.getAllContentByPatient(patientId)).thenReturn(contents);

            //WHEN
            // the uri "/patHistory/getAllInformation/{patientId}" is called
            mockMvc.perform(get("/patHistory/getAllInformation/" + patientId))

                    //THEN
                    //the expected string is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string(contents));
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(1)).getAllContentByPatient(patientId);
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
            doNothing().when(medicalNoteService).addMedicalNote(patientId, content);
            //WHEN
            // the uri "/patHistory/add/{patientId}" is called
            mockMvc.perform(post("/patHistory/add/" + patientId)
                            .content(content))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isCreated())
                    .andExpect(content().string("A medical note have been added to the patient number " + patientId + "."));
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(1)).addMedicalNote(patientId, content);

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
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(0)).addMedicalNote(anyString(), anyString());
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
            String noteId = "noteId";
            String content = "content";
            doNothing().when(medicalNoteService).updateMedicalNote(noteId, content);
            //WHEN
            // the uri "/patHistory/update/{noteId}" is called
            mockMvc.perform(put("/patHistory/update/" + noteId)
                            .content(content))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The medical note number " + noteId + " have been updated."));
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(1)).updateMedicalNote(noteId, content);

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
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(0)).updateMedicalNote(anyString(), anyString());
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

            //GIVEN
            //an existing medicalNote
            String noteId = "noteId";
            doNothing().when(medicalNoteService).deleteMedicalNote(noteId);
            //WHEN
            // the uri "/patHistory/delete/{noteId}" is called
            mockMvc.perform(delete("/patHistory/delete/" + noteId))

                    //THEN
                    //the expected message is returned
                    .andExpect(status().isOk())
                    .andExpect(content().string("The medical note number " + noteId + " have been deleted."));
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(1)).deleteMedicalNote(noteId);
        }

        @DisplayName("GIVEN a non-existing medicalNote " +
                "WHEN the uri \"/patHistory/delete/{noteId}\" is called " +
                "THEN the medicalNote list is still the same and the expected error message is returned.")
        @Test
        void deleteMedicalNoteNonExisting() throws Exception {

            //GIVEN
            //a non-existing medicalNote
            String noteId = "noteId";
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            doThrow(objectNotFoundException).when(medicalNoteService).deleteMedicalNote(noteId);

            //WHEN
            // the uri "/patHistory/getMedicalNote/{medicalNoteId}" is called
            mockMvc.perform(delete("/patHistory/delete/" + noteId))

                    //THEN
                    //an ObjectNotFoundException is thrown with the expected error message
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("error message"));
            // and the expected methods have been called with expected arguments
            verify(medicalNoteService, Mockito.times(1)).deleteMedicalNote(noteId);
        }
    }
}
