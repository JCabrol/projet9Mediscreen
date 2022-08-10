package mediscreen.patientUI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("mediscreen.patientUI")
@SpringBootApplication
public class PatientUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientUiApplication.class, args);
	}

}
