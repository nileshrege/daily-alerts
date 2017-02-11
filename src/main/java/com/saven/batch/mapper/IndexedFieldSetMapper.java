package com.saven.batch.mapper;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Created by nrege on 1/19/2017.
 */
public interface IndexedFieldSetMapper<T> {
    T mapFieldSet(ExecutionContext executionContext, FieldSet fieldSet, int index) throws BindException;
}
