package com.multiportal.csvtool.csvimport.model;

import com.multiportal.csvtool.csvimport.EnumTypes.GenderEnum;

import java.time.LocalDate;

public class PersonCsvRow {
    private String Name;
    private String Middlename;
    private String Email;
    private GenderEnum Gender;
    private String IpAcess;
    private int Age;
    private String DateOfBirth;
    private LocalDate ApproximateDateOfbirth;

    public LocalDate getApproximateDateOfbirth() {
        return ApproximateDateOfbirth;
    }

    public void setApproximateDateOfbirth(LocalDate approximateDateOfbirth) {
        ApproximateDateOfbirth = approximateDateOfbirth;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMiddlename() {
        return Middlename;
    }

    public void setMiddlename(String middlename) {
        Middlename = middlename;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public GenderEnum getGender() {
        return Gender;
    }

    public void setGender(GenderEnum gender) {
        Gender = gender;
    }

    public String getIpAcess() {
        return IpAcess;
    }

    public void setIpAcess(String ipAcess) {
        IpAcess = ipAcess;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }
}
