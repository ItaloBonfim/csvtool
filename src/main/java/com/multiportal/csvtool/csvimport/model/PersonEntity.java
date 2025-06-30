package com.multiportal.csvtool.csvimport.model;

import com.multiportal.csvtool.csvimport.EnumTypes.GenderEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String middlename;
    private String email;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    @Column(name = "ip_acess")
    private String ipAcess;
    private Integer age;
    @Column(name = "approximate_date_of_birth")
    private LocalDate approximateDateOfbirth;

    @ManyToOne
    @JoinColumn(name = "csv_upload_id")
    private CsvUploadEntity csvUpload;

    public CsvUploadEntity getCsvUpload() {
        return csvUpload;
    }

    public void setCsvUpload(CsvUploadEntity csvUpload) {
        this.csvUpload = csvUpload;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getIpAcess() {
        return ipAcess;
    }

    public void setIpAcess(String ipAcess) {
        this.ipAcess = ipAcess;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getApproximateDateOfbirth() {
        return approximateDateOfbirth;
    }

    public void setApproximateDateOfbirth(LocalDate approximateDateOfbirth) {
        this.approximateDateOfbirth = approximateDateOfbirth;
    }
}
