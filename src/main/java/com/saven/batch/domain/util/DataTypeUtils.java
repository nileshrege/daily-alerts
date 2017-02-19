package com.saven.batch.domain.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Created by nrege on 1/30/2017.
 */
public class DataTypeUtils {

    public static Instant instantOf(String pattern, String val) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(val, formatter);
        return dateTime.toInstant(ZoneOffset.UTC);
    }

    public static Integer integerOf(String number) {
        return Integer.valueOf(number);
    }

    public static Double doubleOf(String number) {
        return number.equals("Infinity") ? Double.POSITIVE_INFINITY : new Double(number);
    }

}
