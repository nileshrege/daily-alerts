package com.saven.batch.mapper;

import com.saven.batch.domain.Row;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Created by nrege on 1/19/2017.
 */
public class CSVFileLineMapper implements LineMapper<Row>, InitializingBean, StepExecutionListener {

    private ExecutionContext executionContext;

    private LineTokenizer tokenizer;

    private IndexedFieldSetMapper fieldSetMapper;

    public Row mapLine(String line, int lineNumber) throws Exception {
        try{
            return (Row) fieldSetMapper.mapFieldSet(executionContext, tokenizer.tokenize(line), lineNumber);
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new FlatFileParseException("Parsing error at line: " + lineNumber +
                    ", input=[" + line + "]", ex, line, lineNumber);
        }
    }

    public void setLineTokenizer(LineTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void setFieldSetMapper(IndexedFieldSetMapper fieldSetMapper) {
        this.fieldSetMapper = fieldSetMapper;
    }

    public void afterPropertiesSet() {
        Assert.notNull(tokenizer, "The LineTokenizer must be set");
        Assert.notNull(fieldSetMapper, "The FieldSetMapper must be set");
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.executionContext = stepExecution.getExecutionContext();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }
}
