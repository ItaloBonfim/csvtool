package com.multiportal.csvtool.csvimport.repository;

import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.model.PersonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IPersonRepository extends JpaRepository<PersonEntity, UUID> {
    public List<PersonEntity> findByCsvUpload(CsvUploadEntity csv);
    public Page<PersonEntity> findByCsvUploadId(UUID id, PageRequest pageRequest);
    public long countByCsvUploadId(UUID id);
}
