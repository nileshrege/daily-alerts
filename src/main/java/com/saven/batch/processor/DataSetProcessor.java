package com.saven.batch.processor;

import com.saven.batch.chart.Series;
import com.saven.batch.domain.Column;
import com.saven.batch.domain.Row;
import org.jfree.data.xy.XYSeries;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashMap;
import java.util.Map;

import static com.saven.batch.util.ColumnUtils.getDoubleValue;

/**
 * Created by nrege on 1/27/2017.
 */
public class DataSetProcessor implements ItemProcessor<Row, XYSeries> , StepExecutionListener {

    private ExecutionContext executionContext;

    Map<String, Series> dataSet;

    String DATA_SET = "DATA_SET";

    @Override
    public XYSeries process(Row row) throws Exception {

        Series series = dataSet.get(row.getIdentityValue());

        if (executionContext.get(DATA_SET) == null) {
            executionContext.put(DATA_SET, new HashMap<String, XYSeries>());
        }
        Map<String, XYSeries> xySeriesMap = (Map<String, XYSeries>) executionContext.get(DATA_SET);
        if (!xySeriesMap.containsKey(row.getIdentityValue())) {
            xySeriesMap.put(row.getIdentityValue(), new XYSeries(series.getName()));
        }

        XYSeries xySeries = xySeriesMap.get(row.getIdentityValue());

        Column xColumn = row.getColumn(series.getX()).get();
        Column yColumn = row.getColumn(series.getY()).get();

        xySeries.add(getDoubleValue(xColumn), getDoubleValue(yColumn));
        System.out.println(" series: "+series.getName()+" X: "+getDoubleValue(xColumn)+ " Y:"+getDoubleValue(yColumn));

        return xySeries;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.executionContext = stepExecution.getExecutionContext();
    }

    public Map<String, Series> getDataSet() {
        return dataSet;
    }

    public void setDataSet(Map<String, Series> dataset) {
        this.dataSet = dataset;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }

}
