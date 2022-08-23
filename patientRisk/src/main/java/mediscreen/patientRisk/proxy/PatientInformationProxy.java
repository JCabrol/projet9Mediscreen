package mediscreen.patientRisk.proxy;

import mediscreen.patientRisk.bean.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "patientInformation", url = "${patient.information.proxy.url}")
public interface PatientInformationProxy {

    @GetMapping(value = "/patient/getPatient/{patientId}")
    PatientBean getPatientById(@PathVariable("patientId") int patientId);

    @GetMapping("/patient/getPatient")
    List<PatientBean> getPatientsByName(@RequestParam(required = false) String familyName,
                                        @RequestParam(required = false) String givenName);

}
