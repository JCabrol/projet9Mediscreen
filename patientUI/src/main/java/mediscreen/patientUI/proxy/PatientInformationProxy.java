package mediscreen.patientUI.proxy;

import mediscreen.patientUI.bean.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "patientInformation", url = "localhost:8081")
public interface PatientInformationProxy {

    @GetMapping(value = "/patient/getAll")
    List<PatientBean> getAllPatient();

    @GetMapping(value = "/patient/getPatient/{patientId}")
    PatientBean getPatientById(@PathVariable("patientId") int patientId);

    @GetMapping(value = "/patient/getPatient")
    List<PatientBean> getPatientByName(@Valid @RequestParam(required = false) String familyName, @Valid @RequestParam(required = false) String givenName);

    @PostMapping("/patient/add")
    String addNewPatient(@RequestBody PatientBean patientBean);

    @PutMapping("/patient/update/{patientId}")
    String updatePatient(@PathVariable int patientId,
                         @RequestBody PatientBean patientBean);

    @DeleteMapping("/patient/delete/{patientId}")
    String deletePatient(@PathVariable("patientId") int patientId);
}
