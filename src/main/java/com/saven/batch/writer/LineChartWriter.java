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
import org.springframework.batch.item.ItemWriter;

/**
 * Created by nrege on 1/17/2017.
 */
public class LineChartWriter implements ItemWriter {
    String file;

    @Override
    public void write(List list) throws Exception {
        Map<String, XYSeries> xySeriesMap = new HashMap<>();
        for(Object series : list){
            XYSeries xySeries = (XYSeries) series;
            xySeriesMap.put(xySeries.getKey().toString(), xySeries);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        for(Map.Entry<String, XYSeries> entry : xySeriesMap.entrySet()){
            dataset.addSeries(entry.getValue());
        }

        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Line Chart Demo",      // chart title
                "X",                      // x axis label
                "Y",                      // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
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
        File lineChart = new File( file );

        ChartUtilities.saveChartAsJPEG(lineChart ,chart, width ,height);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
