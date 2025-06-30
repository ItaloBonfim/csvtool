package com.multiportal.csvtool.csvimport.controller;

import com.multiportal.csvtool.csvimport.Interfaces.ICsvExportService;

import com.multiportal.csvtool.csvimport.repository.ICsvExportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class CsvExportController {

   @Autowired
   private ICsvExportService exportService;

   @PostMapping("/start-export")
   public String startExport(@RequestParam UUID id) {
       // starts the exports csv file in backgound (async)
       UUID exportId = exportService.startExport(id);
       return "redirect:/export-progress?id=" + exportId;
   }

    @GetMapping("/export-progress")
    public String showProgress(@RequestParam UUID id, Model model) {
       //shows to user progress of export
        var result = exportService.findExport(id);
        model.addAttribute("jobId", result.getId());
        model.addAttribute("status", result.getStatus());
        model.addAttribute("progress", result.getProgress());

        return "export-progress";
    }

    @GetMapping("/download-csv")
    public ResponseEntity<Resource> downloadCsv(@RequestParam UUID id) throws IOException {
       // when the exports are complete, then download the file
        File file = new File("exports/" + id + ".csv");
        if (!file.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Csv file not found.");
        }

        Resource resource = new UrlResource(file.toURI());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"export.csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @GetMapping("/export-progress/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProgressStatus(@RequestParam UUID id) {
        //consulting progress of export
        var result = exportService.findExport(id);
        Map<String, Object> response = new HashMap<>();
        response.put("status", result.getStatus());
        response.put("progress", result.getProgress());

        return ResponseEntity.ok(response);
    }

}
