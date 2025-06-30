package com.multiportal.csvtool.csvimport.Interfaces;

import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.model.PersonCsvRow;
import com.multiportal.csvtool.csvimport.model.PersonEntity;

import java.util.List;

public interface IPersonFactory {
    public List<PersonEntity> ProducePerson(List<PersonCsvRow> rows, CsvUploadEntity csvInfo);
}
