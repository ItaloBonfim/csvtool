package com.multiportal.csvtool.csvimport.controller;

import com.multiportal.csvtool.csvimport.Interfaces.ICsvImportService;
import com.multiportal.csvtool.csvimport.Interfaces.ICsvUploadService;
import com.multiportal.csvtool.csvimport.Interfaces.IPersonService;
import com.multiportal.csvtool.csvimport.dto.CsvUploadResult;
import com.multiportal.csvtool.csvimport.EnumTypes.GenderEnum;
import com.multiportal.csvtool.csvimport.EnumTypes.Status;
import com.multiportal.csvtool.utils.PaginationUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.multiportal.csvtool.utils.StatisticsUtil.*;

@Controller
public class CsvUploadController {

    private final ICsvImportService ImportService;
    private final IPersonService personService;
    private final ICsvUploadService uploadService;

    public CsvUploadController(ICsvImportService ImportService, IPersonService personService, ICsvUploadService uploadService) {
        this.ImportService = ImportService;
        this.personService = personService;
        this.uploadService = uploadService;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @GetMapping("/upload-result")
    public String showUploadResult(@RequestParam UUID id,
            @RequestParam(defaultValue = "1") int page, Model model) {
        try {
            /*
            * shows the results of the process file csv file
             */
            CsvUploadResult result = personService.FindByCsv(id); // wrapper contendo upload + pessoas

            PaginationUtils.paginate(result.getPerson(), page, 20, model);
            // important flags
            model.addAttribute("uploadId", id);
            model.addAttribute("upload", result.getUpload());
            model.addAttribute("readonly", result.getUpload().getStatus() != Status.PENDING);
            // properties to show on front-end
            model.addAttribute("femaleQuantity", CountByGender(result.getPerson(), GenderEnum.Female));
            model.addAttribute("maleQuantity", CountByGender(result.getPerson(), GenderEnum.Male));
            model.addAttribute("otherGenres", CountOthers(result.getPerson()));
            model.addAttribute("maleAverage", CalculateAvgAge(result.getPerson(), GenderEnum.Male));
            model.addAttribute("femaleAverage", CalculateAvgAge(result.getPerson(), GenderEnum.Female));
            model.addAttribute("ageAverage", CalculateAvgAge(result.getPerson(), null));

            return "upload-result";
        } catch (Exception e) {
            model.addAttribute("Error", "Não foi possível carregar os dados");
            return "upload-result";
        }
    }


    @PostMapping("/upload")
    public String handleFileUpload(MultipartFile file, Model model,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        /*
         * principal method of tool, this will process the csv file and save on databese
         */
        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("Error", "File is not found");
            return "redirect:/upload";
        }
        var csvResult = ImportService.process(file);
        if (csvResult.getPersonRows().isEmpty()) {
            redirectAttributes.addFlashAttribute("Error",
                    "The file does not contain valid data");
            return "redirect:/upload";
        }
        /*
            when I first try to use the session storage, these are necessary to inform to user
            the statitics of file, but when change to databese, this is not necessary any more
            model.addAttribute("rows", csvResult.getPersonRows());
            model.addAttribute("femaleQuantity", csvResult.getFemaleQuantity());
            model.addAttribute("maleQuantity", csvResult.getMaleQuantity());
            model.addAttribute("otherGenres", csvResult.getOthersGenres());
            model.addAttribute("maleAverage", csvResult.getMaleAverage());
            model.addAttribute("femaleAverage", csvResult.getFemaleAverage());
            model.addAttribute("ageAverage", csvResult.getAgeAverage());

         */

        var history = uploadService.saveHistory("migracao.csv", csvResult.getPersonRows().size());
        personService.SaveInBackground(csvResult.getPersonRows(),history);
        /*
         * this send the user direct to results, but with the asynchronous process
         * if try to send immediate, the results will by null on table.
         * //return "redirect:/upload-result?id="+history.getId();
         */

        return "redirect:/upload-progress?id=" + history.getId();
    }

//    @PostMapping("/save")
//    public String saveCSV(@RequestParam UUID uploadId, RedirectAttributes redirectAttributes) {
//        try {
//            uploadService.updateStatus(uploadId, Status.COMPLETED);
//            redirectAttributes.addFlashAttribute("Message", "Upload finish with sucecces!");
//            return "redirect:/view-upload?id=" + uploadId;
//
//        } catch (Exception ex) {
//            redirectAttributes.addFlashAttribute("Error", "Error on finish upload: " + ex.getMessage());
//            return "redirect:/upload";
//        }
//    }

    @PostMapping("/remove-row")
    public String removeRow(@RequestParam UUID personId, @RequestParam UUID uploadId, @RequestParam int page,
                             Model model) throws Exception {
        /*
         * remove rows from database, but only works if the flag is pending, otherwise, is not possible
         */
        personService.deleteById(personId);
        CsvUploadResult updated = personService.FindByCsv(uploadId);
        uploadService.updateRegisters(uploadId);
        PaginationUtils.paginate(updated.getPerson(), page, 20, model);

        model.addAttribute("readonly", updated.getUpload().getStatus() != Status.PENDING);
        model.addAttribute("uploadId", uploadId);

        return "upload-result :: tableBody";
    }

    @GetMapping("/upload-progress")
    public String showProgressPage(@RequestParam UUID id, Model model){
        // page to await the upload csv on database
        model.addAttribute("uploadId", id);
        return "upload-progress";
    }

    @GetMapping("/progress-status")
    @ResponseBody
    public Map<String, Object> getProgressStatus (@RequestParam UUID id) {
        /*
        * Is used to inform to the user the progress of upload csv
        * remember, the upload file is an async process.
        */

        int progress = uploadService.getProgress(id);
        boolean completed = progress >= 100;

        Map<String, Object> response = new HashMap<>();
        response.put("progress", progress);
        response.put("completed", completed);

        return response;
    }

    @PostMapping("/confirm-upload")
    public String confirmUpload(@RequestParam UUID id, RedirectAttributes redirectAttributes) {
        // sets a flag Pending to complete when the user click o save botton
        uploadService.updateStatus(id, Status.COMPLETED);
        redirectAttributes.addFlashAttribute("Message", "Upload finalizado com sucesso!");
        return "redirect:/upload-result?id=" + id;
    }

    @GetMapping("/start-export")
    public String startExport(@RequestParam UUID id, RedirectAttributes redirectAttributes) {
        //exportService.initiateExport(id); // Assíncrono
        return "redirect:/export-progress?id=" + id;
    }

}
