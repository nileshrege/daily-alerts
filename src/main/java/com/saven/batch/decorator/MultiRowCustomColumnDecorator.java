package com.saven.batch.decorator;

import com.saven.batch.builder.ColumnBuilder;
import com.saven.batch.domain.Column;
import com.saven.batch.domain.Row;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.saven.batch.util.RegexUtils.replaceVarWithVal;

/**
 * Created by nrege on 1/27/2017.
 */
public class MultiRowCustomColumnDecorator implements CustomColumnDecorator {

    Logger logger = Logger.getLogger(MultiRowCustomColumnDecorator.class.getName());

    Map<String, String> customColumns;

    Map<String, String> aggregateCustomColumns;

    Map<String, List<String>> applicableCustomColumns;

    ExpressionParser parser = new SpelExpressionParser();

    String UPPER_CASE_ALPHABET = "([A-Z]+)";
    String LOWER_CASE_ALPHABET = "([a-z]+)";
    String DEFAULT_REPLACEMENT_VALUE = "0";

    @Override
    public void decorate(String lineIdentifier, Row row, Row prev) {
        String identifier = row.getColumn(lineIdentifier).get().getVal().toString();
        if (!applicableCustomColumns.containsKey(identifier)) {
            return;
        }

        Function<String, String> thisRowReplacer = (r) ->row.getColumn(r).orElse(ColumnBuilder.EMPTY_COLUMN()).getVal().toString();
        Function<String, String> prevRowReplacer = (r) ->prev.getColumn(r).orElse(ColumnBuilder.EMPTY_COLUMN()).getVal().toString();

        for (String applicableCustomColumn : applicableCustomColumns.get(identifier)) {
            String expr = refract(prev, thisRowReplacer, prevRowReplacer, applicableCustomColumn);
            String derivedValue = parser.parseExpression(expr).getValue().toString();
            logger.log(Level.INFO, applicableCustomColumn+" : "+derivedValue);
            Column<Double> column = ColumnBuilder.instance().build(applicableCustomColumn, derivedValue, Double.class);
            row.add(column);
        }

    }

    private String refract(Row prev, Function<String, String> thisRowReplacer, Function<String, String> prevRowReplacer, String applicableCustomColumn) {
        String refractedExpression = null;
        if (customColumns.containsKey(applicableCustomColumn)) {
            refractedExpression = customColumns.get(applicableCustomColumn);
            refractedExpression = replaceVarWithVal(refractedExpression, UPPER_CASE_ALPHABET, thisRowReplacer, DEFAULT_REPLACEMENT_VALUE);
        }
        else if (aggregateCustomColumns.containsKey(applicableCustomColumn) && prev != null) {
            refractedExpression = aggregateCustomColumns.get(applicableCustomColumn);
            refractedExpression = replaceVarWithVal(refractedExpression, UPPER_CASE_ALPHABET, thisRowReplacer, DEFAULT_REPLACEMENT_VALUE);
            refractedExpression = replaceVarWithVal(refractedExpression, LOWER_CASE_ALPHABET, prevRowReplacer, DEFAULT_REPLACEMENT_VALUE);
        }
        else {
            refractedExpression = "0";
        }
        return refractedExpression;
    }

    public Map<String, String> getCustomColumns() {
        return customColumns;
    }

    public void setCustomColumns(Map<String, String> customColumns) {
        this.customColumns = customColumns;
    }

    public Map<String, String> getAggregateCustomColumns() {
        return aggregateCustomColumns;
    }

    public void setAggregateCustomColumns(Map<String, String> aggregateCustomColumns) {
        this.aggregateCustomColumns = aggregateCustomColumns;
    }

    public Map<String, List<String>> getApplicableCustomColumns() {
        return applicableCustomColumns;
    }

    public void setApplicableCustomColumns(Map<String, List<String>> applicableCustomColumns) {
        this.applicableCustomColumns = applicableCustomColumns;
    }
}
