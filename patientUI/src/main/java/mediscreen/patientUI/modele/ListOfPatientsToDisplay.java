package mediscreen.patientUI.modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mediscreen.patientUI.bean.PatientBean;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ListOfPatientsToDisplay {

    private List<PatientBean> patientList;
    private int activePage;
    private int totalNumberOfPages;
    private Integer[] pagesToDisplay;
}
