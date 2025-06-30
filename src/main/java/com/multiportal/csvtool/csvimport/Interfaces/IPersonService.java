package com.multiportal.csvtool.csvimport.Interfaces;


import com.multiportal.csvtool.csvimport.dto.CsvUploadResult;
import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.model.PersonCsvRow;
import com.multiportal.csvtool.csvimport.model.PersonEntity;


import java.util.List;
import java.util.UUID;

public interface IPersonService {

    public void SaveInBackground(List<PersonCsvRow> data, CsvUploadEntity csvInfo);
    //public List<PersonEntity> FindByCsv(UUID id) throws Exception;
    public CsvUploadResult FindByCsv(UUID id) throws Exception;
    public void deleteById(UUID personId);
}
