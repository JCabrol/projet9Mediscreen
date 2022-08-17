package mediscreen.patientUI.proxy;

import mediscreen.patientUI.bean.DiabetesRiskBean;
import mediscreen.patientUI.bean.MedicalNoteBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "patientRisk", url = "localhost:8080")
public interface PatientRiskProxy {

    @GetMapping("/diabetesRisk/{id}")
    DiabetesRiskBean getDiabetesRisk(@PathVariable int id);
}
