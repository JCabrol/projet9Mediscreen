package mediscreen.patientUI.proxy;

import mediscreen.patientUI.bean.MedicalNoteBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "medicalNote", url = "localhost:8082")
public interface MedicalNoteProxy {

    @GetMapping("/patHistory/getAllMedicalNotes/{patientId}")
    List<MedicalNoteBean> getMedicalNotesByPatient(@PathVariable String patientId);

    @GetMapping("/patHistory/getMedicalNote/{id}")
    MedicalNoteBean getMedicalNote(@PathVariable String id);

    @PostMapping("/patHistory/add/{patientId}")
    String addMedicalNote(@PathVariable String patientId, @RequestBody String content);

    @PutMapping("/patHistory/update/{noteId}")
    String updateMedicalNote(@PathVariable String noteId, @RequestBody String content);

    @DeleteMapping("/patHistory/delete/{noteId}")
    String deleteMedicalNote(@PathVariable String noteId);

}
