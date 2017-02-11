package com.saven.batch.util;

import com.saven.batch.domain.Column;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by nrege on 1/30/2017.
 */
public class ColumnUtils {

    public static double getDoubleValue(Column column) {
        if(column.getClazz().isAssignableFrom(Instant.class)) {
            Instant instant = (Instant) column.getVal();
            LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return time.getMinute();
        } else if(column.getClazz().isAssignableFrom(Integer.class)) {
            Integer integer = (Integer) column.getVal();
            return integer;
        } else if(column.getClazz().isAssignableFrom(Double.class)) {
            Double duble = (Double) column.getVal();
            return duble;
        } else if(column.getClazz().isAssignableFrom(String.class)) {
            String string = (String) column.getVal();
            return Double.valueOf(string);
        } else {
            return 0;
        }
    }

}
