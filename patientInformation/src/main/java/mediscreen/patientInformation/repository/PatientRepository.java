package mediscreen.patientInformation.repository;

import mediscreen.patientInformation.modele.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer> {

    /**
     * Find all patient registered in dataBase, ordered by familyName.
     *
     * @return a list of patient. If there is not any patient, returns empty list.
     */
    List<Patient> findAllByOrderByFamilyName();

    /**
     * Find all patient having the researched familyName and the researched givenName, ordered by familyName.
     *
     * @param givenName  the researched given name
     * @param familyName the researched family name
     * @return the list of corresponding patient. If there is not any corresponding patient, returns empty list.
     */
    List<Patient> findByFamilyNameAndGivenNameOrderByFamilyName(String familyName, String givenName);

    /**
     * Find all patient having the researched familyName, ordered by familyName.
     *
     * @param familyName the researched family name
     * @return the list of corresponding patient. If there is not any corresponding patient, returns empty list.
     */
    List<Patient> findByFamilyNameOrderByFamilyName(String familyName);

    /**
     * Find all patient having the researched givenName, ordered by familyName.
     *
     * @param givenName the researched given name
     * @return the list of corresponding patient. If there is not any corresponding patient, returns empty list.
     */
    List<Patient> findByGivenNameOrderByFamilyName(String givenName);

}
