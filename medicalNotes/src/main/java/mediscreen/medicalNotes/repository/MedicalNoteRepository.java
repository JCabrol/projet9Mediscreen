package mediscreen.medicalNotes.repository;

import mediscreen.medicalNotes.model.MedicalNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalNoteRepository extends MongoRepository<MedicalNote, String>
{

    List<MedicalNote> findByPatientIdOrderByNoteDateDesc(String patId);

}
