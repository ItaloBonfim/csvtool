package com.multiportal.csvtool.csvimport.repository;

import com.multiportal.csvtool.csvimport.model.CsvExportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICsvExportRepository extends JpaRepository<CsvExportEntity, UUID> {
}
