package com.multiportal.csvtool.utils;

import com.multiportal.csvtool.csvimport.dto.CsvProcessorResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

public class SessionUtils {

    public static Optional<CsvProcessorResult> getCsvResultOrDefaultValues(HttpSession session, Model model, int page) {

        Object raw = session.getAttribute("csvRows");

        if(!(raw instanceof CsvProcessorResult csvResult)
                || csvResult.getPersonRows() == null
                || csvResult.getPersonRows().isEmpty()) {

            model.addAttribute("rows", List.of());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", 1);
            model.addAttribute("femaleQuantity", 0);
            model.addAttribute("maleQuantity", 0);
            model.addAttribute("otherGenres", 0);
            model.addAttribute("maleAverage", 0.0);
            model.addAttribute("femaleAverage", 0.0);
            model.addAttribute("ageAverage", 0.0);
            model.addAttribute("readonly", false);
            return Optional.empty();
        }
        return Optional.of((CsvProcessorResult) raw);
    }
}
