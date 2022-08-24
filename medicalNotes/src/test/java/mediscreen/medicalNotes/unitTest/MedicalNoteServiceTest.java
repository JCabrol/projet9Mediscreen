package mediscreen.medicalNotes.unitTest;

import mediscreen.medicalNotes.exception.ObjectNotFoundException;
import mediscreen.medicalNotes.model.MedicalNote;
import mediscreen.medicalNotes.repository.MedicalNoteRepository;
import mediscreen.medicalNotes.service.MedicalNoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag("MedicalNoteServiceTest")
@SpringBootTest
public class MedicalNoteServiceTest {

    @Autowired
    private MedicalNoteService medicalNoteService;

    @MockBean
    private MedicalNoteRepository medicalNoteRepository;

    @Nested
    @Tag("GetAllPatientNotesTest")
    @DisplayName("getAllPatientNotes tests:")
    class GetAllPatientNotesTest {


        @DisplayName("GIVEN a patient having medical notes " +
                "WHEN the function getAllPatientNotes is called " +
                "THEN a list of MedicalNote is returned.")
        @Test
        void getAllPatientNotesTest() {

            //GIVEN
            //a list containing medicalNotes
            List<MedicalNote> medicalNoteList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                MedicalNote medicalNote = new MedicalNote();
                medicalNoteList.add(medicalNote);
            }
            String patientId = "patientId";
            when(medicalNoteRepository.findByPatientIdOrderByNoteDateDesc(patientId)).thenReturn(medicalNoteList);

            //WHEN
            //the function getAllMedicalNotes is called
            List<MedicalNote> result = medicalNoteService.getAllPatientNotes(patientId);

            //THEN
            //a list of MedicalNoteDTO containing expected information is returned
            assertEquals(5, result.size());
            //the expected methods have been called with expected arguments
            verify(medicalNoteRepository, Mockito.times(1)).findByPatientIdOrderByNoteDateDesc(patientId);
        }

        @DisplayName("GIVEN a patient having not any medical note " +
                "WHEN the function getAllPatientNotes is called " +
                "THEN an empty list is returned.")
        @Test
        void getAllPatientNotesEmptyListTest() {

            //GIVEN
            //a list containing medicalNotes
            List<MedicalNote> medicalNoteList = new ArrayList<>();
            String patientId = "patientId";
            when(medicalNoteRepository.findByPatientIdOrderByNoteDateDesc(patientId)).thenReturn(medicalNoteList);

            //WHEN
            //the function getAllMedicalNotes is called
            List<MedicalNote> result = medicalNoteService.getAllPatientNotes(patientId);

            //THEN
            //a list of MedicalNoteDTO containing expected information is returned
            assertEquals(0, result.size());
            //the expected methods have been called with expected arguments
            verify(medicalNoteRepository, Mockito.times(1)).findByPatientIdOrderByNoteDateDesc(patientId);
        }
    }

    @Nested
    @Tag("GetMedicalNoteTest")
    @DisplayName("getMedicalNote tests:")
    class GetMedicalNoteTest {

        @DisplayName("GIVEN an existing id " +
                "WHEN the function getMedicalNote is called " +
                "THEN the expected medical note is returned.")
        @Test
        void getMedicalNoteTest() {

            //GIVEN
            //an existing id
            String noteId = "noteId";
            MedicalNote medicalNote = new MedicalNote();
            medicalNote.setId(noteId);
            medicalNote.setNoteDate("date");
            medicalNote.setPatientId("patientId");
            medicalNote.setNoteContent("content");
            when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.of(medicalNote));

            //WHEN
            //the function getMedicalNoteById is called
            MedicalNote result = medicalNoteService.getMedicalNote(noteId);

            //THEN
            //the expected medical note is returned
            assertEquals(noteId, result.getId());
            assertEquals("patientId", result.getPatientId());
            assertEquals("date", result.getNoteDate());
            assertEquals("content", result.getNoteContent());

            //the expected methods have been called with expected arguments
            verify(medicalNoteRepository, Mockito.times(1)).findById(noteId);
        }

        @DisplayName("GIVEN a non-existing id " +
                "WHEN the function getMedicalNote is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getMedicalNoteByIdNonExistingTest() {

            //GIVEN
            //a non-existing id
            String noteId = "noteId";
            when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.empty());

            //WHEN
            //the function getMedicalNoteById is called

            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> medicalNoteService.getMedicalNote(noteId));
            assertEquals("The medical note number " + noteId + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(medicalNoteRepository, Mockito.times(1)).findById(noteId);
        }
    }

    @Nested
    @Tag("GetAllContentByPatientTest")
    @DisplayName("getAllContentByPatient tests:")
    class GetAllContentByPatientTest {

        @DisplayName("GIVEN an existing patient id with note contents" +
                "WHEN the function getAllContentByPatient is called " +
                "THEN a String containing all note contents is returned.")
        @Test
        void getAllContentByPatientTest() {

            //GIVEN
            //an existing patient id with note contents
            String patientId = "patientId";
            String content1 = "content";
            String content2 = "content2";
            String content3 = "content3";
            MedicalNote medicalNote = new MedicalNote();
            medicalNote.setPatientId(patientId);
            medicalNote.setNoteContent(content1);
            MedicalNote medicalNote2 = new MedicalNote();
            medicalNote2.setPatientId(patientId);
            medicalNote2.setNoteContent(content2);
            MedicalNote medicalNote3 = new MedicalNote();
            medicalNote3.setPatientId(patientId);
            medicalNote3.setNoteContent(content3);
            when(medicalNoteRepository.findByPatientIdOrderByNoteDateDesc(patientId)).thenReturn(List.of(medicalNote, medicalNote2, medicalNote3));

            //WHEN
            //the function getAllContentByPatient is called
            String result = medicalNoteService.getAllContentByPatient(patientId);

            //THEN
            //a String containing all note contents is returned.
            assertEquals(result, " " + content1 + " " + content2 + " " + content3);
            //and the expected methods have been called with expected arguments
            verify(medicalNoteRepository, Mockito.times(1)).findByPatientIdOrderByNoteDateDesc(patientId);
        }

        @DisplayName("GIVEN no content" +
                "WHEN the function getAllContentByPatient is called " +
                "THEN an empty String is returned.")
        @Test
        void getAllContentByPatientNoContentTest() {

            //GIVEN
            //no content
            String patientId = "patientId";
            when(medicalNoteRepository.findByPatientIdOrderByNoteDateDesc(patientId)).thenReturn(new ArrayList<>());

            //WHEN
            //the function getAllContentByPatient is called
            String result = medicalNoteService.getAllContentByPatient(patientId);

            //THEN
            //an empty String is returned
            assertEquals(result, "");
            //and the expected methods have been called with expected arguments
            verify(medicalNoteRepository, Mockito.times(1)).findByPatientIdOrderByNoteDateDesc(patientId);
        }
    }

    @Nested
    @Tag("AddMedicalNoteTest")
    @DisplayName("addMedicalNote tests:")
    class AddMedicalNoteTest {


        @DisplayName("GIVEN a medicalNote" +
                "WHEN the function addMedicalNote is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void addMedicalNoteTest() {
            //GIVEN
            //a medicalNoteDTO with all information given in parameter
            MedicalNote medicalNote = new MedicalNote();
            String patientId = "patientId";
            String content = "content";
            medicalNote.setPatientId(patientId);
            medicalNote.setNoteContent(content);
            final ArgumentCaptor<MedicalNote> arg = ArgumentCaptor.forClass(MedicalNote.class);
            when(medicalNoteRepository.save(any(MedicalNote.class))).thenReturn(new MedicalNote());

            //WHEN
            //the function addMedicalNote is called
            medicalNoteService.addMedicalNote(patientId, content);

            //THEN
            //the expected methods have been called with expected arguments
            verify(medicalNoteRepository).save(arg.capture());
            assertEquals(patientId, arg.getValue().getPatientId());
            assertEquals(content, arg.getValue().getNoteContent());
            assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), arg.getValue().getNoteDate());
        }

        @DisplayName("GIVEN a medicalNote without information " +
                "WHEN the function addMedicalNote is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void addMedicalNoteTestNoInformation() {

            //GIVEN
            //a medicalNote without information
            final ArgumentCaptor<MedicalNote> arg = ArgumentCaptor.forClass(MedicalNote.class);
            when(medicalNoteRepository.save(any(MedicalNote.class))).thenReturn(new MedicalNote());

            //WHEN
            //the function addNewMedicalNote is called
            medicalNoteService.addMedicalNote("", "");

            //THEN
            //the expected methods have been called with expected arguments
            verify(medicalNoteRepository).save(arg.capture());
            assertEquals("", arg.getValue().getPatientId());
            assertEquals("", arg.getValue().getNoteContent());
            assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), arg.getValue().getNoteDate());
        }
    }

    @Nested
    @Tag("UpdateMedicalNoteTest")
    @DisplayName("updateMedicalNote tests:")
    class UpdateMedicalNoteTest {


        @DisplayName("GIVEN a medicalNoteDTO with all information given in parameter " +
                "WHEN the function updateMedicalNote is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void updateMedicalNoteTest() {

            String content = "Content";
            String patientId = "patientId";
            String noteDate = "date";
            String noteId = "noteId";

            MedicalNote medicalNote = new MedicalNote();
            medicalNote.setNoteContent(content);
            medicalNote.setId(noteId);
            medicalNote.setNoteDate(noteDate);
            medicalNote.setPatientId(patientId);

            String newContent = "newContent";

            final ArgumentCaptor<MedicalNote> arg = ArgumentCaptor.forClass(MedicalNote.class);
            when(medicalNoteRepository.save(any(MedicalNote.class))).thenReturn(new MedicalNote());
            when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.of(medicalNote));

            //WHEN
            //the function addMedicalNote is called
            medicalNoteService.updateMedicalNote(noteId, newContent);

            //THEN
            //the expected methods have been called with expected arguments
            verify(medicalNoteRepository).save(arg.capture());
            assertEquals(patientId, arg.getValue().getPatientId());
            assertEquals(newContent, arg.getValue().getNoteContent());
            assertEquals(noteDate, arg.getValue().getNoteDate());
            assertEquals(noteId, arg.getValue().getId());
            verify(medicalNoteRepository, Mockito.times(1)).findById(noteId);
        }

        @DisplayName("GIVEN a medicalNoteDTO with all information given in parameter " +
                "WHEN the function updateMedicalNote is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void updateMedicalNoteNoContentTest() {

            String content = "Content";
            String patientId = "patientId";
            String noteDate = "date";
            String noteId = "noteId";

            MedicalNote medicalNote = new MedicalNote();
            medicalNote.setNoteContent(content);
            medicalNote.setId(noteId);
            medicalNote.setNoteDate(noteDate);
            medicalNote.setPatientId(patientId);


            final ArgumentCaptor<MedicalNote> arg = ArgumentCaptor.forClass(MedicalNote.class);
            when(medicalNoteRepository.save(any(MedicalNote.class))).thenReturn(new MedicalNote());
            when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.of(medicalNote));

            //WHEN
            //the function addMedicalNote is called
            medicalNoteService.updateMedicalNote(noteId, "");

            //THEN
            //the expected methods have been called with expected arguments
            verify(medicalNoteRepository).save(arg.capture());
            assertEquals(patientId, arg.getValue().getPatientId());
            assertEquals("", arg.getValue().getNoteContent());
            assertEquals(noteDate, arg.getValue().getNoteDate());
            assertEquals(noteId, arg.getValue().getId());
            verify(medicalNoteRepository, Mockito.times(1)).findById(noteId);
        }

        @DisplayName("GIVEN a medicalNote with non-existing id " +
                "WHEN the function updateMedicalNote is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void addNewMedicalNoteTestMissingInformation() {

            //GIVEN
            //a medicalNoteDTO with non-existing id
            String noteId = "noteId";
            when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.empty());

            //WHEN
            //the function updateMedicalNote is called

            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> medicalNoteService.updateMedicalNote(noteId, ""));
            assertEquals("The medical note number " + noteId + " was not found.", exception.getMessage());
            //the expected methods have been called with expected arguments
            verify(medicalNoteRepository, Mockito.times(1)).findById(noteId);
            verify(medicalNoteRepository, Mockito.times(0)).save(any(MedicalNote.class));
        }
    }

    @Nested
    @Tag("DeleteMedicalNoteTest")
    @DisplayName("deleteMedicalNote tests:")
    class DeleteMedicalNoteTest {


        @DisplayName("GIVEN an existing id " +
                "WHEN the function deleteMedicalNote is called " +
                "THEN the expected method have been called with expected arguments.")
        @Test
        void deleteMedicalNoteTest() {

            //GIVEN
            //an existing id
            String id = "medicalNoteId";
            MedicalNote medicalNote = new MedicalNote();
            when(medicalNoteRepository.findById(id)).thenReturn(Optional.of(medicalNote));
            doNothing().when(medicalNoteRepository).delete(medicalNote);

            //WHEN
            //the function deleteMedicalNote is called
            medicalNoteService.deleteMedicalNote(id);

            //THEN
            //the expected methods have been called with expected arguments
            verify(medicalNoteRepository, Mockito.times(1)).findById(id);
            verify(medicalNoteRepository, Mockito.times(1)).delete(medicalNote);
        }

        @DisplayName("GIVEN a non-existing id " +
                "WHEN the function deleteMedicalNote is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void deleteMedicalNoteNonExistingTest() {

            //GIVEN
            //a non-existing id
            String id = "medicalNoteId";
            when(medicalNoteRepository.findById(id)).thenReturn(Optional.empty());

            //WHEN
            //the function deleteMedicalNote is called

            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> medicalNoteService.deleteMedicalNote(id));
            assertEquals("The medical note number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(medicalNoteRepository, Mockito.times(1)).findById(id);
            verify(medicalNoteRepository, Mockito.times(0)).deleteById(id);
        }
    }
}
