package com.saven.batch.writer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by nrege on 1/17/2017.
 */
public class LineChartWriter implements ItemWriter , StepExecutionListener {

    private ExecutionContext executionContext;

    String file;

    String chartName;

    String xAxisName;

    String yAxisName;

    @Override
    public void write(List list) throws Exception {

        Map<String, XYSeries> xySeriesMap = (Map<String, XYSeries>) executionContext.get("DATA_SET");

        final XYSeriesCollection dataset = new XYSeriesCollection();

        for(Map.Entry<String, XYSeries> entry : xySeriesMap.entrySet()){
            XYSeries xySeries = entry.getValue();
            dataset.addSeries(xySeries);
        }

        JFreeChart chart = createChart(dataset);

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

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getXAxisName() {
        return xAxisName;
    }

    public void setXAxisName(String xAxisName) {
        this.xAxisName = xAxisName;
    }

    public String getYAxisName() {
        return yAxisName;
    }

    public void setYAxisName(String yAxisName) {
        this.yAxisName = yAxisName;
    }

    private JFreeChart createChart(XYDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
                getChartName(), // chart title
                getXAxisName(), // domain axis label
                getYAxisName(), // range axis label
                dataset,  // initial series
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        // set chart background
        chart.setBackgroundPaint(Color.white);

        // set a few custom plot features
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        // set the plot's axes to display integers
        TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setStandardTickUnits(ticks);
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setStandardTickUnits(ticks);

        // render shapes and lines
        XYLineAndShapeRenderer renderer =
                new XYLineAndShapeRenderer(true, true);
        plot.setRenderer(renderer);
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);

        // set the renderer's stroke
        Stroke stroke = new BasicStroke(
                3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
        renderer.setBaseOutlineStroke(stroke);

        // label the points
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        XYItemLabelGenerator generator =
                new StandardXYItemLabelGenerator(
                        StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,
                        format, format);
        renderer.setBaseItemLabelGenerator(generator);
        renderer.setBaseItemLabelsVisible(true);

        return chart;
    }
}
