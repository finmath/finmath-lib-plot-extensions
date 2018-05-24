/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.experimental;

import net.finmath.plots.experimental.jfreechart.JFreeChartUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.text.ITextRenderer;
import org.jzy3d.plot3d.text.renderers.TextBitmapRenderer;

import java.text.NumberFormat;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Small convenient wrapper for JZY3D derived from the JZY3D SurfaceDemo.
 * 
 * @author Christian Fries
 */
public class Plot2D {

	private double xmin, xmax;
	private int numberOfPointsX;
	private DoubleUnaryOperator function;

	private String title = "";
	private String xAxisLabel = "x";
	private String yAxisLabel = "y";
	private NumberFormat xAxisNumberFormat;
	private NumberFormat yAxisNumberFormat;

	private transient JFreeChart chart;

	public Plot2D(double xmin, double xmax, int numberOfPointsX, DoubleUnaryOperator function) {
		super();
		this.xmin = xmin;
		this.xmax = xmax;
		this.numberOfPointsX = numberOfPointsX;
		this.function = function;

		if(numberOfPointsX < 2) throw new IllegalArgumentException("Number of points needs to be larger than 1.");
	}

	private void init() {
		double[] xValues = new double[numberOfPointsX];
		double[] yValues = new double[numberOfPointsX];
		for(int i = 0; i<xValues.length; i++) {
			xValues[i] = xmin + i * ((xmax-xmin) / (numberOfPointsX-1));
			yValues[i] = function.applyAsDouble(xValues[i]);
		}
		chart = JFreeChartUtilities.getXYLinesPlotChart(title, xAxisLabel, "#.#" /* xAxisNumberFormat */, yAxisLabel, "#.#" /* yAxisNumberFormat */, xValues, yValues);
	}

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

	public Plot2D setTitle(String title) {
		this.title = title;
		return this;
	}

	public Plot2D setXAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		return this;
	}

	public Plot2D setYAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		return this;
	}
}
