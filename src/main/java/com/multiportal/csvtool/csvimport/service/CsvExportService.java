package com.multiportal.csvtool.csvimport.service;

import com.multiportal.csvtool.csvimport.EnumTypes.ExportStatus;
import com.multiportal.csvtool.csvimport.Interfaces.ICsvExportService;
import com.multiportal.csvtool.csvimport.model.CsvExportEntity;
import com.multiportal.csvtool.csvimport.model.PersonEntity;
import com.multiportal.csvtool.csvimport.repository.ICsvExportRepository;
import com.multiportal.csvtool.csvimport.repository.ICsvUploadRepository;
import com.multiportal.csvtool.csvimport.repository.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CsvExportService implements ICsvExportService {
    @Autowired
    private ICsvExportRepository exportRepository;
    //private ICsvUploadRepository uploadRepository;

    @Autowired
    private IPersonRepository personRepository;

    @Override
    public UUID startExport(UUID uploadId) {
        CsvExportEntity job = new CsvExportEntity();
        job.setUploadId(uploadId);
        job.setStatus(ExportStatus.PROCESSING);
        job = exportRepository.save(job);

        //starts exports
        exportCsvAsync(uploadId,job.getId());
        return job.getId();
    }
    public CsvExportEntity findExport(UUID id ){
        return exportRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Async
    private void exportCsvAsync(UUID uploadId, UUID exportId) {
        try {
            //search csv history, to manage and show the progress for user
            CsvExportEntity process = exportRepository.findById(exportId)
                    .orElseThrow(() -> new RuntimeException("CSV ID not identified"));
            // setting name and directory
            File exportFile = new File("exports/" + exportId + ".csv");
            exportFile.getParentFile().mkdirs();
            //Using Buffer to write document .csv
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {
                // sets the header
                writer.write("Nome;Sobrenome;Email;Sexo;IpAcesso;Idade;Nascimento");
                writer.newLine();
                // defines the configuration of the batch and process control
                int page = 0;
                int size = 100;
                boolean hasMore = true;

                long total = personRepository.countByCsvUploadId(uploadId);
                long processed = 0;
                // starting process in batch
                while (hasMore) {
                    // search by data to write using pagination
                    Page<PersonEntity> pageData = personRepository
                            .findByCsvUploadId(uploadId, PageRequest.of(page, size));
                    List<PersonEntity> batch = pageData.getContent();
                    // controls the flows
                    if (batch.isEmpty()) {
                        hasMore = false;
                    } else {
                        // keep write lines
                        for (PersonEntity p : batch) {
                            /*
                           If you use "," then the writing will be done in a single column,
                            in this case A, from the header to the last line.
                            However, if you use ";" then the writing will be done in different columns.
                             */
                            writer.write(String.format("%s;%s;%s;%s;%s;%d;%s",
                                    clean(p.getName()),
                                    clean(p.getMiddlename()),
                                    clean(p.getEmail()),
                                    p.getGender(),
                                    clean(p.getIpAcess()),
                                    p.getAge(),
                                    p.getApproximateDateOfbirth().toString()));
                            writer.newLine();
                        }

                        processed += batch.size();
                        process.setProgress((int) ((processed * 100) / total));
                        exportRepository.save(process);

                        page++;
                    }
                }
                // sets the export completed on database
                process.setCompletedAt(LocalDateTime.now());
                process.setProgress(100); // defines progress
                process.setStatus(ExportStatus.COMPLETED); // sets status
                process.setFinishedAt(LocalDateTime.now()); // sets when finish
                process.setFilePath("/downloads"); // unused, but can be used on future
                exportRepository.save(process); // salve status

            } catch (IOException e) {
                //handling errors
                process.setStatus(ExportStatus.FAILED);
                process.setProgress(0);
                process.setFinishedAt(LocalDateTime.now());
                exportRepository.save(process);
                throw new RuntimeException("Error on exports CSV", e);
            }

        } catch (Exception ex) {
            System.err.println("Unexpected error on try exports CSV" + ex.getMessage());
        }
    }
    // helpers
    private String clean(String value) {
        return value == null ? "" : value.replaceAll("[\\r\\n]+", " ").trim();
    }


}
