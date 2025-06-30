package com.multiportal.csvtool.csvimport.Interfaces;

import com.multiportal.csvtool.csvimport.model.CsvExportEntity;

import java.util.UUID;


public interface ICsvExportService {
    public UUID startExport(UUID uploadId);
    public CsvExportEntity findExport(UUID id );
}
