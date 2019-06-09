package com.ndgndg91.common;

import java.time.LocalDate;

public class MemberUtils {
    public static String getMemberKoreanAge(String stringOfBirth){
        int year = convertStringOfDateToIntegerArray(stringOfBirth);
        LocalDate now = LocalDate.now();
        return String.valueOf(now.getYear() - year + 1);
    }

    private static int convertStringOfDateToIntegerArray(String stringOfBirth){
        String[] component = stringOfBirth.split("-");
        return Integer.parseInt(component[0]);
    }
}
