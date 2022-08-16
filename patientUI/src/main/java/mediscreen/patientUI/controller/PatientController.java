package mediscreen.patientUI.controller;

import lombok.extern.slf4j.Slf4j;
import mediscreen.patientUI.bean.MedicalNoteBean;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.modele.ListOfNotesToDisplay;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import mediscreen.patientUI.modele.PatientSearch;
import mediscreen.patientUI.patientUIService.PatientUIService;
import mediscreen.patientUI.proxy.MedicalNoteProxy;
import mediscreen.patientUI.proxy.PatientInformationProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PatientController {

    private final PatientInformationProxy patientInformationProxy;
    private final MedicalNoteProxy medicalNoteProxy;

    @Autowired
    private PatientUIService patientUIService;

    public PatientController(PatientInformationProxy patientInformationProxy, MedicalNoteProxy medicalNoteProxy) {
        this.patientInformationProxy = patientInformationProxy;
        this.medicalNoteProxy = medicalNoteProxy;
    }

    @GetMapping("/")
    public ModelAndView home() {
        String viewName = "home";
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(viewName, model);
    }

    @PostMapping("/patient/updateNote")
    public RedirectView submitUpdateNote(@Valid @ModelAttribute MedicalNoteBean medicalNoteBean) {
        String patientId = medicalNoteBean.getPatientId();
        String noteId = medicalNoteBean.getId();
                medicalNoteProxy.updateMedicalNote(noteId, medicalNoteBean.getNoteContent());
                return new RedirectView("/patient/getPatient/" + patientId);
    }

    @PostMapping("/patient/addNote")
    public RedirectView submitNewNote(@Valid @ModelAttribute MedicalNoteBean medicalNoteBean) {
        String patientId = medicalNoteBean.getPatientId();
        medicalNoteProxy.addMedicalNote(patientId, medicalNoteBean.getNoteContent());
        return new RedirectView("/patient/getPatient/" + patientId);
    }

    @GetMapping("/patient/getAllPatients")
    public ModelAndView allPatient(@Valid Integer currentPage) {
        String viewName = "selectPatient";
        Map<String, Object> model = new HashMap<>();
        List<PatientBean> patientList = patientInformationProxy.getAllPatient();
        if (currentPage == null) {
            currentPage = 1;
        }
        ListOfPatientsToDisplay listOfPatientsToDisplay = patientUIService.getPatientsToDisplay(currentPage, 6, patientList);
        PatientSearch patientSearch = new PatientSearch();
        model.put("listOfPatientsToDisplay", listOfPatientsToDisplay);
        model.put("linkedFrom", "allPatients");
        model.put("patientSearch", patientSearch);
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/patient/getPatient")
    public ModelAndView getPatientsByName(@Valid @ModelAttribute String familyName, @Valid @ModelAttribute String
            givenName, @Valid Integer currentPage) {
        String viewName = "selectPatient";
        Map<String, Object> model = new HashMap<>();
        List<PatientBean> patientList = patientInformationProxy.getPatientByName(familyName, givenName);
        if (currentPage == null) {
            currentPage = 1;
        }
        ListOfPatientsToDisplay listOfPatientsToDisplay = patientUIService.getPatientsToDisplay(currentPage, 6, patientList);
        PatientSearch patientSearch = new PatientSearch();
        model.put("listOfPatientsToDisplay", listOfPatientsToDisplay);
        model.put("familyName", familyName);
        model.put("givenName", givenName);
        model.put("patientSearch", patientSearch);
        model.put("linkedFrom", "research");
        return new ModelAndView(viewName, model);
    }

//    @GetMapping("/patient/getPatient")
//    public ModelAndView getPatientsByName(ModelMap modelReceived) {
//        String viewName = "selectPatient";
//        Map<String, Object> model = new HashMap<>();
//        List<PatientBean> patientList = patientInformationProxy.getPatientByName((String) modelReceived.get("familyName"), (String) modelReceived.get("givenName"));
//        if (modelReceived.get() == null) {
//            currentPage = 1;
//        }
//        ListOfPatientsToDisplay listOfPatientsToDisplay = patientUIService.getPatientsToDisplay(currentPage, 6, patientList);
//        PatientSearch patientSearch = new PatientSearch();
//        model.put("listOfPatientsToDisplay", listOfPatientsToDisplay);
//        model.put("familyName", familyName);
//        model.put("givenName", givenName);
//        model.put("patientSearch", patientSearch);
//        model.put("linkedFrom", "research");
//        return new ModelAndView(viewName, model);
//    }

    @PostMapping("/patient/getPatient")
    public ModelAndView searchPatientsByName(@Valid @ModelAttribute("patientSearch") PatientSearch
                                                     patientSearch, BindingResult bindingResult, ModelMap model) {
        String viewName = "selectPatient";
        String familyName = patientSearch.getFamilyName();
        String givenName = patientSearch.getGivenName();
        List<PatientBean> patientList = patientInformationProxy.getPatientByName(familyName, givenName);
        ListOfPatientsToDisplay listOfPatientsToDisplay = patientUIService.getPatientsToDisplay(1, 6, patientList);

        model.put("listOfPatientsToDisplay", listOfPatientsToDisplay);
        model.put("familyName", familyName);
        model.put("givenName", givenName);
        model.put("linkedFrom", "research");

        return new ModelAndView(viewName, model);
    }

    @GetMapping("/patient/getPatient/{patientId}")
    public ModelAndView getPatient(@PathVariable int patientId, String noteId,String updateNoteId,Integer currentPage) {
        String viewName = "patientCard";
        Map<String, Object> model = new HashMap<>();
        PatientBean patientBean = patientInformationProxy.getPatientById(patientId);

        List<MedicalNoteBean> medicalNoteList = patientUIService.createPreviewContentList(medicalNoteProxy.getMedicalNotesByPatient(String.valueOf(patientId)));
        if(currentPage==null){
            currentPage=1;
        }
        ListOfNotesToDisplay medicalNotesToDisplay = patientUIService.getMedicalNotesToDisplay(currentPage,5,medicalNoteList);

        MedicalNoteBean readingNote = null;
        if (noteId != null) {
            readingNote = medicalNoteProxy.getMedicalNote(noteId);
        }

        MedicalNoteBean updatingNote = null;
        if (updateNoteId != null) {
            updatingNote = medicalNoteProxy.getMedicalNote(updateNoteId);
        }

        MedicalNoteBean writingNote = new MedicalNoteBean();
        writingNote.setPatientId(String.valueOf(patientId));


        Boolean bindingError = false;

        model.put("patientBean", patientBean);
        model.put("patientBeanToModify", patientBean);
        model.put("bindingError", bindingError);
        model.put("medicalNotesToDisplay", medicalNotesToDisplay);
        model.put("readingNote", readingNote);
        model.put("writingNote", writingNote);
        model.put("updatingNote", updatingNote);
        return new ModelAndView(viewName, model);
    }

    @PostMapping("/patient/addPatient")
    public ModelAndView createNewPatient(@Valid @ModelAttribute("patientBean") PatientBean
                                                 patientBean, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("newPatientForm");
        } else {
            patientInformationProxy.addNewPatient(patientBean);
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/patient/getAllPatients");
            return new ModelAndView(redirect);
        }
    }

    @PostMapping("/patient/updatePatient/{patientId}")
    public ModelAndView updatePatient(@PathVariable int patientId,
                                      @Valid @ModelAttribute("patientBeanToModify") PatientBean patientBeanToModify, BindingResult
                                              bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            Boolean bindingError = true;
            PatientBean patientBean = patientInformationProxy.getPatientById(patientId);
            model.put("patientBean", patientBean);
            model.put("bindingError", bindingError);
            return new ModelAndView("patientCard");
        } else {
            patientInformationProxy.updatePatient(patientId, patientBeanToModify);
            PatientBean patientBean = patientInformationProxy.getPatientById(patientId);
            String viewName = "patientCard";
            model.put("patientBean", patientBean);
            return new ModelAndView(viewName, model);
        }
    }

    @GetMapping("/patient/addPatient")
    public ModelAndView addNewPatient() {
        String viewName = "newPatientForm";
        PatientBean patientBean = new PatientBean();
        Map<String, Object> model = new HashMap<>();
        model.put("patientBean", patientBean);
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/patient/delete/{patientId}")
    public ModelAndView deletePatient(@PathVariable int patientId) {
        patientInformationProxy.deletePatient(patientId);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/patient/getAllPatients");
        return new ModelAndView(redirect);
    }

    @GetMapping("/patient/deleteNote/{patientId}/{noteId}")
    public ModelAndView deleteNote(@PathVariable int patientId,@PathVariable String noteId) {
        medicalNoteProxy.deleteMedicalNote(noteId);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/patient/getPatient/"+patientId);
        return new ModelAndView(redirect);
    }

}
