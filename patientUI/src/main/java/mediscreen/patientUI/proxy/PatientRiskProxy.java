package mediscreen.patientUI.proxy;

import mediscreen.patientUI.bean.DiabetesRiskBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patientRisk", url = "${patient.risk.proxy.url}")
public interface PatientRiskProxy {

    @GetMapping("/diabetesRisk/{id}")
    DiabetesRiskBean getDiabetesRisk(@PathVariable int id);
}
