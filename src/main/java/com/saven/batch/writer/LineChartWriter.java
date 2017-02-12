package com.saven.batch.writer;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

/**
 * Created by nrege on 1/17/2017.
 */
public class LineChartWriter implements ItemWriter , StepExecutionListener {

    private ExecutionContext executionContext;

    String file;

    @Override
    public void write(List list) throws Exception {

        Map<String, XYSeries> xySeriesMap = (Map<String, XYSeries>) executionContext.get("DATA_SET");

        final XYSeriesCollection dataset = new XYSeriesCollection();

        for(Map.Entry<String, XYSeries> entry : xySeriesMap.entrySet()){
            XYSeries xySeries = entry.getValue();
            System.out.println(xySeries.getItemCount());
            dataset.addSeries(xySeries);
        }

        final JFreeChart chart = ChartFactory.createXYLineChart(
              "Line Chart Demo",
              "X",
              "Y",
               dataset,
               PlotOrientation.VERTICAL,
              true,
              true,
              false
        );

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        plot.setRenderer(renderer);

        int width = 640; /* Width of the image */
        int height = 480; /* Height of the image */
        File file = new File(this.file);

        ChartUtilities.saveChartAsJPEG(file ,chart, width ,height);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
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
