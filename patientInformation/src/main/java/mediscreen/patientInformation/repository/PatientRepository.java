package mediscreen.patientInformation.repository;

import mediscreen.patientInformation.modele.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer> {

   List<Patient> findAllByOrderByFamilyName();

   List<Patient> findByFamilyNameAndGivenNameOrderByFamilyName(String familyName,String givenName);

    List<Patient> findByFamilyNameOrderByFamilyName(String familyName);

    List<Patient> findByGivenNameOrderByFamilyName(String givenName);

}
