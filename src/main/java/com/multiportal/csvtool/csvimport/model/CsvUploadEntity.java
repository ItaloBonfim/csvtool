package com.multiportal.csvtool.csvimport.model;



import com.multiportal.csvtool.csvimport.EnumTypes.Status;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "csv_uploads")
public class CsvUploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String filename;
    @Column(name = "numberrecords")
    private Integer numberRecords;
    @Column(name = "uploaddata")
    private LocalDateTime uploadData;
    @OneToMany(mappedBy = "csvUpload", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonEntity> pessoas;
    @Enumerated(EnumType.STRING)
    private Status status;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getNumberRecords() {
        return numberRecords;
    }

    public void setNumberRecords(Integer numberRecords) {
        this.numberRecords = numberRecords;
    }

    public LocalDateTime getUploadData() {
        return uploadData;
    }

    public void setUploadData(LocalDateTime uploadData) {
        this.uploadData = uploadData;
    }

    public List<PersonEntity> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<PersonEntity> pessoas) {
        this.pessoas = pessoas;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
