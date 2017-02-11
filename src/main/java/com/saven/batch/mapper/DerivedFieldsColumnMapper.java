package com.saven.batch.mapper;

import com.saven.batch.decorator.CustomColumnDecorator;
import com.saven.batch.domain.Row;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nrege on 1/30/2017.
 */
public class DerivedFieldsColumnMapper extends StandardFieldsColumnMapper implements IndexedFieldSetMapper<Row>{

    String ROW_CONTEXT = "ROW_CONTEXT";

    CustomColumnDecorator customColumnDecorator;

    @Override
    public Row mapFieldSet(ExecutionContext executionContext, FieldSet fieldSet, int index) throws BindException {

        Row row = (Row) super.mapFieldSet(executionContext, fieldSet, index);

        Map<String, Row> rowContext = getRowContext(executionContext);
        Row lastRow = rowContext.get(row.getIdentityValue());
        rowContext.put(row.getIdentityValue(), row);

        customColumnDecorator.decorate(identifier, row, lastRow);

        return row;
    }

    private Map<String, Row> getRowContext(ExecutionContext executionContext) {
        if (executionContext.get(ROW_CONTEXT) == null) {
            executionContext.put(ROW_CONTEXT, new HashMap<String, Row>());
        }
        return (Map<String, Row>) executionContext.get(ROW_CONTEXT);
    }

    public CustomColumnDecorator getCustomColumnDecorator() {
        return customColumnDecorator;
    }

    public void setCustomColumnDecorator(CustomColumnDecorator customColumnDecorator) {
        this.customColumnDecorator = customColumnDecorator;
    }
}
