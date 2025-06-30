package com.multiportal.csvtool.csvimport.dto;

import com.multiportal.csvtool.csvimport.model.PersonCsvRow;

import java.util.List;

public class CsvProcessorResult {
    private final List<PersonCsvRow> personRows;
    private final long maleQuantity;
    private final long femaleQuantity;
    private final long othersGenres;
    private final double ageAverage;
    private final double maleAverage;
    private final double femaleAverage;

    public CsvProcessorResult(List<PersonCsvRow> personRows, long femaleQuantity, long maleQuantity,
                              long othersGenres, double femaleAverage, double maleAverage, double ageAverage) {
        this.personRows = personRows;
        this.maleQuantity = maleQuantity;
        this.femaleQuantity = femaleQuantity;
        this.othersGenres = othersGenres;
        this.maleAverage = maleAverage;
        this.femaleAverage = femaleAverage;
        this.ageAverage = ageAverage;
    }

    public List<PersonCsvRow> getPersonRows() {
        return personRows;
    }

    public long getMaleQuantity() {
        return maleQuantity;
    }

    public long getFemaleQuantity() {
        return femaleQuantity;
    }

    public long getOthersGenres() {
        return othersGenres;
    }

    public double getAgeAverage() {
        return ageAverage;
    }

    public double getMaleAverage() {
        return maleAverage;
    }

    public double getFemaleAverage() {
        return femaleAverage;
    }
}
