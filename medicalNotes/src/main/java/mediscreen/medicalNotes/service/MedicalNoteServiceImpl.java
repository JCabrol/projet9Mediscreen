package mediscreen.medicalNotes.service;

import mediscreen.medicalNotes.exception.ObjectNotFoundException;
import mediscreen.medicalNotes.model.MedicalNote;
import mediscreen.medicalNotes.repository.MedicalNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalNoteServiceImpl implements MedicalNoteService {

    @Autowired
    MedicalNoteRepository medicalNoteRepository;

    @Override
    public List<MedicalNote> getAllPatientNotes(String patientId) {
        return medicalNoteRepository.findByPatientIdOrderByNoteDateDesc(patientId);
    }

    @Override
    public MedicalNote getMedicalNote(String id) throws ObjectNotFoundException {
        Optional<MedicalNote> medicalNoteOptional = medicalNoteRepository.findById(id);
        if (medicalNoteOptional.isPresent()) {
            return medicalNoteOptional.get();
        } else {
            throw new ObjectNotFoundException("The medical note number " + id + " was not found.");
        }
    }

    @Override
    public String getAllContentByPatient(String patientId) {
        List<MedicalNote> medicalNoteList = getAllPatientNotes(patientId);

        StringBuilder result = new StringBuilder();
        for(MedicalNote medicalNote:medicalNoteList) {result.append(" ").append(medicalNote.getNoteContent());}
        return result.toString();
    }

    @Override
    public void addMedicalNote(String patientId, String content)
    {
       MedicalNote medicalNote = new MedicalNote();
       medicalNote.setPatientId(patientId);
       medicalNote.setNoteDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
       medicalNote.setNoteContent(content);
       medicalNoteRepository.save(medicalNote);
    }

    @Override
    public void updateMedicalNote(String id, String content)
    {
        MedicalNote medicalNote = getMedicalNote(id);
        medicalNote.setNoteContent(content);
        medicalNoteRepository.save(medicalNote);
    }

    @Override
    public void deleteMedicalNote(String id)
    {
        MedicalNote medicalNote = getMedicalNote(id);
        medicalNoteRepository.delete(medicalNote);
    }
}
