package mediscreen.patientInformation.service;

import mediscreen.patientInformation.exception.ObjectNotFoundException;
import mediscreen.patientInformation.modele.InfoPatientToUpdateDTO;
import mediscreen.patientInformation.modele.Patient;
import mediscreen.patientInformation.modele.PatientDTO;
import mediscreen.patientInformation.modele.Sex;
import mediscreen.patientInformation.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientRepository patientRepository;

    /**
     * Get the list of all registered patients, ordered by family name. If there is no registered patient returns an empty list.
     *
     * @return a list of PatientDTO objects
     */
    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patientList = patientRepository.findAllByOrderByFamilyName();
        List<PatientDTO> result = new ArrayList<>();
        for (Patient patient : patientList) {
            PatientDTO patientDTO = transformPatientToPatientDTO(patient);
            result.add(patientDTO);
        }
        return result;
    }

    /**
     * Get a patient by its id.
     *
     * @param id the id of the researched patient
     * @return a PatientDTO object
     * @throws ObjectNotFoundException if no patient is found with this id
     */
    @Override
    public PatientDTO getPatientById(Integer id) throws ObjectNotFoundException {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();
            return transformPatientToPatientDTO(patient);
        } else {
            throw new ObjectNotFoundException("The patient with id " + id + " was not found.");
        }
    }

    /**
     * Get a list of patients by name.
     * The method search by familyName if givenName is null, by givenName if familyName is null, by family name and given name if none parameter is null.
     * If there are several corresponding results, all are returned in the result list.
     * If there is not any corresponding result, an empty list is returned.
     *
     * @param familyName the familyName of the researched patient
     * @param givenName  the givenName of the researched patient
     * @return a list of PatientDTO objects
     */
    @Override
    public List<PatientDTO> getPatientsByName(String familyName, String givenName) {
        List<Patient> patientsFound = new ArrayList<>();
        if (familyName != null && givenName != null && !familyName.equals("") && !givenName.equals("")) {
            patientsFound = patientRepository.findByFamilyNameAndGivenNameOrderByFamilyName(familyName, givenName);
        } else if (familyName != null && !familyName.equals("")) {
            patientsFound = patientRepository.findByFamilyNameOrderByFamilyName(familyName);
        } else if (givenName != null && !givenName.equals("")) {
            patientsFound = patientRepository.findByGivenNameOrderByFamilyName(givenName);
        }

        List<PatientDTO> result = new ArrayList<>();
        for (Patient patient : patientsFound) {
            PatientDTO patientDTO = transformPatientToPatientDTO(patient);
            result.add(patientDTO);
        }
        return result;
    }

    /**
     * Create a new patient from information given in patientDTO
     *
     * @param patientDTO a PatientDTO object containing all information to create new patient
     */
    @Override
    public void addNewPatient(PatientDTO patientDTO) {
        Patient patient = transformPatientDTOToPatient(patientDTO);
        patientRepository.save(patient);
    }

    /**
     * Update an existing patient.
     *
     * @param patientId  the id of the patient to update
     * @param patientDTO an InfoPatientToUpdateDTO object containing all new information to update a patient
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    @Override
    public void updatePatient(int patientId, InfoPatientToUpdateDTO patientDTO) throws ObjectNotFoundException {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            if (patientDTO.getFamilyName() != null) {
                patient.setFamilyName(patientDTO.getFamilyName());
            }
            if (patientDTO.getGivenName() != null) {
                patient.setGivenName(patientDTO.getGivenName());
            }
            if (patientDTO.getAddress() != null) {
                patient.setAddress(patientDTO.getAddress());
            }
            if (patientDTO.getPhone() != null) {
                patient.setPhone(patientDTO.getPhone());
            }
            if (patientDTO.getDateOfBirth() != null) {
                patient.setDateOfBirth(LocalDate.of(Integer.parseInt(patientDTO.getDateOfBirth().substring(0, 4)), Integer.parseInt(patientDTO.getDateOfBirth().substring(5, 7)), Integer.parseInt(patientDTO.getDateOfBirth().substring(8, 10))));
            }
            if (patientDTO.getSex() != null) {
                patient.setSex(Sex.valueOf(patientDTO.getSex()));
            }
            patientRepository.save(patient);
        } else {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found, so it couldn't have been updated.");
        }
    }

    /**
     * Delete - Delete a patient.
     *
     * @param patientId the id of the patient to delete
     * @throws ObjectNotFoundException if the patient is not found by its id
     */
    @Override
    public void deletePatient(int patientId) throws ObjectNotFoundException {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isPresent()) {
            patientRepository.delete(optionalPatient.get());
        } else {
            throw new ObjectNotFoundException("The patient with id " + patientId + " was not found, so it couldn't have been deleted.");
        }
    }

    /**
     * Transform a Patient object to a PatientDTO object
     *
     * @param patient the patient to transform
     * @return a PatientDTO object containing all information about patient
     */
    private PatientDTO transformPatientToPatientDTO(Patient patient) {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setPatientId(patient.getPatientId());
        patientDTO.setFamilyName(patient.getFamilyName());
        patientDTO.setGivenName(patient.getGivenName());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setDateOfBirth(patient.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        patientDTO.setSex(patient.getSex().toString());
        patientDTO.setPhone(patient.getPhone());
        int age = LocalDate.now().compareTo(patient.getDateOfBirth());
        if (LocalDate.now().getDayOfYear() < patient.getDateOfBirth().getDayOfYear()) {
            age = age - 1;
        }
        patientDTO.setAge(age);
        return patientDTO;
    }

    /**
     * Transform a PatientDTO object to a Patient object
     *
     * @param patientDTO the patientDTO to transform
     * @return a Patient object corresponding to given information in patientDTO
     */
    private Patient transformPatientDTOToPatient(PatientDTO patientDTO) {
        Patient patient = new Patient();
        patient.setFamilyName(patientDTO.getFamilyName());
        patient.setGivenName(patientDTO.getGivenName());
        if (patientDTO.getDateOfBirth() != null) {
            int year = Integer.parseInt(patientDTO.getDateOfBirth().substring(0, 4));
            int month = Integer.parseInt(patientDTO.getDateOfBirth().substring(5, 7));
            int dayOfMonth = Integer.parseInt(patientDTO.getDateOfBirth().substring(8, 10));
            patient.setDateOfBirth(LocalDate.of(year, month, dayOfMonth));
        }
        if (patientDTO.getSex() != null) {
            patient.setSex(Sex.valueOf(patientDTO.getSex()));
        }
        patient.setAddress(patientDTO.getAddress());
        patient.setPhone(patientDTO.getPhone());
        return patient;
    }
}
