package com.multiportal.csvtool.csvimport.Interfaces;

import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.EnumTypes.Status;

import java.util.List;
import java.util.UUID;

public interface ICsvUploadService {
    public CsvUploadEntity saveHistory(String filename, int rowSize);
    public List<CsvUploadEntity> ListHistory();
    public CsvUploadEntity FindHistory(UUID id) throws Exception;
    public void updateStatus (UUID id, Status status);
    public void trackProgress(UUID uploadId, int progress);
    public int getProgress(UUID uploadId);
    public void updateRegisters(UUID id);
}
