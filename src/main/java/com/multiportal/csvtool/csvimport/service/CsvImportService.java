package com.multiportal.csvtool.csvimport.service;

import com.multiportal.csvtool.csvimport.Interfaces.ICsvImportService;
import com.multiportal.csvtool.csvimport.dto.CsvProcessorResult;
import com.multiportal.csvtool.csvimport.model.PersonCsvRow;
import com.multiportal.csvtool.csvimport.EnumTypes.GenderEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvImportService implements ICsvImportService {

    public CsvProcessorResult process(MultipartFile file) {
        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){

            var dataList = reader.lines()
                    .skip(1) // skips the header
                    .filter(line -> line != null && !line.trim().isEmpty()) // filters empty lines;
                    .map(this::parseRow)    // map all csv file properties inside the function
                    .sorted(Comparator.comparing(PersonCsvRow::getName)) // order by data by name
                    .collect(Collectors.toList()); // transform into a list

            // generate statistics
            var totalFemale = setTotal(dataList, GenderEnum.Female);
            var totalMale = setTotal(dataList, GenderEnum.Male);
            var othersGenres = setTotal(dataList, GenderEnum.NoBinary);

            double maleAvarage = setAverage(dataList, GenderEnum.Male);
            double femaleAvarage = setAverage(dataList, GenderEnum.Female);
            double overallAvarage = setAverage(dataList, GenderEnum.NoBinary);

            return new CsvProcessorResult
                    (dataList,
                     totalFemale, totalMale, othersGenres,
                     femaleAvarage, maleAvarage, overallAvarage);

        } catch (Exception e) {
            throw new RuntimeException("Error on read CSV: " + e.getMessage(), e);
        }
    }

    private PersonCsvRow parseRow(String line) {
        String[] parts = line.split(","); // splits the string turn into an array

        //System.out.println(line); // prints all values, just to checking
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid line of CSV" + line);
        }

        PersonCsvRow row = new PersonCsvRow();
        //row.setName(parts[0].trim());
        row.setName((clean(parts[0])));
        //row.setMiddlename(parts[1].trim());
        row.setMiddlename(clean(parts[1]));
        //row.setEmail(parts[2].trim());
        row.setEmail(parts[2].trim());

        genderMap(parts, row); // maps the gender of person

        //row.setIpAcess(parts[4].trim());
        row.setIpAcess(clean(parts[4]));
        row.setAge(Integer.parseInt(parts[5].trim())); //sets age
        treatDateOfBirth(parts, row); // treats the birth of data
        AdjusteBirth(row); // treats the date of birth to an approximate date
        return row;
    }
    // helpers
    private void genderMap(String[] row, PersonCsvRow personObj) {
        // Mapear sexo
        String GenderRaw = row[3].trim().toUpperCase();
        GenderEnum sexo;
        switch (GenderRaw) {
            case "MALE" -> sexo = GenderEnum.Male;
            case "FEMALE" -> sexo = GenderEnum.Female;
            default -> sexo = GenderEnum.NoBinary;
        }
        personObj.setGender(sexo);
    }
    private void treatDateOfBirth(String[] row, PersonCsvRow personObj){
        String nascimentoCompleto = row[6].trim();
        if (nascimentoCompleto.contains(" ")) {
            //row.setDataNascimento(nascimentoCompleto.split(" ")[0].trim()); // pega só a data
            personObj.setDateOfBirth(nascimentoCompleto.split( " ")[0].trim());
        } else if (nascimentoCompleto.matches("\\d+/\\d+/\\d+\\d+")) {
            //row.setDataNascimento(nascimentoCompleto.replaceAll("(\\d+/\\d+/\\d{2}).*", "$1")); // regex: só a parte da data
            personObj.setDateOfBirth(nascimentoCompleto.replaceAll("(\\d+/\\d+/\\d{2}).*", "$1"));
        } else {
            //row.setDataNascimento(nascimentoCompleto); // fallback total
            personObj.setDateOfBirth(nascimentoCompleto);
        }
    }
    private void AdjusteBirth(PersonCsvRow personObj){
        LocalDate today = LocalDate.now();
        LocalDate adjuste = today.minusYears(personObj.getAge());
        personObj.setApproximateDateOfbirth(adjuste);
    }
    private double setAverage(List<PersonCsvRow> dataList, GenderEnum gender){
        var results =  dataList.stream();

        if(gender.equals(GenderEnum.NoBinary) || gender.equals(GenderEnum.None)){
            return results.mapToInt(PersonCsvRow::getAge)
                    .average()
                    .orElse(0.0);
        }
        return results.filter(p -> p.getGender().equals(gender))
                .mapToInt(PersonCsvRow::getAge)
                .average()
                .orElse(0.0);
    }
    private long setTotal(List<PersonCsvRow> dataList, GenderEnum gender){
        var total = dataList.stream();
        if(gender.equals(GenderEnum.Male)){
           return total.filter(p -> p.getGender().equals(gender)).count();
        }
        if(gender.equals(GenderEnum.Female)){
            return total.filter(p -> p.getGender().equals(gender)).count();
        }

        return total.
                filter(p -> !p.getGender().equals(GenderEnum.Female) && !p.getGender().equals(GenderEnum.Male))
                .count();
    }
    private String clean(String value) {
        // clean the value case they have some " on start or in the end of string
        return value == null ? null : value.trim().replaceAll("^\"|\"$", "");
    }

}
