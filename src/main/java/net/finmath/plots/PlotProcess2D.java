/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.function.DoubleFunction;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import net.finmath.plots.jfreechart.JFreeChartUtilities;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;

/**
 * Small convenient wrapper for JFreeChart line plot of a stochastic process.
 *
 * @author Christian Fries
 */
public class PlotProcess2D implements Plot {

	private final TimeDiscretization timeDiscretization;
	private final Named<DoubleFunction<RandomVariable>> process;
	private final int maxNumberOfPaths;

	private String title = "";
	private String xAxisLabel = "x";
	private String yAxisLabel = "y";
	private NumberFormat xAxisNumberFormat;
	private NumberFormat yAxisNumberFormat;
	private Boolean isLegendVisible = false;

	private transient JFreeChart chart;


	/**
	 * Plot the first (maxNumberOfPaths) paths of a time discrete stochastic process.
	 *
	 * @param timeDiscretizationFromArray The time discretization to be used for the x-axis.
	 * @param process The stochastic process to be plotted against the y-axsis (the first n paths are plotted).
	 * @param maxNumberOfPaths Maximum number of path (n) to be plotted.
	 */
	public PlotProcess2D(TimeDiscretization timeDiscretization, Named<DoubleFunction<RandomVariable>> process, int maxNumberOfPaths) {
		super();
		this.timeDiscretization = timeDiscretization;
		this.process = process;
		this.maxNumberOfPaths = maxNumberOfPaths;
	}

	/**
	 * Plot the first 100 paths of a time discrete stochastic process.
	 *
	 * @param timeDiscretizationFromArray The time discretization to be used for the x-axis.
	 * @param process The stochastic process to be plotted against the y-axsis.
	 */
	public PlotProcess2D(TimeDiscretization timeDiscretization, Named<DoubleFunction<RandomVariable>> process) {
		this(timeDiscretization, process, 100);
	}

	private void init() {
		ArrayList<XYSeries> seriesList = new ArrayList<XYSeries>();
		for(double time : timeDiscretization) {
			RandomVariable randomVariable = process.get().apply(time);
			for(int pathIndex=0; pathIndex<Math.min(randomVariable.size(),maxNumberOfPaths); pathIndex++) {
				XYSeries series = pathIndex < seriesList.size() ? seriesList.get(pathIndex) : null;
				if(series == null) {
					series = new XYSeries(pathIndex);
					seriesList.add(pathIndex, series);
				}
				series.add(time, randomVariable.get(pathIndex));
			}
		}
		XYSeriesCollection data = new XYSeriesCollection();
		for(XYSeries series : seriesList) {
			data.addSeries(series);
		}

		StandardXYItemRenderer renderer	= new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
		renderer.setSeriesPaint(0, new java.awt.Color(255, 0,  0));
		renderer.setSeriesPaint(1, new java.awt.Color(0, 255,   0));
		renderer.setSeriesPaint(2, new java.awt.Color(0,   0, 255));

		chart = JFreeChartUtilities.getXYPlotChart(title, xAxisLabel, "#.#" /* xAxisNumberFormat */, yAxisLabel, "#.#" /* yAxisNumberFormat */, data, renderer, isLegendVisible);
	}

	@Override
	public void show() {
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
	public void saveAsJPG(File file, int width, int height) throws IOException {
		JFreeChartUtilities.saveChartAsJPG(file, chart, width, height);
	}

	@Override
	public void saveAsPDF(File file, int width, int height) throws IOException {
		JFreeChartUtilities.saveChartAsPDF(file, chart, width, height);
	}

	@Override
	public void saveAsSVG(File file, int width, int height) throws IOException {
		JFreeChartUtilities.saveChartAsSVG(file, chart, width, height);
	}

	@Override
	public PlotProcess2D setTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	public PlotProcess2D setXAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		return this;
	}

	@Override
	public PlotProcess2D setYAxisLabel(String yAxisLabel) {
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
