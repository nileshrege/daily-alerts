package com.saven.batch.domain.util;

import com.saven.batch.domain.Column;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            return round(duble, 2);
        } else if(column.getClazz().isAssignableFrom(String.class)) {
            String string = (String) column.getVal();
            return Double.valueOf(string);
        } else {
            return 0;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
