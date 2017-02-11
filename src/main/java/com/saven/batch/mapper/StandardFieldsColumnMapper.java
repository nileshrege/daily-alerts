package com.saven.batch.mapper;

import com.saven.batch.builder.ColumnBuilder;
import com.saven.batch.domain.Column;
import com.saven.batch.domain.Row;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.util.Map;
import java.util.logging.Logger;

import static com.saven.batch.util.SpreadsheetUtils.columnForIndex;
import static com.saven.batch.util.SpreadsheetUtils.nextColumn;

/**
 * Created by nrege on 1/17/2017.
 */
public class StandardFieldsColumnMapper implements IndexedFieldSetMapper<Row> {

    Logger logger = Logger.getLogger(StandardFieldsColumnMapper.class.getName());

    String identifier;

    Class defaultDataType;

    Map<Integer, Class> indexDataType;

    ColumnBuilder columnBuilder = ColumnBuilder.instance();

    @Override
    public Row mapFieldSet(ExecutionContext executionContext, FieldSet fieldSet, int index) throws BindException {

        Row row = map(fieldSet, index);
        return row;
    }

    private Row map(FieldSet fieldSet, int index) {
        Row row = new Row();
        row.setIndex(index);
        row.setIdentity(identifier);
        for (int columnIndex = 0; columnIndex < fieldSet.getValues().length; columnIndex++) {
            row.add(map(fieldSet.getValues(), columnIndex));
        }
        return row;
    }

    private Column map(String[] values, int i) {
        String name = nextColumn(columnForIndex(i));
        Class dataType = indexDataType.containsKey(i) ? indexDataType.get(i) : defaultDataType;
        String value = values[i];
        return columnBuilder.build(name, value, dataType);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Class getDefaultDataType() {
        return defaultDataType;
    }

    public void setDefaultDataType(Class defaultDataType) {
        this.defaultDataType = defaultDataType;
    }

    public Map<Integer, Class> getIndexDataType() {
        return indexDataType;
    }

    public void setIndexDataType(Map<Integer, Class> indexDataType) {
        this.indexDataType = indexDataType;
    }

}
