/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import net.finmath.plots.jfreechart.JFreeChartUtilities;

/**
 * Small convenient wrapper for JFreeChart line plot derived.
 * 
 * @author Christian Fries
 */
public class Plot2D implements Plot {

	private double xmin, xmax;
	private int numberOfPointsX;
	private List<Named<DoubleUnaryOperator>> functions;

	private String title = "";
	private String xAxisLabel = "x";
	private String yAxisLabel = "y";
	private NumberFormat xAxisNumberFormat;
	private NumberFormat yAxisNumberFormat;
	private Boolean isLegendVisible = false;

	private transient JFreeChart chart;

	public Plot2D(double xmin, double xmax, int numberOfPointsX, DoubleUnaryOperator function) {
		this(xmin, xmax, numberOfPointsX, Collections.singletonList(new Named<DoubleUnaryOperator>("",function)));
	}

	public Plot2D(double xmin, double xmax, int numberOfPointsX, List<Named<DoubleUnaryOperator>> doubleUnaryOperators) {
		super();
		this.xmin = xmin;
		this.xmax = xmax;
		this.numberOfPointsX = numberOfPointsX;
		this.functions = doubleUnaryOperators;

		if(numberOfPointsX < 2) throw new IllegalArgumentException("Number of points needs to be larger than 1.");
	}

	private void init() {
		XYSeriesCollection data = new XYSeriesCollection();
		for(int functionIndex=0; functionIndex<functions.size(); functionIndex++) {
			XYSeries series = new XYSeries(functions.get(functionIndex).getName());
			DoubleUnaryOperator function = functions.get(functionIndex).getFunction();
			for(int i = 0; i<numberOfPointsX; i++) {
				double x = xmin + i * ((xmax-xmin) / (numberOfPointsX-1));
				double y = function.applyAsDouble(x);
				series.add(x, y);
			}
			data.addSeries(series);			
		}
		StandardXYItemRenderer renderer	= new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
		renderer.setSeriesPaint(0, new java.awt.Color(255, 0,  0));
        renderer.setSeriesPaint(1, new java.awt.Color(0, 255,   0));
        renderer.setSeriesPaint(2, new java.awt.Color(0,   0, 255));
		
		chart = JFreeChartUtilities.getXYPlotChart(title, xAxisLabel, "#.#" /* xAxisNumberFormat */, yAxisLabel, "#.#" /* yAxisNumberFormat */, data, renderer, isLegendVisible);
	}

	@Override
	public void show() throws Exception {
		init();
		JPanel chartPanel = new ChartPanel(chart, 
				800, 400,   // size
				128, 128,   // minimum size
				2024, 2024, // maximum size
				false, true, true, false, true, false);    // useBuffer, properties, save, print, zoom, tooltips

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.add(chartPanel);
				frame.setVisible(true);
				frame.pack();
			}
		});	
	}

	@Override
	public Plot2D setTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	public Plot2D setXAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		return this;
	}

	@Override
	public Plot2D setYAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		return this;
	}

	@Override
	public Plot setZAxisLabel(String zAxisLabel) {
		throw new UnsupportedOperationException("The 2D plot does not suport a z-axis. Try 3D plot instead.");
	}

	/**
	 * @param isLegendVisible the isLegendVisible to set
	 */
	public Plot setIsLegendVisible(Boolean isLegendVisible) {
		this.isLegendVisible = isLegendVisible;
		return this;
	}

}
