package mediscreen.patientRisk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("mediscreen.patientRisk")
@SpringBootApplication
public class PatientRiskApplication {


    public static void main(String[] args) {
        SpringApplication.run(PatientRiskApplication.class, args);
    }

}
