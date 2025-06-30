package com.multiportal.csvtool.csvimport.controller;

import com.multiportal.csvtool.csvimport.Interfaces.ICsvUploadService;
import com.multiportal.csvtool.csvimport.Interfaces.IPersonService;
import com.multiportal.csvtool.csvimport.dto.CsvUploadResult;
import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.EnumTypes.GenderEnum;
import com.multiportal.csvtool.csvimport.EnumTypes.Status;
import com.multiportal.csvtool.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

import static com.multiportal.csvtool.utils.StatisticsUtil.*;

@Controller
public class HomeController {
    @Autowired
    private ICsvUploadService uploadService;
    @Autowired
    private IPersonService personService;

    @GetMapping("/")
    public String home(Model model){
        List<CsvUploadEntity> uploads = uploadService.ListHistory();
        model.addAttribute("uploads", uploads);
        return "home";
    }
    @GetMapping("/view-upload")
    public String viewUpload(@RequestParam UUID id, @RequestParam(defaultValue = "1") int page,
                             Model model) {
        try {
            CsvUploadResult result = personService.FindByCsv(id);

            PaginationUtils.paginate(result.getPerson(), page, 20, model);

            //most have because are controls of the pages
            model.addAttribute("upload", result.getUpload());
            model.addAttribute("readonly", true);
            model.addAttribute("uploadId", id);
            model.addAttribute
                    ("readonly", result.getUpload().getStatus() != Status.PENDING);

            // statistics
            model.addAttribute("femaleQuantity", CountByGender(result.getPerson(), GenderEnum.Female));
            model.addAttribute("maleQuantity", CountByGender(result.getPerson(), GenderEnum.Male));
            model.addAttribute("otherGenres", CountOthers(result.getPerson()));
            model.addAttribute("maleAverage", CalculateAvgAge(result.getPerson(), GenderEnum.Male));
            model.addAttribute("femaleAverage", CalculateAvgAge(result.getPerson(), GenderEnum.Female));
            model.addAttribute("ageAverage", CalculateAvgAge(result.getPerson(), null));

            return "upload-result";

        } catch (Exception error) {
            model.addAttribute("Error", "Não foi possível carregar os dados");
            return "upload-result";
        }

    }
}
