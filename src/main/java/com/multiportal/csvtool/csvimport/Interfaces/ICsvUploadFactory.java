package com.multiportal.csvtool.csvimport.Interfaces;

import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;


public interface ICsvUploadFactory {
    public CsvUploadEntity createCsvHistory (String filename, int rowsSize);

}
