package com.multiportal.csvtool.csvimport.service;

import com.multiportal.csvtool.csvimport.Interfaces.ICsvUploadFactory;
import com.multiportal.csvtool.csvimport.Interfaces.ICsvUploadService;
import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.EnumTypes.Status;
import com.multiportal.csvtool.csvimport.repository.ICsvUploadRepository;
import com.multiportal.csvtool.csvimport.repository.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CsvUploadService implements ICsvUploadService {

    @Autowired
    private ICsvUploadRepository repository;
    @Autowired
    private IPersonRepository personRepository;
    @Autowired
    private ICsvUploadFactory factory;

    private final Map<UUID, Integer> progressMap = new ConcurrentHashMap<>();


    public CsvUploadService(ICsvUploadRepository repository, ICsvUploadFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    @Override
    public CsvUploadEntity saveHistory(String filename, int rowSize) {
        var data = factory.createCsvHistory(filename, rowSize);
        return repository.save(data);
    }

    @Override
    public List<CsvUploadEntity> ListHistory() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "uploadData"));
    }

    @Override
    public CsvUploadEntity FindHistory(UUID id)  {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Upload não encontrado"));
    }

    @Override
    public void updateStatus(UUID id, Status status) {
        var row = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        var registers = personRepository.countByCsvUploadId(id);
//        row.setNumberRecords((int)registers);
        updateRegisters(id);
        row.setStatus(status);
        repository.save(row);
    }

    public void updateRegisters(UUID id) {
        var row = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var registers = personRepository.countByCsvUploadId(id);
        row.setNumberRecords((int)registers);
        repository.save(row);
    }

    public void trackProgress(UUID uploadId, int progress) {
        progressMap.put(uploadId, progress);
    }

    public int getProgress(UUID uploadId) {
        return progressMap.getOrDefault(uploadId, 0);
    }

}
