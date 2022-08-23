package mediscreen.patientUI.unitTest;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import mediscreen.patientUI.bean.DiabetesRiskBean;
import mediscreen.patientUI.bean.MedicalNoteBean;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.exception.ObjectNotFoundException;
import mediscreen.patientUI.modele.ListOfNotesToDisplay;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import mediscreen.patientUI.patientUIService.PatientUIService;
import mediscreen.patientUI.proxy.MedicalNoteProxy;
import mediscreen.patientUI.proxy.PatientInformationProxy;
import mediscreen.patientUI.proxy.PatientRiskProxy;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("ServiceTests")
@Slf4j
@SpringBootTest
public class PatientServiceTest {

    @Autowired
    private PatientUIService patientUIService;

    @MockBean
    PatientInformationProxy patientInformationProxy;

    @MockBean
    MedicalNoteProxy medicalNoteProxy;

    @MockBean
    PatientRiskProxy patientRiskProxy;

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("CreatePreviewContentListTest")
    class CreatePreviewContentListTests {

        @Test
        @DisplayName("GIVEN a list of medicalNoteBean " +
                "WHEN the function createPreviewContentList is called " +
                "THEN a list of medicalNoteBean is returned, all content size is under 90 characters and all tags have been removed.")
        void createPreviewContentListTest() {
            //GIVEN
            //a list of medicalNoteBean
            MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
            MedicalNoteBean medicalNoteBean2 = new MedicalNoteBean();
            MedicalNoteBean medicalNoteBean3 = new MedicalNoteBean();

            medicalNoteBean.setNoteContent("under 90 characters 1234567890 1234567890");
            medicalNoteBean2.setNoteContent("over 90 characters 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890");
            medicalNoteBean3.setNoteContent("over 90 characters with tags <br> 1234567890 1234567890 1234567890 1234567890 1234567890 <strong> 1234567890 1234567890 1234567890 12345<ul>67890 1234567890");

            List<MedicalNoteBean> medicalNoteList = new ArrayList<>();
            medicalNoteList.add(medicalNoteBean);
            medicalNoteList.add(medicalNoteBean2);
            medicalNoteList.add(medicalNoteBean3);
            // WHEN
            //the function createPreviewContentList is called
            List<MedicalNoteBean> result = patientUIService.createPreviewContentList(medicalNoteList);
            // THEN
            //a list of medicalNoteBean is returned, all content size is under 90 characters and all tags have been removed.
            assertEquals(result.get(0).getNoteContent(), medicalNoteList.get(0).getNoteContent());
            assertTrue(result.get(1).getNoteContent().chars().count() <= 90);
            assertTrue(result.get(2).getNoteContent().chars().count() <= 90);
            assertFalse(result.get(2).getNoteContent().contains("<br>"));
            assertFalse(result.get(2).getNoteContent().contains("<strong>"));
            assertFalse(result.get(2).getNoteContent().contains("<ul>"));
        }

        @Test
        @DisplayName("GIVEN an empty list of medicalNoteBean " +
                "WHEN the function createPreviewContentList is called " +
                "THEN an empty list is returned.")
        void createPreviewContentListEmptyTest() {
            //GIVEN
            //an empty list of medicalNoteBean
            List<MedicalNoteBean> medicalNoteList = new ArrayList<>();

            // WHEN
            //the function createPreviewContentList is called
            List<MedicalNoteBean> result = patientUIService.createPreviewContentList(medicalNoteList);
            // THEN
            // an empty list is returned.
            assertEquals(0, result.size());
        }

        @Test
        @DisplayName("GIVEN a list of medicalNoteBean with null content " +
                "WHEN the function createPreviewContentList is called " +
                "THEN a list of medicalNoteBean is returned with empty String content.")
        void createPreviewContentListContentNullTest() {
            //GIVEN
            //a list of medicalNoteBean with null content
            MedicalNoteBean medicalNoteBean = new MedicalNoteBean();

            List<MedicalNoteBean> medicalNoteList = new ArrayList<>();
            medicalNoteList.add(medicalNoteBean);

            // WHEN
            //the function createPreviewContentList is called
            List<MedicalNoteBean> result = patientUIService.createPreviewContentList(medicalNoteList);
            // THEN
            //a list of medicalNoteBean is returned with empty String content.
            assertEquals(result.get(0).getNoteContent(), "");
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("GetPatientsToDisplayTest")
    class GetPatientsToDisplayTests {

        @Test
        @DisplayName("GIVEN a list of patientBean with 5 elements, page number 1 and 4 elements by page" +
                "WHEN the function getPatientsToDisplay is called " +
                "THEN a ListOfPatientsToDisplay object is returned with expected elements.")
        void getPatientsToDisplayTest() {
            //GIVEN
            //a list of patientBean with 5 elements, page number 1 and 4 elements by page
            List<PatientBean> patientList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                PatientBean patientBean = new PatientBean();
                patientList.add(patientBean);
            }
            // WHEN
            //the function getPatientsToDisplay is called
            ListOfPatientsToDisplay result = patientUIService.getPatientsToDisplay(1, 4, patientList);
            // THEN
            // a ListOfPatientsToDisplay object is returned with expected elements
            assertEquals(1, result.getActivePage());
            assertEquals(4, result.getPatientList().size());
            assertEquals(2, result.getTotalNumberOfPages());
            assertEquals(1, result.getPagesToDisplay()[0]);
            assertEquals(2, result.getPagesToDisplay()[1]);
            assertNull(result.getPagesToDisplay()[2]);
        }

        @Test
        @DisplayName("GIVEN a list of patientBean with 3 elements, page number 1 and 5 elements by page" +
                "WHEN the function getPatientsToDisplay is called " +
                "THEN a ListOfPatientsToDisplay object is returned with expected elements.")
        void getPatientsToDisplayLessPatientThanElementsByPageTest() {
            //GIVEN
            //a list of patientBean with 3 elements, page number 1 and 5 elements by page
            List<PatientBean> patientList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                PatientBean patientBean = new PatientBean();
                patientList.add(patientBean);
            }
            // WHEN
            //the function getPatientsToDisplay is called
            ListOfPatientsToDisplay result = patientUIService.getPatientsToDisplay(1, 5, patientList);
            // THEN
            // a ListOfPatientsToDisplay object is returned with expected elements
            assertEquals(1, result.getActivePage());
            assertEquals(3, result.getPatientList().size());
            assertEquals(1, result.getTotalNumberOfPages());
            assertEquals(1, result.getPagesToDisplay()[0]);
            assertNull(result.getPagesToDisplay()[1]);
            assertNull(result.getPagesToDisplay()[2]);
        }

        @Test
        @DisplayName("GIVEN a list of patientBean with 10 elements, page number 3 and 4 elements by page" +
                "WHEN the function getPatientsToDisplay is called " +
                "THEN a ListOfPatientsToDisplay object is returned with expected elements.")
        void getPatientsToDisplayLastPageTest() {
            //GIVEN
            //a list of patientBean with 10 elements, page number 3 and 4 elements by page
            List<PatientBean> patientList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                PatientBean patientBean = new PatientBean();
                patientList.add(patientBean);
            }
            // WHEN
            //the function getPatientsToDisplay is called
            ListOfPatientsToDisplay result = patientUIService.getPatientsToDisplay(3, 4, patientList);
            // THEN
            // a ListOfPatientsToDisplay object is returned with expected elements
            assertEquals(3, result.getActivePage());
            assertEquals(2, result.getPatientList().size());
            assertEquals(3, result.getTotalNumberOfPages());
            assertEquals(1, result.getPagesToDisplay()[0]);
            assertEquals(2, result.getPagesToDisplay()[1]);
            assertEquals(3, result.getPagesToDisplay()[2]);
        }

        @Test
        @DisplayName("GIVEN a list of patientBean with 15 elements, page number 3 and 3 elements by page" +
                "WHEN the function getPatientsToDisplay is called " +
                "THEN a ListOfPatientsToDisplay object is returned with expected elements.")
        void getPatientsToDisplayMiddlePageTest() {
            //GIVEN
            //a list of patientBean with 15 elements, page number 3 and 3 elements by page
            List<PatientBean> patientList = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                PatientBean patientBean = new PatientBean();
                patientList.add(patientBean);
            }
            // WHEN
            //the function getPatientsToDisplay is called
            ListOfPatientsToDisplay result = patientUIService.getPatientsToDisplay(3, 3, patientList);
            // THEN
            // a ListOfPatientsToDisplay object is returned with expected elements
            assertEquals(3, result.getActivePage());
            assertEquals(3, result.getPatientList().size());
            assertEquals(5, result.getTotalNumberOfPages());
            assertEquals(2, result.getPagesToDisplay()[0]);
            assertEquals(3, result.getPagesToDisplay()[1]);
            assertEquals(4, result.getPagesToDisplay()[2]);
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("GetMedicalNotesToDisplayTest")
    class GetMedicalNotesToDisplayTests {

        @Test
        @DisplayName("GIVEN a list of medicalNoteBean with 12 elements, page number 1 and 6 elements by page" +
                "WHEN the function getMedicalNotesToDisplay is called " +
                "THEN a ListOfNotesToDisplay object is returned with expected elements.")
        void getMedicalNotesToDisplayTest() {
            //GIVEN
            //a list of medicalNoteBean with 12 elements, page number 1 and 6 elements by page
            List<MedicalNoteBean> noteBeanList = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
                noteBeanList.add(medicalNoteBean);
            }
            // WHEN
            //the function getMedicalNotesToDisplay is called
            ListOfNotesToDisplay result = patientUIService.getMedicalNotesToDisplay(1, 6, noteBeanList);
            // THEN
            // a ListOfNotesToDisplay object is returned with expected elements
            assertEquals(1, result.getActivePage());
            assertEquals(6, result.getMedicalNoteList().size());
            assertEquals(2, result.getTotalNumberOfPages());
            assertEquals(1, result.getPagesToDisplay()[0]);
            assertEquals(2, result.getPagesToDisplay()[1]);
            assertNull(result.getPagesToDisplay()[2]);
        }

        @Test
        @DisplayName("GIVEN a list of medicalNoteBean with 4 elements, page number 1 and 4 elements by page" +
                "WHEN the function getMedicalNotesToDisplay is called " +
                "THEN a ListOfNotesToDisplay object is returned with expected elements.")
        void getMedicalNotesToDisplayAsMuchNotesAsElementsByPageTest() {
            //GIVEN
            //a list of medicalNoteBean with 4 elements, page number 1 and 4 elements by page
            List<MedicalNoteBean> noteBeanList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
                noteBeanList.add(medicalNoteBean);
            }
            // WHEN
            //the function getMedicalNotesToDisplay is called
            ListOfNotesToDisplay result = patientUIService.getMedicalNotesToDisplay(1, 4, noteBeanList);
            // THEN
            // a ListOfNotesToDisplay object is returned with expected elements
            assertEquals(1, result.getActivePage());
            assertEquals(4, result.getMedicalNoteList().size());
            assertEquals(1, result.getTotalNumberOfPages());
            assertEquals(1, result.getPagesToDisplay()[0]);
            assertNull(result.getPagesToDisplay()[1]);
            assertNull(result.getPagesToDisplay()[2]);
        }

        @Test
        @DisplayName("GIVEN a list of medicalNoteBean with 15 elements, page number 3 and 5 elements by page" +
                "WHEN the function getMedicalNotesToDisplay is called " +
                "THEN a ListOfNotesToDisplay object is returned with expected elements.")
        void getMedicalNotesToDisplayLastPageTest() {
            //GIVEN
            //a list of medicalNoteBean with 15 elements, page number 3 and 5 elements by page
            List<MedicalNoteBean> noteBeanList = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
                noteBeanList.add(medicalNoteBean);
            }
            // WHEN
            //the function getMedicalNotesToDisplay is called
            ListOfNotesToDisplay result = patientUIService.getMedicalNotesToDisplay(3, 5, noteBeanList);
            // THEN
            // a ListOfNotesToDisplay object is returned with expected elements
            assertEquals(3, result.getActivePage());
            assertEquals(5, result.getMedicalNoteList().size());
            assertEquals(3, result.getTotalNumberOfPages());
            assertEquals(1, result.getPagesToDisplay()[0]);
            assertEquals(2, result.getPagesToDisplay()[1]);
            assertEquals(3, result.getPagesToDisplay()[2]);
        }

        @Test
        @DisplayName("GIVEN a list of medicalNoteBean with 50 elements, page number 4 and 8 elements by page" +
                "WHEN the function getMedicalNotesToDisplay is called " +
                "THEN a ListOfNotesToDisplay object is returned with expected elements.")
        void getMedicalNotesToDisplayMiddlePageTest() {
            //GIVEN
            //a list of medicalNoteBean with 50 elements, page number 4 and 8 elements by page
            List<MedicalNoteBean> noteBeanList = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
                noteBeanList.add(medicalNoteBean);
            }
            // WHEN
            //the function getMedicalNotesToDisplay is called
            ListOfNotesToDisplay result = patientUIService.getMedicalNotesToDisplay(4, 8, noteBeanList);
            // THEN
            // a ListOfNotesToDisplay object is returned with expected elements
            assertEquals(4, result.getActivePage());
            assertEquals(8, result.getMedicalNoteList().size());
            assertEquals(7, result.getTotalNumberOfPages());
            assertEquals(3, result.getPagesToDisplay()[0]);
            assertEquals(4, result.getPagesToDisplay()[1]);
            assertEquals(5, result.getPagesToDisplay()[2]);
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("GetAllPatientTest")
    class GetAllPatientTests {

        @Test
        @DisplayName("GIVEN" +
                "WHEN the function getAllPatient is called " +
                "THEN a list is returned.")
        void getAllPatientTest() {
            //GIVEN
            //an existing patient
            List<PatientBean> patientBeanList = new ArrayList<>();
            when(patientInformationProxy.getAllPatient()).thenReturn(patientBeanList);
            // WHEN
            //the function getPatientById is called
            List<PatientBean> result = patientUIService.getAllPatient();
            // THEN
            // the expected PatientBean is returned
            assertEquals(patientBeanList, result);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getAllPatient();
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("GetPatientByIdTest")
    class GetPatientByIdTests {

        @Test
        @DisplayName("GIVEN an existing patient" +
                "WHEN the function getPatientById is called " +
                "THEN the expected PatientBean is returned.")
        void getPatientByIdTest() {
            //GIVEN
            //an existing patient
            PatientBean patientBean = new PatientBean();
            when(patientInformationProxy.getPatientById(1)).thenReturn(patientBean);
            // WHEN
            //the function getPatientById is called
            PatientBean result = patientUIService.getPatientById(1);
            // THEN
            // the expected PatientBean is returned
            assertEquals(patientBean, result);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(1);
        }

        @Test
        @DisplayName("GIVEN a non-existing patient" +
                "WHEN the function getPatientById is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        void getPatientByIdNonExistingTest() {
            //GIVEN
            //a non-existing patient
            PatientBean patientBean = new PatientBean();
            when(patientInformationProxy.getPatientById(1)).thenThrow(FeignException.FeignClientException.class);
            // WHEN
            //the function getPatientById is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientUIService.getPatientById(1));
            assertEquals("The patient with id 1 was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientById(1);
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("GetPatientByNameTest")
    class GetPatientByNameTests {

        @Test
        @DisplayName("GIVEN a list returned by proxy" +
                "WHEN the function getPatientByName is called " +
                "THEN the expected list is returned.")
        void getPatientByNameTest() {
            //GIVEN
            //an existing patient
            String familyName = "family";
            String givenName = "given";
            List<PatientBean> patientBeanList = new ArrayList<>();
            when(patientInformationProxy.getPatientByName(familyName, givenName)).thenReturn(patientBeanList);
            // WHEN
            //the function getPatientByName is called
            List<PatientBean> result = patientUIService.getPatientByName(familyName, givenName);
            // THEN
            // the expected list of patientBean is returned
            assertEquals(patientBeanList, result);
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).getPatientByName(familyName, givenName);
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("AddNewPatientTest")
    class AddNewPatientTests {

        @Test
        @DisplayName("GIVEN a patientBean" +
                "WHEN the function addNewPatient is called " +
                "THEN the expected function is called.")
        void addNewPatientTest() {
            //GIVEN
            //a patientBean
            PatientBean patientBean = new PatientBean();
            when(patientInformationProxy.addNewPatient(patientBean)).thenReturn("ok");
            // WHEN
            //the function addNewPatient is called
            patientUIService.addNewPatient(patientBean);
            // THEN
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).addNewPatient(patientBean);
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("UpdatePatientTest")
    class UpdatePatientTests {

        @Test
        @DisplayName("GIVEN an existing patient" +
                "WHEN the function updatePatient is called " +
                "THEN the expected function is called.")
        void updatePatientTest() {
            //GIVEN
            //an existing patient
            PatientBean patientBean = new PatientBean();
            when(patientInformationProxy.updatePatient(1,patientBean)).thenReturn("ok");
            // WHEN
            //the function updatePatient is called
            patientUIService.updatePatient(1,patientBean);
            // THEN
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).updatePatient(1,patientBean);
        }

        @Test
        @DisplayName("GIVEN a non-existing patient" +
                "WHEN the function updatePatient is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        void updatePatientNonExistingTest() {
            //GIVEN
            //a non-existing patient
            PatientBean patientBean = new PatientBean();
            when(patientInformationProxy.updatePatient(1,patientBean)).thenThrow(FeignException.FeignClientException.class);
            // WHEN
            //the function updatePatient is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientUIService.updatePatient(1,patientBean));
            assertEquals("The patient with id 1 was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).updatePatient(1,patientBean);
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("DeletePatientTest")
    class DeletePatientTests {

        @Test
        @DisplayName("GIVEN an existing patient" +
                "WHEN the function deletePatient is called " +
                "THEN the expected function is called.")
        void deletePatientTest() {
            //GIVEN
            //an existing patient
            when(patientInformationProxy.deletePatient(1)).thenReturn("ok");
            // WHEN
            //the function deletePatient is called
            patientUIService.deletePatient(1);
            // THEN
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).deletePatient(1);
        }

        @Test
        @DisplayName("GIVEN a non-existing patient" +
                "WHEN the function deletePatient is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        void deletePatientNonExistingTest() {
            //GIVEN
            //a non-existing patient
            when(patientInformationProxy.deletePatient(1)).thenThrow(FeignException.FeignClientException.class);
            // WHEN
            //the function deletePatient is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientUIService.deletePatient(1));
            assertEquals("The patient with id 1 was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(patientInformationProxy, Mockito.times(1)).deletePatient(1);
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("GetDiabetesRiskTest")
    class GetDiabetesRiskTests {

        @Test
        @DisplayName("GIVEN an existing patient" +
                "WHEN the function getDiabetesRisk is called " +
                "THEN the expected string is returned.")
        void getDiabetesRiskTest() {
            //GIVEN
            //an existing patient
            DiabetesRiskBean diabetesRiskBean = DiabetesRiskBean.NONE;
            when(patientRiskProxy.getDiabetesRisk(1)).thenReturn(diabetesRiskBean);
            // WHEN
            //the function getDiabetesRisk is called
            String result = patientUIService.getDiabetesRisk(1);
            // THEN
            // the expected String is returned
            assertEquals("NONE", result);
            //and the expected methods have been called with expected arguments
            verify(patientRiskProxy, Mockito.times(1)).getDiabetesRisk(1);
        }

        @Test
        @DisplayName("GIVEN a non-existing patient" +
                "WHEN the function getDiabetesRisk is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        void getDiabetesRiskNonExistingTest() {
            //GIVEN
            //a non-existing patient
            when(patientRiskProxy.getDiabetesRisk(1)).thenThrow(FeignException.FeignClientException.class);
            // WHEN
            //the function getDiabetesRisk is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientUIService.getDiabetesRisk(1));
            assertEquals("The patient with id 1 was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(patientRiskProxy, Mockito.times(1)).getDiabetesRisk(1);
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("GetMedicalNoteTest")
    class GetMedicalNoteTests {

        @Test
        @DisplayName("GIVEN an existing note" +
                "WHEN the function getMedicalNote is called " +
                "THEN the expected medicalNoteBean is returned.")
        void getMedicalNoteTest() {
            //GIVEN
            //an existing patient
            MedicalNoteBean medicalNoteBean = new MedicalNoteBean();
            when(medicalNoteProxy.getMedicalNote("id")).thenReturn(medicalNoteBean);
            // WHEN
            //the function getMedicalNote is called
            MedicalNoteBean result = patientUIService.getMedicalNote("id");
            // THEN
            // the expected medicalNoteBean is returned
            assertEquals(medicalNoteBean, result);
            //and the expected methods have been called with expected arguments
            verify(medicalNoteProxy, Mockito.times(1)).getMedicalNote("id");
        }

        @Test
        @DisplayName("GIVEN a non-existing medicalnote" +
                "WHEN the function getMedicalNote is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        void getMedicalNoteNonExistingTest() {
            //GIVEN
            //a non-existing patient
            when(medicalNoteProxy.getMedicalNote("id")).thenThrow(FeignException.FeignClientException.class);
            // WHEN
            //the function getMedicalNote is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientUIService.getMedicalNote("id"));
            assertEquals("The medical note with id id was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(medicalNoteProxy, Mockito.times(1)).getMedicalNote("id");
        }

        @Test
        @DisplayName("GIVEN an id null" +
                "WHEN the function getMedicalNote is called " +
                "THEN null is returned.")
        void getMedicalNoteNullIdTest() {
            //GIVEN
            //an id null
            // WHEN
            //the function getMedicalNote is called
            MedicalNoteBean result = patientUIService.getMedicalNote(null);
            // THEN
            // null is returned
            assertNull(result);
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("GetMedicalNotesByPatientTest")
    class GetMedicalNotesByPatientTests {

        @Test
        @DisplayName("GIVEN a patient id" +
                "WHEN the function getMedicalNotesByPatient is called " +
                "THEN the expected list is called.")
        void getMedicalNotesByPatientTest() {
            //GIVEN
            //a patient id
           List<MedicalNoteBean> medicalNoteBeanList = new ArrayList<>();
            when(medicalNoteProxy.getMedicalNotesByPatient("id")).thenReturn(medicalNoteBeanList);
            // WHEN
            //the function getMedicalNotesByPatient is called
          List<MedicalNoteBean> result = patientUIService.getMedicalNotesByPatient("id");
            // THEN
            //the expected list is returned
            assertEquals(medicalNoteBeanList, result);
            //and the expected methods have been called with expected arguments
            verify(medicalNoteProxy, Mockito.times(1)).getMedicalNotesByPatient("id");
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("AddMedicalNoteTest")
    class AddMedicalNoteTests {

        @Test
        @DisplayName("GIVEN " +
                "WHEN the function addMedicalNote is called " +
                "THEN the expected function is called.")
        void addMedicalNoteTest() {
            //GIVEN
            //

            when(medicalNoteProxy.addMedicalNote("id","content")).thenReturn("ok");
            // WHEN
            //the function addMedicalNote is called
            patientUIService.addMedicalNote("id","content");
            // THEN
            //and the expected methods have been called with expected arguments
            verify(medicalNoteProxy, Mockito.times(1)).addMedicalNote("id","content");
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("UpdateMedicalNoteTest")
    class UpdateMedicalNoteTests {

        @Test
        @DisplayName("GIVEN an existing medical note" +
                "WHEN the function updateMedicalNote is called " +
                "THEN the expected function is called.")
        void updateMedicalNoteTest() {
            //GIVEN
            //an existing medicalNote

            when(medicalNoteProxy.updateMedicalNote("id","content")).thenReturn("ok");
            // WHEN
            //the function updateMedicalNote is called
           patientUIService.updateMedicalNote("id","content");
            // THEN
            //and the expected methods have been called with expected arguments
            verify(medicalNoteProxy, Mockito.times(1)).updateMedicalNote("id","content");
        }

        @Test
        @DisplayName("GIVEN a non-existing medical note" +
                "WHEN the function updateMedicalNote is called " +
                "THEN the expected exception.")
        void updateMedicalNoteNonExistingTest() {
            //GIVEN
            //a non-existing medicalNote

            when(medicalNoteProxy.updateMedicalNote("id","content")).thenThrow(FeignException.FeignClientException.class);
            // WHEN
            //the function updateMedicalNote is called
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientUIService.updateMedicalNote("id","content"));
            assertEquals("The medical note with id id was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(medicalNoteProxy, Mockito.times(1)).updateMedicalNote("id","content");
        }
    }

    @Nested
    @Tag("PatientServiceTests")
    @DisplayName("DeleteMedicalNoteTest")
    class DeleteMedicalNoteTests {

        @Test
        @DisplayName("GIVEN an existing medical note" +
                "WHEN the function deleteMedicalNote is called " +
                "THEN the expected function is called.")
        void updateMedicalNoteTest() {
            //GIVEN
            //an existing medicalNote

            when(medicalNoteProxy.deleteMedicalNote("id")).thenReturn("ok");
            // WHEN
            //the function deleteMedicalNote is called
            patientUIService.deleteMedicalNote("id");
            // THEN
            //and the expected methods have been called with expected arguments
            verify(medicalNoteProxy, Mockito.times(1)).deleteMedicalNote("id");
        }

        @Test
        @DisplayName("GIVEN a non-existing medical note" +
                "WHEN the function deleteMedicalNote is called " +
                "THEN the expected exception.")
        void deleteMedicalNoteNonExistingTest() {
            //GIVEN
            //an non-existing medicalNote

            when(medicalNoteProxy.deleteMedicalNote("id")).thenThrow(FeignException.FeignClientException.class);
            // WHEN
            //the function deleteMedicalNote is called
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientUIService.deleteMedicalNote("id"));
            assertEquals("The medical note with id id was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(medicalNoteProxy, Mockito.times(1)).deleteMedicalNote("id");
        }
    }
}