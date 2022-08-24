package mediscreen.patientUI.controller;

import lombok.extern.slf4j.Slf4j;
import mediscreen.patientUI.bean.MedicalNoteBean;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.modele.ListOfNotesToDisplay;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import mediscreen.patientUI.modele.PatientSearch;
import mediscreen.patientUI.patientUIService.PatientUIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PatientController {

    @Autowired
    private PatientUIService patientUIService;

    /**
     * Get home page.
     *
     * @return the home page
     */
    @GetMapping("/")
    public ModelAndView home() {

        String viewName = "home";
        Map<String, Object> model = new HashMap<>();

        return new ModelAndView(viewName, model);
    }

    /**
     * Get the page to select patient when all patient list is requested.
     *
     * @param currentPage the number of the page in the displayed list, default value is 1
     * @return the select patient page with the list of all patient
     */
    @GetMapping("/patient/getAllPatients")
    public ModelAndView allPatient(@RequestParam(defaultValue = "1") Integer currentPage) {

        List<PatientBean> patientList = patientUIService.getAllPatient();
        ListOfPatientsToDisplay listOfPatientsToDisplay = patientUIService.getPatientsToDisplay(currentPage, 6, patientList);
        PatientSearch patientSearch = new PatientSearch();

        String viewName = "selectPatient";
        Map<String, Object> model = new HashMap<>();
        model.put("listOfPatientsToDisplay", listOfPatientsToDisplay);
        model.put("linkedFrom", "allPatients");
        model.put("patientSearch", patientSearch);

        return new ModelAndView(viewName, model);
    }

    /**
     * Submit familyName and/or givenName given in patientSearch.
     * Redirect to getting patient with this name in attribute
     *
     * @param patientSearch an object containing a familyName and/or a givenName
     * @return a redirection to "patient/getPatient"
     */
    @PostMapping("/patient/searchPatient")
    public RedirectView searchPatientsByName(
            @Valid @ModelAttribute("patientSearch") PatientSearch patientSearch, RedirectAttributes redirectAttributes) {

        String familyName = patientSearch.getFamilyName();
        String givenName = patientSearch.getGivenName();
        redirectAttributes.addAttribute("familyName", familyName);
        redirectAttributes.addAttribute("givenName", givenName);

        return new RedirectView("/patient/getPatient", true);
    }

    /**
     * Get the page to select patient when a search has been effectuated.
     *
     * @param currentPage the number of the page in the displayed list, default value is 1
     * @param familyName not required - The family name which is researched
     * @param givenName not required - The given name which is researched
     * @return the select patient page with the list of patient found by research
     */
    @GetMapping("/patient/getPatient")
    public ModelAndView getPatientsByName(@RequestParam(required = false) String familyName,
                                          @RequestParam(required = false) String givenName,
                                          @RequestParam(defaultValue = "1") Integer currentPage) {

        List<PatientBean> patientList = patientUIService.getPatientByName(familyName, givenName);
        ListOfPatientsToDisplay listOfPatientsToDisplay = patientUIService.getPatientsToDisplay(currentPage, 6, patientList);
        PatientSearch patientSearch = new PatientSearch();

        Map<String, Object> model = new HashMap<>();
        String viewName = "selectPatient";
        model.put("listOfPatientsToDisplay", listOfPatientsToDisplay);
        model.put("familyName", familyName);
        model.put("givenName", givenName);
        model.put("patientSearch", patientSearch);
        model.put("linkedFrom", "research");

        return new ModelAndView(viewName, model);
    }

    /**
     * Get the information page concerning a patient.
     *
     * @param patientId the id of the patient whose page has to be displayed
     * @param noteId not required - the id of the note to read, if a reading note is selected
     * @param updateNoteId not required - the id of the note to update, if an updating note is selected
     * @param currentPage the number of the page in the medical note list, default value is 1
     * @return the patient information page with the selected displayed elements if any selected
     */
    @GetMapping("/patient/getPatient/{patientId}")
    public ModelAndView getPatient(@PathVariable int patientId,
                                   @RequestParam(required = false) String noteId,
                                   @RequestParam(required = false) String updateNoteId,
                                   @RequestParam(defaultValue = "1") Integer currentPage) {

        PatientBean patientBean = patientUIService.getPatientById(patientId);
        List<MedicalNoteBean> medicalNoteList = patientUIService.createPreviewContentList(patientUIService.getMedicalNotesByPatient(String.valueOf(patientId)));
        ListOfNotesToDisplay medicalNotesToDisplay = patientUIService.getMedicalNotesToDisplay(currentPage, 5, medicalNoteList);
        String diabetesRisk = patientUIService.getDiabetesRisk(patientId);
        MedicalNoteBean readingNote = patientUIService.getMedicalNote(noteId);
        MedicalNoteBean updatingNote = patientUIService.getMedicalNote(updateNoteId);
        MedicalNoteBean writingNote = new MedicalNoteBean();
        Boolean bindingError = false;

        Map<String, Object> model = new HashMap<>();
        String viewName = "patientCard";
        model.put("patientBean", patientBean);
        model.put("patientBeanToModify", patientBean);
        model.put("bindingError", bindingError);
        model.put("medicalNotesToDisplay", medicalNotesToDisplay);
        model.put("readingNote", readingNote);
        model.put("writingNote", writingNote);
        model.put("updatingNote", updatingNote);
        model.put("diabetesRisk", diabetesRisk);

        return new ModelAndView(viewName, model);
    }

    /**
     * Submit a medical note to update.
     * Redirect to the patient information page
     *
     * @param medicalNoteBean the medical note to update
     * @return a redirection to "patient/getPatient/{patientId}"
     */
    @PostMapping("/patient/updateNote")
    public RedirectView submitUpdateNote(@Valid @ModelAttribute MedicalNoteBean medicalNoteBean) {

        String patientId = medicalNoteBean.getPatientId();
        String noteId = medicalNoteBean.getId();
        patientUIService.updateMedicalNote(noteId, medicalNoteBean.getNoteContent());

        return new RedirectView("/patient/getPatient/" + patientId);
    }

    /**
     * Submit a medical note to add.
     * Redirect to the patient information page
     *
     * @param medicalNoteBean the medical note to add
     * @return a redirection to "patient/getPatient/{patientId}"
     */
    @PostMapping("/patient/addNote")
    public RedirectView submitNewNote(@Valid @ModelAttribute MedicalNoteBean medicalNoteBean) {

        String patientId = medicalNoteBean.getPatientId();
        patientUIService.addMedicalNote(patientId, medicalNoteBean.getNoteContent());

        return new RedirectView("/patient/getPatient/" + patientId);
    }

    /**
     * Submit a new patient to add
     * Redirect to the select patient page
     *
     * @param patientBean the patient to add
     * @return a redirection to "patient/getAllPatient" if there is not any validation error, the form page to add patient with displayed error messages if there is validation error
     */
    @PostMapping("/patient/addPatient")
    public ModelAndView createNewPatient(@Valid @ModelAttribute("patientBean") PatientBean
                                                 patientBean, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {

            return new ModelAndView("newPatientForm");

        } else {
            patientUIService.addNewPatient(patientBean);
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/patient/getAllPatients");

            return new ModelAndView(redirect);
        }
    }

    /**
     * Submit a patient to update
     * Redirect to the patient page
     *
     * @param patientId the id of the patient to update
     * @param patientBeanToModify an object containing information to update for the patient
     * @return a redirection to the patient information page. If there is not any validation error, the updating form is hidden, if there are errors, the updating form is showed with error validation messages.
     */
    @PostMapping("/patient/updatePatient/{patientId}")
    public ModelAndView updatePatient(@PathVariable int patientId,
                                      @Valid @ModelAttribute("patientBeanToModify") PatientBean patientBeanToModify, BindingResult
                                              bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {

            Boolean bindingError = true;
            PatientBean patientBean = patientUIService.getPatientById(patientId);
            List<MedicalNoteBean> medicalNoteList = patientUIService.createPreviewContentList(patientUIService.getMedicalNotesByPatient(String.valueOf(patientId)));
            ListOfNotesToDisplay medicalNotesToDisplay = patientUIService.getMedicalNotesToDisplay(1, 5, medicalNoteList);
            String diabetesRisk = patientUIService.getDiabetesRisk(patientId);
            model.put("medicalNotesToDisplay", medicalNotesToDisplay);
            model.put("diabetesRisk", diabetesRisk);
            model.put("patientBean", patientBean);
            model.put("bindingError", bindingError);
            return new ModelAndView("patientCard");

        } else {

            patientUIService.updatePatient(patientId, patientBeanToModify);
            RedirectView redirect = new RedirectView();
            redirect.setUrl("/patient/getPatient/" + patientId);

            return new ModelAndView(redirect);
        }
    }

    /**
     * Displays the form to add a new patient
     *
     * @return the form page to add a new patient
     */
    @GetMapping("/patient/addPatient")
    public ModelAndView addNewPatient() {

        PatientBean patientBean = new PatientBean();

        Map<String, Object> model = new HashMap<>();
        String viewName = "newPatientForm";
        model.put("patientBean", patientBean);

        return new ModelAndView(viewName, model);
    }

    /**
     * Delete a patient
     * Redirect to the select patient page
     *
     * @param patientId the if of the patient to delete
     * @return a redirection to "patient/getAllPatient"
     */
    @GetMapping("/patient/delete/{patientId}")
    public ModelAndView deletePatient(@PathVariable int patientId) {

        patientUIService.deletePatient(patientId);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/patient/getAllPatients");

        return new ModelAndView(redirect);
    }

    /**
     * Delete a medical note
     * Redirect to the patient information page
     *
     * @param patientId the if of the patient to whom the note is deleted
     * @param noteId the id of the note to delete
     * @return a redirection to "patient/getAllPatient"
     */
    @GetMapping("/patient/deleteNote/{patientId}/{noteId}")
    public ModelAndView deleteNote(@PathVariable int patientId, @PathVariable String noteId) {

        patientUIService.deleteMedicalNote(noteId);
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/patient/getPatient/" + patientId);

        return new ModelAndView(redirect);
    }

}
