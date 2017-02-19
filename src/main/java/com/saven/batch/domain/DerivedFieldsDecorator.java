package com.saven.batch.domain;

import com.saven.batch.domain.util.RegexUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.saven.batch.domain.util.RegexUtils.replaceVarWithVal;

/**
 * Created by nrege on 1/30/2017.
 */
public class DerivedFieldsDecorator implements CustomColumnDecorator{
    Logger logger = Logger.getLogger(DerivedFieldsDecorator.class.getName());

    Map<String, String> customColumns;

    Map<String, List<String>> applicableCustomColumns;

    ExpressionParser parser = new SpelExpressionParser();

    String LOWER_CASE_ALPHABET = "([a-z]+)";
    String ALPHABETS = "[A-z]+";

    String DEFAULT_REPLACEMENT_VALUE = "0";
    String EMPTY_STRING="";

    public Function<String, String> replacer(Row row, Row prev) {
        Map<Integer, Row> map = new HashMap<>();
        map.put(0, prev);
        map.put(1, row);

        return (c) -> {
            Row r = RegexUtils.matches(c, LOWER_CASE_ALPHABET) && map.get(0) != null ? map.get(0) : map.get(1);
            return r.getColumn(c.toUpperCase()).orElse(ColumnBuilder.instance().build(EMPTY_STRING, DEFAULT_REPLACEMENT_VALUE, Double.class)).getVal().toString();
        };
    }

    @Override
    public void decorate(String lineIdentifier, Row row, Row prev) {
        String identifier = row.getColumn(lineIdentifier).get().getVal().toString();
        if (!applicableCustomColumns.containsKey(identifier)) {
            return;
        }

        for (String applicableCustomColumn : applicableCustomColumns.get(identifier)) {
            String expr = refract(replacer(row, prev), applicableCustomColumn);
            String derivedValue = parser.parseExpression(expr).getValue().toString();
            logger.log(Level.INFO, applicableCustomColumn+" : "+derivedValue);

            Column<Double> column = ColumnBuilder.instance().build(applicableCustomColumn, derivedValue, Double.class);
            row.add(column);
        }

    }

    private String refract(Function<String, String> replacer, String applicableCustomColumn) {
        String refractedExpression = customColumns.get(applicableCustomColumn);
        return replaceVarWithVal(refractedExpression, ALPHABETS, replacer, DEFAULT_REPLACEMENT_VALUE);
    }

    public Map<String, String> getCustomColumns() {
        return customColumns;
    }

    public void setCustomColumns(Map<String, String> customColumns) {
        this.customColumns = customColumns;
    }

    public Map<String, List<String>> getApplicableCustomColumns() {
        return applicableCustomColumns;
    }

    public void setApplicableCustomColumns(Map<String, List<String>> applicableCustomColumns) {
        this.applicableCustomColumns = applicableCustomColumns;
    }
}
