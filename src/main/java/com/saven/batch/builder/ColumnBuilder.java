package com.saven.batch.builder;

import com.saven.batch.domain.Column;

import java.time.Instant;

import static com.saven.batch.util.DataTypeUtils.*;

/**
 * Created by nrege on 1/30/2017.
 */
public class ColumnBuilder {

    private static ColumnBuilder columnBuilder = new ColumnBuilder();

    public static ColumnBuilder instance() {
        return columnBuilder;
    }

    private ColumnBuilder(){}

    public Column build(String name, String value, Class dataType) {
        Column column = null;
        if(dataType == Instant.class){
            column = new Column<Instant>(name , instantOf("yyyyMMddHHmmss", value));
            column.setClazz(Instant.class);
        } else if(dataType == Integer.class){
            column = new Column<Integer>(name, integerOf(value));
            column.setClazz(Integer.class);
        } else if(dataType == Double.class) {
            column = new Column<Double>(name, doubleOf(value));
            column.setClazz(Double.class);
        } else {
            column = new Column<String>(name, value);
            column.setClazz(String.class);
        }
        return column;
    }

    public static Column<String> EMPTY_COLUMN(){
        Column<String> column = new Column<>("", "");
        column.setClazz(String.class);
        return column;
    }

}
