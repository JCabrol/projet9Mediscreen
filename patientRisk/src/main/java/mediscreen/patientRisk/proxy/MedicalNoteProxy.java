package mediscreen.patientRisk.proxy;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "medicalNote", url = "localhost:8082")
public interface MedicalNoteProxy {

    @GetMapping("/patHistory/getAllInformation/{patientId}")
    String getAllInformation(@PathVariable String patientId);
}
