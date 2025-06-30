package com.multiportal.csvtool.csvimport.Factory;

import com.multiportal.csvtool.csvimport.Interfaces.IPersonFactory;
import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.model.PersonCsvRow;
import com.multiportal.csvtool.csvimport.model.PersonEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonFactory implements IPersonFactory {

    public List<PersonEntity> ProducePerson(List<PersonCsvRow> rows, CsvUploadEntity csvInfo) {
         List<PersonEntity> DataList = rows.stream().map(row -> {
            PersonEntity result = new PersonEntity();
            result.setName(row.getName());
            result.setMiddlename(row.getMiddlename());
            result.setEmail(row.getEmail());
            result.setGender(row.getGender());
            result.setIpAcess(row.getIpAcess());
            result.setAge(row.getAge());
            result.setApproximateDateOfbirth(row.getApproximateDateOfbirth());
            result.setCsvUpload(csvInfo);
            return result;
        }).toList();
         return DataList;
    }
}
