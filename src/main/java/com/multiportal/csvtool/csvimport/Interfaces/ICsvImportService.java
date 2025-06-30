package com.multiportal.csvtool.csvimport.Interfaces;

import com.multiportal.csvtool.csvimport.dto.CsvProcessorResult;
import org.springframework.web.multipart.MultipartFile;

public interface ICsvImportService {
    public CsvProcessorResult process(MultipartFile file);

}
