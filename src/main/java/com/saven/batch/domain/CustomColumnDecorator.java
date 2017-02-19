package com.saven.batch.domain;

import com.saven.batch.domain.Row;

/**
 * Created by nrege on 1/27/2017.
 */
public interface CustomColumnDecorator {
    void decorate(String lineIdentifier, Row row, Row prev);
}
