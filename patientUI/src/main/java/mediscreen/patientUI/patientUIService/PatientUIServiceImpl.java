package mediscreen.patientUI.patientUIService;

import mediscreen.patientUI.bean.MedicalNoteBean;
import mediscreen.patientUI.bean.PatientBean;
import mediscreen.patientUI.modele.ListOfNotesToDisplay;
import mediscreen.patientUI.modele.ListOfPatientsToDisplay;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PatientUIServiceImpl implements PatientUIService {

//    @Override
//    public WritingNoteDTO initializeNewWritingNote(int patientId) {
//        List<ContentBean> allNoteContent = new ArrayList<>();
//        ContentBean activeContent = new ContentBean();
//        activeContent.setName("tab 1");
//        allNoteContent.add(activeContent);
//        WritingNoteDTO writingNoteDTO = new WritingNoteDTO();
//        writingNoteDTO.setActiveContentPreviousName("tab 1");
//        writingNoteDTO.setPatientId(String.valueOf(patientId));
//        writingNoteDTO.setActiveContent(activeContent);
//        writingNoteDTO.setAllNoteContents(allNoteContent);
//        return writingNoteDTO;
//    }
//
//    @Override
//    public WritingNoteDTO saveTabInNote(WritingNoteDTO writingNoteDTO, String button) {
//        ContentBean activeContent = writingNoteDTO.getActiveContent();
//        List<ContentBean> allNoteContents = writingNoteDTO.getAllNoteContents();
//        List<String> allNames = new ArrayList<>();
//        if (button.equals("new")) {
//            for (ContentBean contentBean : allNoteContents) {
//                allNames.add(contentBean.getName());
//            }
//            int intToAddAtNameEnd = 2;
//            String name = activeContent.getName();
//            int nameInitialSize = name.length();
//            while (allNames.contains(name)) {
//                name = name.substring(0, nameInitialSize);
//                name = name + "(" + intToAddAtNameEnd + ")";
//                activeContent.setName(name);
//                intToAddAtNameEnd++;
//            }
//        }
//        ContentBean contentToReplace = allNoteContents.stream().filter(content -> content.getName().equals(writingNoteDTO.getActiveContentPreviousName())).collect(Collectors.toList()).get(0);
//        int index = allNoteContents.indexOf(contentToReplace);
//        allNoteContents.set(index, activeContent);
//        writingNoteDTO.setAllNoteContents(allNoteContents);
//        return writingNoteDTO;
//    }
//
//    @Override
//    public WritingNoteDTO addNewTabToNote(WritingNoteDTO writingNoteDTO) {
//        ContentBean activeContent = new ContentBean();
//        List<ContentBean> allNoteContents = writingNoteDTO.getAllNoteContents();
//        String name = "tab " + (allNoteContents.size() + 1);
//        activeContent.setName(name);
//        allNoteContents.add(activeContent);
//        writingNoteDTO.setActiveContentPreviousName(name);
//        writingNoteDTO.setActiveContent(activeContent);
//        writingNoteDTO.setAllNoteContents(allNoteContents);
//        return writingNoteDTO;
//    }
//
//    @Override
//    public WritingNoteDTO selectActiveTabInNote(WritingNoteDTO writingNoteDTO, String button) {
//        ContentBean activeContent = new ContentBean();
//        for (ContentBean contentBean : writingNoteDTO.getAllNoteContents()) {
//            if (contentBean.getName().equals(button)) {
//                activeContent = contentBean;
//                break;
//            }
//        }
//        writingNoteDTO.setActiveContent(activeContent);
//        writingNoteDTO.setActiveContentPreviousName(activeContent.getName());
//        return writingNoteDTO;
//    }
//
//    @Override
//    public WritingNoteDTO removeTabFromNote(WritingNoteDTO writingNoteDTO)
//    {
//        ContentBean activeContent = writingNoteDTO.getActiveContent();
//        List<ContentBean> allNoteContents = writingNoteDTO.getAllNoteContents();
//        ContentBean contentToRemove = allNoteContents.stream().filter(content -> content.getName().equals(activeContent.getName())).collect(Collectors.toList()).get(0);
//        allNoteContents.remove(contentToRemove);
//        writingNoteDTO.setAllNoteContents(allNoteContents);
//        writingNoteDTO = selectActiveTabInNote(writingNoteDTO, allNoteContents.get(0).getName());
//        return writingNoteDTO;
//    }

    @Override
    public List<MedicalNoteBean> createPreviewContentList(List<MedicalNoteBean> medicalNoteList) {
        for (MedicalNoteBean medicalNote : medicalNoteList) {
            String preview = medicalNote.getNoteContent().replaceAll("<[^>]*>", " ");
            if (preview.chars().count() > 90) {
                preview = preview.substring(0, 87) + "...";
            }
            medicalNote.setNoteContent(preview);
        }
        return medicalNoteList;
    }

    //    @Override
//    public ListOfPatientsToDisplay getPatientsToDisplay(int pageNumber, int numberOfPatientByPage, List<PatientBean> patientList) {
//        int totalOfPatients = patientList.size();
//        int totalNumberOfPage = totalOfPatients / numberOfPatientByPage;
//        int rest = totalOfPatients % numberOfPatientByPage;
//        if (rest != 0) {
//            totalNumberOfPage++;
//        }
//        List<PatientBean> patientsToDisplay;
//        if (totalOfPatients < numberOfPatientByPage) {
//            patientsToDisplay = patientList;
//        } else {
//            int lastPatientToDisplay = pageNumber * numberOfPatientByPage;
//            int firstPatientToDisplay = lastPatientToDisplay - numberOfPatientByPage;
//            if (lastPatientToDisplay > totalOfPatients) {
//                patientsToDisplay = patientList.subList(firstPatientToDisplay, totalOfPatients);
//            } else {
//                patientsToDisplay = patientList.subList(firstPatientToDisplay, lastPatientToDisplay);
//            }
//        }
//        Integer[] pagesToDisplay = new Integer[3];
//        if (totalNumberOfPage < 3) {
//            for (int i = 0; i < totalNumberOfPage; i++) {
//                pagesToDisplay[i] = i + 1;
//            }
//        } else {
//            if (pageNumber == 1) {
//                for (int i = 0; i < 3; i++) {
//                    pagesToDisplay[i] = i + 1;
//                }
//            } else {
//                if (pageNumber == totalNumberOfPage) {
//                    for (int i = totalNumberOfPage - 1; i >= totalNumberOfPage - 3; i--) {
//                        pagesToDisplay[i] = i + 1;
//                    }
//                } else {
//
//                    for (int i = 0; i < 3; i++) {
//                        pagesToDisplay[i] = i + pageNumber - 1;
//
//                    }
//                }
//            }
//        }
//
//        return new ListOfPatientsToDisplay(patientsToDisplay, pageNumber, totalNumberOfPage, pagesToDisplay);
//    }
    @Override
    public ListOfPatientsToDisplay getPatientsToDisplay(int pageNumber, int numberOfPatientByPage, List<PatientBean> patientList) {
        int totalOfElements = patientList.size();
        HashMap<String,Integer> elementsForList = getPaginatedElementList(totalOfElements, pageNumber, numberOfPatientByPage);
        patientList = patientList.subList(elementsForList.get("firstListElement"),elementsForList.get("lastListElement"));
        int totalNumberOfPages =elementsForList.get("totalNumberOfPages");
        Integer[] pagesToDisplay = getPagesToDisplay(totalNumberOfPages,pageNumber);
        return new ListOfPatientsToDisplay(patientList, pageNumber, totalNumberOfPages,pagesToDisplay);
    }

    @Override
    public ListOfNotesToDisplay getMedicalNotesToDisplay(int pageNumber, int numberOfNotesByPage, List<MedicalNoteBean> medicalNoteList) {
        int totalOfElements = medicalNoteList.size();
        HashMap<String,Integer> elementsForList = getPaginatedElementList(totalOfElements, pageNumber, numberOfNotesByPage);
        medicalNoteList = medicalNoteList.subList(elementsForList.get("firstListElement"),elementsForList.get("lastListElement"));
        int totalNumberOfPages =elementsForList.get("totalNumberOfPages");
        Integer[] pagesToDisplay = getPagesToDisplay(totalNumberOfPages,pageNumber);
        return new ListOfNotesToDisplay(medicalNoteList, pageNumber, totalNumberOfPages,pagesToDisplay);
    }

    private HashMap<String, Integer> getPaginatedElementList(int totalOfElements, int pageNumber, int numberOfElementsByPage) {

        int totalNumberOfPage = totalOfElements / numberOfElementsByPage;
        int rest = totalOfElements % numberOfElementsByPage;
        if (rest != 0) {
            totalNumberOfPage++;
        }

        int firstListElement;
        int lastListElement;

        if (totalOfElements < numberOfElementsByPage) {
            firstListElement = 0;
            lastListElement = totalOfElements;
        } else {
            lastListElement = pageNumber * numberOfElementsByPage;
            firstListElement = lastListElement - numberOfElementsByPage;
            if (lastListElement > totalOfElements) {
                lastListElement = totalOfElements;
            }
        }

        HashMap<String, Integer> result = new HashMap<>();
        result.put("totalNumberOfPages", totalNumberOfPage);
        result.put("firstListElement", firstListElement);
        result.put("lastListElement", lastListElement);

        return result;
    }

    private Integer[] getPagesToDisplay(int totalNumberOfPage, int pageNumber)
    {
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
        return pagesToDisplay;
    }

}
