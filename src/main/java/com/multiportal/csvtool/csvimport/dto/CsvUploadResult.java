package com.multiportal.csvtool.csvimport.dto;

import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.model.PersonEntity;

import java.util.List;

public class CsvUploadResult {
    private CsvUploadEntity upload;
    private List<PersonEntity> person;

    public CsvUploadResult(CsvUploadEntity upload, List<PersonEntity> person) {
        this.upload = upload;
        this.person = person;
    }

    public CsvUploadEntity getUpload() {
        return upload;
    }

    public void setUpload(CsvUploadEntity upload) {
        this.upload = upload;
    }

    public List<PersonEntity> getPerson() {
        return person;
    }

    public void setPerson(List<PersonEntity> person) {
        this.person = person;
    }
}
