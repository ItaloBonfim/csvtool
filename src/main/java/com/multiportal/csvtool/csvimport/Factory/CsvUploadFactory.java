package com.multiportal.csvtool.csvimport.Factory;

import com.multiportal.csvtool.csvimport.Interfaces.ICsvUploadFactory;
import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.EnumTypes.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CsvUploadFactory implements ICsvUploadFactory {

    public CsvUploadEntity createCsvHistory (String filename, int rowsSize){
        var data = new CsvUploadEntity();
        data.setUploadData(LocalDateTime.now());
        data.setFilename(filename);
        data.setNumberRecords(rowsSize);
        data.setStatus(Status.PENDING);
        return data;
    }
}
