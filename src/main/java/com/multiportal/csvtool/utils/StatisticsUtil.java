package com.multiportal.csvtool.utils;

import com.multiportal.csvtool.csvimport.EnumTypes.GenderEnum;
import com.multiportal.csvtool.csvimport.model.PersonEntity;

import java.util.List;

public class StatisticsUtil {

    public static long CountByGender(List<PersonEntity> list, GenderEnum gender){
        return list.stream()
                .filter(p -> p.getGender() != null && p.getGender().equals(gender))
                .count();
    }
    public static double CountOthers(List<PersonEntity> list){
        return list.stream()
                .filter(p -> p.getAge() != null &&
                        !(p.getGender().equals(GenderEnum.Male) || p.getGender().equals(GenderEnum.Female)))
                .count();
    }

    public static double CalculateAvgAge(List<PersonEntity> list, GenderEnum gender) {
        return list.stream()
                .filter(p -> p.getAge() != null
                        && (gender == null || p.getGender().equals(gender)) )
                .mapToInt(PersonEntity::getAge)
                .average().orElse(0.0);
    }

}
