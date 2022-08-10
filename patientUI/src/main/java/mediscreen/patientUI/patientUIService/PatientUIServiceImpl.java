package mediscreen.patientUI.patientUIService;

import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientUIServiceImpl implements PatientUIService {

    @Override
    public ListOfPatientsToDisplay getPatientsToDisplay(int pageNumber, int numberOfPatientByPage, List<PatientBean> patientList) {
        int totalOfPatients = patientList.size();
        int totalNumberOfPage = totalOfPatients / numberOfPatientByPage;
        int rest = totalOfPatients % numberOfPatientByPage;
        if (rest != 0) {
            totalNumberOfPage++;
        }
        List<PatientBean> patientsToDisplay;
        if (totalOfPatients < numberOfPatientByPage) {
            patientsToDisplay = patientList;
        } else {
            int lastPatientToDisplay = pageNumber * numberOfPatientByPage;
            int firstPatientToDisplay = lastPatientToDisplay - numberOfPatientByPage;
            if (lastPatientToDisplay > totalOfPatients) {
                patientsToDisplay = patientList.subList(firstPatientToDisplay, totalOfPatients);
            } else {
                patientsToDisplay = patientList.subList(firstPatientToDisplay, lastPatientToDisplay);
            }
        }
        Integer[] pagesToDisplay = new Integer[3];
        if (totalNumberOfPage < 3) {
            for (int i = 0; i < totalNumberOfPage; i++) {
                pagesToDisplay[i] = i + 1;
            }
        } else {
            if (pageNumber == 1) {
                for (int i = 0; i < 3; i++) {
                    pagesToDisplay[i] = i + 1;
                }
            } else {
                if (pageNumber == totalNumberOfPage) {
                    for (int i = totalNumberOfPage - 1; i >= totalNumberOfPage - 3; i--) {
                        pagesToDisplay[i] = i + 1;
                    }
                } else {

                    for (int i = 0; i < 3; i++) {
                        pagesToDisplay[i] = i + pageNumber - 1;

                    }
                }
            }
        }

        return new ListOfPatientsToDisplay(patientsToDisplay, pageNumber, totalNumberOfPage, pagesToDisplay);
    }
}
