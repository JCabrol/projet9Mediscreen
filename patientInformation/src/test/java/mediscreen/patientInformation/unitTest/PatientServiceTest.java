package mediscreen.patientInformation.unitTest;

import mediscreen.patientInformation.exception.ObjectNotFoundException;
import mediscreen.patientInformation.modele.Patient;
import mediscreen.patientInformation.modele.PatientDTO;
import mediscreen.patientInformation.modele.InfoPatientToUpdateDTO;
import mediscreen.patientInformation.modele.Sex;
import mediscreen.patientInformation.repository.PatientRepository;
import mediscreen.patientInformation.service.PatientService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Tag("patientServiceTest")
@SpringBootTest
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @MockBean
    private PatientRepository patientRepository;

    @Nested
    @Tag("GetAllPatientTest")
    @DisplayName("getAllPatients tests:")
    class GetAllPatientsTest {


        @DisplayName("GIVEN a list containing patients " +
                "WHEN the function getAllPatients is called " +
                "THEN a list of PatientDTO containing expected information is returned.")
        @Test
        void getAllPatientsTest() {

            //GIVEN
            //a list containing patients
            List<Patient> patientList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Patient patient = new Patient("family" + i, "given" + i, LocalDate.of(2000 + i, i + 1, i + 1), Sex.M, "phone" + i, "address" + i);
                patientList.add(patient);
            }
            when(patientRepository.findAllByOrderByFamilyName()).thenReturn(patientList);

            //WHEN
            //the function getAllPatients is called
            List<PatientDTO> result = patientService.getAllPatients();

            //THEN
            //a list of PatientDTO containing expected information is returned
            assertEquals(5, result.size());
            for (int i = 0; i < 5; i++) {
                assertEquals("family" + i, result.get(i).getFamilyName());
                assertEquals("given" + i, result.get(i).getGivenName());
                assertEquals(LocalDate.of(2000 + i, i + 1, i + 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(i).getDateOfBirth());
                assertEquals("M", result.get(i).getSex());
                assertEquals("phone" + i, result.get(i).getPhone());
                assertEquals("address" + i, result.get(i).getAddress());
            }
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findAllByOrderByFamilyName();
        }

        @DisplayName("GIVEN an empty list " +
                "WHEN the function getAllPatients is called " +
                "THEN an empty list is returned.")
        @Test
        void getAllPatientsEmptyListTest() {

            //GIVEN
            //an empty list
            List<Patient> patientList = new ArrayList<>();
            when(patientRepository.findAllByOrderByFamilyName()).thenReturn(patientList);

            //WHEN
            //the function getAllPatients is called
            List<PatientDTO> result = patientService.getAllPatients();

            //THEN
            //an empty list is returned
            assertEquals(0, result.size());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findAllByOrderByFamilyName();
        }
    }

    @Nested
    @Tag("GetPatientByIdTest")
    @DisplayName("getPatientById tests:")
    class GetPatientByIdTest {


        @DisplayName("GIVEN an existing id " +
                "WHEN the function getPatientById is called " +
                "THEN a PatientDTO containing expected information is returned.")
        @Test
        void getPatientByIdTest() {

            //GIVEN
            //an existing id
            int id = 1;
            Patient patient = new Patient("family", "given", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

            //WHEN
            //the function getPatientById is called
            PatientDTO result = patientService.getPatientById(id);

            //THEN
            //a PatientDTO containing expected information is returned
            assertEquals("family", result.getFamilyName());
            assertEquals("given", result.getGivenName());
            assertEquals(LocalDate.of(2000, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.getDateOfBirth());
            assertEquals("M", result.getSex());
            assertEquals("phone", result.getPhone());
            assertEquals("address", result.getAddress());

            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findById(id);
        }

        @DisplayName("GIVEN a non-existing id " +
                "WHEN the function getPatientById is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getPatientByIdNonExistingTest() {

            //GIVEN
            //a non-existing id
            int id = 1;
            when(patientRepository.findById(1)).thenReturn(Optional.empty());

            //WHEN
            //the function getPatientById is called

            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientService.getPatientById(id));
            assertEquals("The patient with id " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findById(id);
        }
    }

    @Nested
    @Tag("GetPatientsByNameTest")
    @DisplayName("getPatientsByName tests:")
    class GetPatientsByNameTest {


        @DisplayName("GIVEN a familyName and a givenName existing given in parameter " +
                "WHEN the function getPatientsByName is called " +
                "THEN a list containing the corresponding patientDTO is returned.")
        @Test
        void getPatientsByNameFamilyNameAndGivenNameExistingTest() {

            //GIVEN
            //a familyName and a givenName existing given in parameter
            String familyName = "familyName";
            String givenName = "givenName";

            Patient patient = new Patient("familyName", "givenName", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            when(patientRepository.findByFamilyNameAndGivenNameOrderByFamilyName(familyName, givenName)).thenReturn(List.of(patient));

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(familyName, givenName);

            //THEN
            //a list containing the corresponding patientDTO is returned
            assertEquals(1, result.size());
            assertEquals("givenName", result.get(0).getGivenName());
            assertEquals("familyName", result.get(0).getFamilyName());
            assertEquals(LocalDate.of(2000, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(0).getDateOfBirth());
            assertEquals("M", result.get(0).getSex());
            assertEquals("phone", result.get(0).getPhone());
            assertEquals("address", result.get(0).getAddress());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findByFamilyNameAndGivenNameOrderByFamilyName(familyName, givenName);
            verify(patientRepository, Mockito.times(0)).findByFamilyNameOrderByFamilyName(anyString());
            verify(patientRepository, Mockito.times(0)).findByGivenNameOrderByFamilyName(anyString());
        }

        @DisplayName("GIVEN a familyName and a givenName existing for several patient given in parameter " +
                "WHEN the function getPatientsByName is called " +
                "THEN a list containing the corresponding patientsDTO is returned.")
        @Test
        void getPatientsByNameFamilyNameAndGivenNameSeveralExistingTest() {

            //GIVEN
            //a familyName and a givenName existing for several patient given in parameter
            String familyName = "familyName";
            String givenName = "givenName";

            Patient patient = new Patient("familyName", "givenName", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            Patient patient2 = new Patient("familyName", "givenName", LocalDate.of(2000, 2, 2), Sex.M, "phone2", "address2");
            Patient patient3 = new Patient("familyName", "givenName", LocalDate.of(2000, 3, 3), Sex.F, "phone3", "address3");
            when(patientRepository.findByFamilyNameAndGivenNameOrderByFamilyName(familyName, givenName)).thenReturn(List.of(patient, patient2, patient3));

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(familyName, givenName);

            //THEN
            //a list containing the corresponding patientsDTO is returned
            assertEquals(3, result.size());
            assertEquals("givenName", result.get(0).getGivenName());
            assertEquals("familyName", result.get(0).getFamilyName());
            assertEquals("givenName", result.get(1).getGivenName());
            assertEquals("familyName", result.get(1).getFamilyName());
            assertEquals("givenName", result.get(2).getGivenName());
            assertEquals("familyName", result.get(2).getFamilyName());
            assertEquals(LocalDate.of(2000, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(0).getDateOfBirth());
            assertEquals("M", result.get(0).getSex());
            assertEquals("phone", result.get(0).getPhone());
            assertEquals("address", result.get(0).getAddress());
            assertEquals(LocalDate.of(2000, 2, 2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(1).getDateOfBirth());
            assertEquals("M", result.get(1).getSex());
            assertEquals("phone2", result.get(1).getPhone());
            assertEquals("address2", result.get(1).getAddress());
            assertEquals(LocalDate.of(2000, 3, 3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(2).getDateOfBirth());
            assertEquals("F", result.get(2).getSex());
            assertEquals("phone3", result.get(2).getPhone());
            assertEquals("address3", result.get(2).getAddress());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findByFamilyNameAndGivenNameOrderByFamilyName(familyName, givenName);
            verify(patientRepository, Mockito.times(0)).findByFamilyNameOrderByFamilyName(anyString());
            verify(patientRepository, Mockito.times(0)).findByGivenNameOrderByFamilyName(anyString());
        }

        @DisplayName("GIVEN a familyName and a givenName non-existing given in parameter " +
                "WHEN the function getPatientsByName is called " +
                "THEN an empty list is returned.")
        @Test
        void getPatientsByNameFamilyNameAndGivenNameNonExistingTest() {

            //GIVEN
            //a familyName and a givenName non-existing given in parameter
            String familyName = "familyName";
            String givenName = "givenName";

            when(patientRepository.findByFamilyNameAndGivenNameOrderByFamilyName(familyName, givenName)).thenReturn(List.of());

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(familyName, givenName);

            //THEN
            //an empty list is returned
            assertEquals(0, result.size());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findByFamilyNameAndGivenNameOrderByFamilyName(familyName, givenName);
            verify(patientRepository, Mockito.times(0)).findByFamilyNameOrderByFamilyName(anyString());
            verify(patientRepository, Mockito.times(0)).findByGivenNameOrderByFamilyName(anyString());
        }

        @DisplayName("GIVEN a familyName existing given in parameter " +
                "WHEN the function getPatientsByName is called " +
                "THEN a list containing the corresponding patientDTO is returned.")
        @Test
        void getPatientsByNameFamilyNameExistingTest() {

            //GIVEN
            //a familyName existing given in parameter
            String familyName = "familyName";

            Patient patient = new Patient("familyName", "givenName", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            when(patientRepository.findByFamilyNameOrderByFamilyName(familyName)).thenReturn(List.of(patient));

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(familyName, null);

            //THEN
            //a list containing the corresponding patientDTO is returned
            assertEquals(1, result.size());
            assertEquals("givenName", result.get(0).getGivenName());
            assertEquals("familyName", result.get(0).getFamilyName());
            assertEquals(LocalDate.of(2000, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(0).getDateOfBirth());
            assertEquals("M", result.get(0).getSex());
            assertEquals("phone", result.get(0).getPhone());
            assertEquals("address", result.get(0).getAddress());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(0)).findByFamilyNameAndGivenNameOrderByFamilyName(anyString(), anyString());
            verify(patientRepository, Mockito.times(1)).findByFamilyNameOrderByFamilyName(familyName);
            verify(patientRepository, Mockito.times(0)).findByGivenNameOrderByFamilyName(anyString());
        }

        @DisplayName("GIVEN a familyName existing for several patient given in parameter " +
                "WHEN the function getPatientsByName is called " +
                "THEN a list containing the corresponding patientsDTO is returned.")
        @Test
        void getPatientsByNameFamilyNameSeveralExistingTest() {

            //GIVEN
            //a familyName existing for several patient given in parameter
            String familyName = "familyName";


            Patient patient = new Patient("familyName", "givenName", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            Patient patient2 = new Patient("familyName", "givenName2", LocalDate.of(2000, 2, 2), Sex.M, "phone2", "address2");
            Patient patient3 = new Patient("familyName", "givenName3", LocalDate.of(2000, 3, 3), Sex.F, "phone3", "address3");
            when(patientRepository.findByFamilyNameOrderByFamilyName(familyName)).thenReturn(List.of(patient, patient2, patient3));

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(familyName, null);

            //THEN
            //a list containing the corresponding patientsDTO is returned
            assertEquals(3, result.size());
            assertEquals("givenName", result.get(0).getGivenName());
            assertEquals("familyName", result.get(0).getFamilyName());
            assertEquals("givenName2", result.get(1).getGivenName());
            assertEquals("familyName", result.get(1).getFamilyName());
            assertEquals("givenName3", result.get(2).getGivenName());
            assertEquals("familyName", result.get(2).getFamilyName());
            assertEquals(LocalDate.of(2000, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(0).getDateOfBirth());
            assertEquals("M", result.get(0).getSex());
            assertEquals("phone", result.get(0).getPhone());
            assertEquals("address", result.get(0).getAddress());
            assertEquals(LocalDate.of(2000, 2, 2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(1).getDateOfBirth());
            assertEquals("M", result.get(1).getSex());
            assertEquals("phone2", result.get(1).getPhone());
            assertEquals("address2", result.get(1).getAddress());
            assertEquals(LocalDate.of(2000, 3, 3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(2).getDateOfBirth());
            assertEquals("F", result.get(2).getSex());
            assertEquals("phone3", result.get(2).getPhone());
            assertEquals("address3", result.get(2).getAddress());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(0)).findByFamilyNameAndGivenNameOrderByFamilyName(anyString(), anyString());
            verify(patientRepository, Mockito.times(1)).findByFamilyNameOrderByFamilyName(familyName);
            verify(patientRepository, Mockito.times(0)).findByGivenNameOrderByFamilyName(anyString());
        }

        @DisplayName("GIVEN a familyName non-existing given in parameter " +
                "WHEN the function getPatientsByName is called " +
                "THEN an empty list is returned.")
        @Test
        void getPatientsByNameFamilyNameNonExistingTest() {

            //GIVEN
            //a familyName and a givenName non-existing given in parameter
            String familyName = "familyName";

            when(patientRepository.findByFamilyNameOrderByFamilyName(familyName)).thenReturn(List.of());

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(familyName, null);

            //THEN
            //an empty list is returned
            assertEquals(0, result.size());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(0)).findByFamilyNameAndGivenNameOrderByFamilyName(anyString(), anyString());
            verify(patientRepository, Mockito.times(1)).findByFamilyNameOrderByFamilyName(familyName);
            verify(patientRepository, Mockito.times(0)).findByGivenNameOrderByFamilyName(anyString());
        }

        @DisplayName("GIVEN a givenName existing given in parameter " +
                "WHEN the function getPatientsByName is called " +
                "THEN a list containing the corresponding patientDTO is returned.")
        @Test
        void getPatientsByNameGivenNameExistingTest() {

            //GIVEN
            //a givenName existing given in parameter

            String givenName = "givenName";

            Patient patient = new Patient("familyName", "givenName", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            when(patientRepository.findByGivenNameOrderByFamilyName(givenName)).thenReturn(List.of(patient));

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(null, givenName);

            //THEN
            //a list containing the corresponding patientDTO is returned
            assertEquals(1, result.size());
            assertEquals("givenName", result.get(0).getGivenName());
            assertEquals("familyName", result.get(0).getFamilyName());
            assertEquals(LocalDate.of(2000, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(0).getDateOfBirth());
            assertEquals("M", result.get(0).getSex());
            assertEquals("phone", result.get(0).getPhone());
            assertEquals("address", result.get(0).getAddress());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(0)).findByFamilyNameAndGivenNameOrderByFamilyName(anyString(), anyString());
            verify(patientRepository, Mockito.times(0)).findByFamilyNameOrderByFamilyName(anyString());
            verify(patientRepository, Mockito.times(1)).findByGivenNameOrderByFamilyName(givenName);
        }

        @DisplayName("GIVEN a givenName existing for several patient given in parameter " +
                "WHEN the function getPatientsByName is called " +
                "THEN a list containing the corresponding patientsDTO is returned.")
        @Test
        void getPatientsByNameGivenNameSeveralExistingTest() {

            //GIVEN
            //a givenName existing for several patient given in parameter

            String givenName = "givenName";

            Patient patient = new Patient("familyName", "givenName", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            Patient patient2 = new Patient("familyName2", "givenName", LocalDate.of(2000, 2, 2), Sex.M, "phone2", "address2");
            Patient patient3 = new Patient("familyName3", "givenName", LocalDate.of(2000, 3, 3), Sex.F, "phone3", "address3");
            when(patientRepository.findByGivenNameOrderByFamilyName(givenName)).thenReturn(List.of(patient, patient2, patient3));

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(null, givenName);

            //THEN
            //a list containing the corresponding patientsDTO is returned
            assertEquals(3, result.size());
            assertEquals("givenName", result.get(0).getGivenName());
            assertEquals("familyName", result.get(0).getFamilyName());
            assertEquals("givenName", result.get(1).getGivenName());
            assertEquals("familyName2", result.get(1).getFamilyName());
            assertEquals("givenName", result.get(2).getGivenName());
            assertEquals("familyName3", result.get(2).getFamilyName());
            assertEquals(LocalDate.of(2000, 1, 1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(0).getDateOfBirth());
            assertEquals("M", result.get(0).getSex());
            assertEquals("phone", result.get(0).getPhone());
            assertEquals("address", result.get(0).getAddress());
            assertEquals(LocalDate.of(2000, 2, 2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(1).getDateOfBirth());
            assertEquals("M", result.get(1).getSex());
            assertEquals("phone2", result.get(1).getPhone());
            assertEquals("address2", result.get(1).getAddress());
            assertEquals(LocalDate.of(2000, 3, 3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(2).getDateOfBirth());
            assertEquals("F", result.get(2).getSex());
            assertEquals("phone3", result.get(2).getPhone());
            assertEquals("address3", result.get(2).getAddress());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(0)).findByFamilyNameAndGivenNameOrderByFamilyName(anyString(), anyString());
            verify(patientRepository, Mockito.times(0)).findByFamilyNameOrderByFamilyName(anyString());
            verify(patientRepository, Mockito.times(1)).findByGivenNameOrderByFamilyName(givenName);
        }

        @DisplayName("GIVEN a givenName non-existing given in parameter " +
                "WHEN the function getPatientsByName is called " +
                "THEN an empty list is returned.")
        @Test
        void getPatientsByNameGivenNameNonExistingTest() {

            //GIVEN
            //a givenName non-existing given in parameter
            String givenName = "givenName";

            when(patientRepository.findByGivenNameOrderByFamilyName(givenName)).thenReturn(List.of());

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(null, givenName);

            //THEN
            //an empty list is returned
            assertEquals(0, result.size());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(0)).findByFamilyNameAndGivenNameOrderByFamilyName(anyString(), anyString());
            verify(patientRepository, Mockito.times(0)).findByFamilyNameOrderByFamilyName(anyString());
            verify(patientRepository, Mockito.times(1)).findByGivenNameOrderByFamilyName(givenName);
        }

        @DisplayName("GIVEN no parameter given " +
                "WHEN the function getPatientsByName is called " +
                "THEN an empty list is returned.")
        @Test
        void getPatientsByNameNoParameterTest() {

            //GIVEN
            //no parameter given

            //WHEN
            //the function getPatientsByName is called
            List<PatientDTO> result = patientService.getPatientsByName(null, null);

            //THEN
            //an empty list is returned
            assertEquals(0, result.size());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(0)).findByFamilyNameAndGivenNameOrderByFamilyName(anyString(), anyString());
            verify(patientRepository, Mockito.times(0)).findByFamilyNameOrderByFamilyName(anyString());
            verify(patientRepository, Mockito.times(0)).findByGivenNameOrderByFamilyName(anyString());
        }
    }

    @Nested
    @Tag("AddNewPatientTest")
    @DisplayName("addNewPatient tests:")
    class AddNewPatientTest {


        @DisplayName("GIVEN a patientDTO with all information given in parameter " +
                "WHEN the function addNewPatient is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void addNewPatientTest() {

            //GIVEN
            //a patientDTO with all information given in parameter
            PatientDTO patientDTO = new PatientDTO("family", "given", "2000-01-01", "M", "phone", "address");
            final ArgumentCaptor<Patient> arg = ArgumentCaptor.forClass(Patient.class);
            when(patientRepository.save(any(Patient.class))).thenReturn(new Patient());

            //WHEN
            //the function addNewPatient is called
            patientService.addNewPatient(patientDTO);

            //THEN
            //the expected methods have been called with expected arguments
            verify(patientRepository).save(arg.capture());
            assertEquals("family", arg.getValue().getFamilyName());
            assertEquals("given", arg.getValue().getGivenName());
            assertEquals(LocalDate.of(2000, 1, 1), arg.getValue().getDateOfBirth());
            assertEquals(Sex.M, arg.getValue().getSex());
            assertEquals("phone", arg.getValue().getPhone());
            assertEquals("address", arg.getValue().getAddress());
        }

        @DisplayName("GIVEN a patientDTO without information " +
                "WHEN the function addNewPatient is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void addNewPatientTestMissingInformation() {

            //GIVEN
            //a patientDTO without information
            PatientDTO patientDTO = new PatientDTO();
            when(patientRepository.save(any(Patient.class))).thenReturn(new Patient());

            //WHEN
            //the function addNewPatient is called
            patientService.addNewPatient(patientDTO);

            //THEN
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).save(any(Patient.class));
        }
    }

    @Nested
    @Tag("UpdatePatientTest")
    @DisplayName("updatePatient tests:")
    class UpdatePatientTest {


        @DisplayName("GIVEN a patientDTO with all information given in parameter " +
                "WHEN the function updatePatient is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void updatePatientTest() {

            //GIVEN
            //a patientDTO with all information given in parameter
            int patientId = 1;
            Patient patient = new Patient("family", "given", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO("family2", "given2", "2000-02-02", "F", "phone2", "address2");
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
            final ArgumentCaptor<Patient> arg = ArgumentCaptor.forClass(Patient.class);
            when(patientRepository.save(any(Patient.class))).thenReturn(new Patient());

            //WHEN
            //the function addNewPatient is called
            patientService.updatePatient(patientId,patientDTO);

            //THEN
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findById(patientId);
            verify(patientRepository).save(arg.capture());
            assertEquals("family2", arg.getValue().getFamilyName());
            assertEquals("given2", arg.getValue().getGivenName());
            assertEquals(LocalDate.of(2000, 2, 2), arg.getValue().getDateOfBirth());
            assertEquals(Sex.F, arg.getValue().getSex());
            assertEquals("phone2", arg.getValue().getPhone());
            assertEquals("address2", arg.getValue().getAddress());
        }

        @DisplayName("GIVEN a patientDTO without information " +
                "WHEN the function updatePatient is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void updatePatientTestMissingInformation() {

            //GIVEN
            //a patientDTO without information
           int patientId = 1;
            Patient patient = new Patient("family", "given", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO();

            when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
            final ArgumentCaptor<Patient> arg = ArgumentCaptor.forClass(Patient.class);
            when(patientRepository.save(any(Patient.class))).thenReturn(new Patient());

            //WHEN
            //the function updatePatient is called
            patientService.updatePatient(patientId,patientDTO);

            //THEN
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findById(patientId);
            verify(patientRepository).save(arg.capture());
            assertEquals("family", arg.getValue().getFamilyName());
            assertEquals("given", arg.getValue().getGivenName());
            assertEquals(LocalDate.of(2000, 1, 1), arg.getValue().getDateOfBirth());
            assertEquals(Sex.M, arg.getValue().getSex());
            assertEquals("phone", arg.getValue().getPhone());
            assertEquals("address", arg.getValue().getAddress());
        }

        @DisplayName("GIVEN a patientDTO with non-existing id " +
                "WHEN the function updatePatient is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void addNewPatientTestMissingInformation() {

            //GIVEN
            //a patientDTO with non-existing id
            int patientId = 1;
            InfoPatientToUpdateDTO patientDTO = new InfoPatientToUpdateDTO();
            when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

            //WHEN
            //the function updatePatient is called

            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientService.updatePatient(patientId,patientDTO));
            assertEquals("The patient with id " + 1 + " was not found, so it couldn't have been updated.", exception.getMessage());
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findById(1);
            verify(patientRepository, Mockito.times(0)).save(any(Patient.class));
        }
    }

    @Nested
    @Tag("DeletePatientTest")
    @DisplayName("deletePatient tests:")
    class DeletePatientTest {


        @DisplayName("GIVEN an existing id " +
                "WHEN the function deletePatient is called " +
                "THEN the expected method have been called with expected arguments.")
        @Test
        void deletePatientTest() {

            //GIVEN
            //an existing id
            int id = 1;
            Patient patient = new Patient("family", "given", LocalDate.of(2000, 1, 1), Sex.M, "phone", "address");
            when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
            doNothing().when(patientRepository).delete(patient);

            //WHEN
            //the function deletePatient is called
            patientService.deletePatient(id);

            //THEN
            //the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findById(id);
            verify(patientRepository, Mockito.times(1)).delete(patient);
        }

        @DisplayName("GIVEN a non-existing id " +
                "WHEN the function deletePatient is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void deletePatientNonExistingTest() {

            //GIVEN
            //a non-existing id
            int id = 1;
            when(patientRepository.findById(1)).thenReturn(Optional.empty());

            //WHEN
            //the function deletePatient is called

            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> patientService.deletePatient(id));
            assertEquals("The patient with id " + id + " was not found, so it couldn't have been deleted.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(patientRepository, Mockito.times(1)).findById(id);
            verify(patientRepository, Mockito.times(0)).deleteById(id);
        }
    }
}
