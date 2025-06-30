package com.multiportal.csvtool.csvimport.repository;

import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.model.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICsvUploadRepository extends JpaRepository<CsvUploadEntity, UUID> {

}
