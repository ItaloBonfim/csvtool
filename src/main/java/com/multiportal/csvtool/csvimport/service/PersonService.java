package com.multiportal.csvtool.csvimport.service;

import com.multiportal.csvtool.csvimport.Interfaces.ICsvUploadService;
import com.multiportal.csvtool.csvimport.Interfaces.IPersonFactory;
import com.multiportal.csvtool.csvimport.Interfaces.IPersonService;
import com.multiportal.csvtool.csvimport.dto.CsvUploadResult;
import com.multiportal.csvtool.csvimport.model.CsvUploadEntity;
import com.multiportal.csvtool.csvimport.model.PersonCsvRow;
import com.multiportal.csvtool.csvimport.model.PersonEntity;
import com.multiportal.csvtool.csvimport.repository.IPersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class PersonService implements IPersonService {

    //@Autowired
    private IPersonRepository IPersonRepository;
   // @Autowired
    private IPersonFactory personFactory;
    //@Autowired
    private ICsvUploadService uploadService;

    public PersonService(IPersonRepository IPersonRepository, IPersonFactory personFactory,
                         ICsvUploadService uploadService) {
        this.IPersonRepository = IPersonRepository;
        this.personFactory = personFactory;
        this.uploadService = uploadService;
    }

    @Async
    public void SaveInBackground(List<PersonCsvRow> data, CsvUploadEntity csvInfo){
        var processingList = personFactory.ProducePerson(data, csvInfo);

        int batchSize = 100;
        int total = processingList.size();
        UUID uploadId = csvInfo.getId();

        for(int i = 0; i < processingList.size(); i += batchSize){
            int end = Math.min(i + batchSize, processingList.size());
            var batch = processingList.subList(i, end);

            // simulation
            System.out.println("🟡 Processando lote de " + batch.size() + " registros (de " + i + " até " + (end - 1) + ")");
            batch.forEach(p -> System.out.println(" - " + p.getName()));


            IPersonRepository.saveAll(batch);

            int progress = (int) (((double) end / total) * 100);
            uploadService.trackProgress(uploadId, progress);
        }

        System.out.println("✅ Todos os dados processados e prontos para serem persistidos.");

        //uploadService.updateStatus(csvInfo.getId(), Status.COMPLETED);
        //uploadService.updateStatus(csvInfo.getId(), Status.PENDING);
        uploadService.trackProgress(uploadId, 100); //ensure 100%

        System.out.println("✅ Upload concluído e status atualizado.");
    }

    @Override
    public CsvUploadResult FindByCsv(UUID id) throws Exception {
       CsvUploadEntity csv = uploadService.FindHistory(id);
       List<PersonEntity> person = IPersonRepository.findByCsvUpload(csv);
       return new CsvUploadResult(csv, person);
    }

    @Override
    public void deleteById(UUID personId) {
        if(!IPersonRepository.existsById(personId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "person cound't not be found to remove");
        }
        IPersonRepository.deleteById(personId);
    }

}
