/**
 *
 */
package net.finmath.plots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.plots.axis.NumberAxis;
import net.finmath.stochastic.RandomVariable;

/**
 * Static factory methods for various plots (used to keep demos short).
 *
 * @author Christian Fries
 */
public class Plots {

	/*
	 * Scatter
	 */

	/**
	 * Create a scatter plot.
	 * 
	 * @param x List of x-values.
	 * @param mapOfValues A map that maps a name to a list of y-values (which will be plotted (x,y) under that name.
	 * @param xmin The min of the x-axis.
	 * @param xmax The max of the x-axis.
	 * @param dotSize The dot size.
	 * @return The plot.
	 */
	public static Plot2D createScatter(final List<Double> x, final Map<String, List<Double>> mapOfValues, final double xmin, final double xmax, final int dotSize) {

		final List<Plotable2D> plotables = new ArrayList<Plotable2D>();

		for(Map.Entry<String, List<Double>> values : mapOfValues.entrySet()) {
			final List<Point2D> series = new ArrayList<Point2D>();
			final List<Double> valueDoubles = values.getValue();
			for(int i=0; i<valueDoubles.size(); i++) {
				series.add(new Point2D(x.get(i), valueDoubles.get(i)));
			}
			plotables.add(new PlotablePoints2D(values.getKey(), series, new GraphStyle(new Rectangle(dotSize, dotSize), null, null)));
		}

		return new Plot2D(plotables);
	}

	public static Plot createScatter(List<double[]> points, Double xmin, Double xmax, Double ymin, Double ymax, int dotSize) {
		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Scatter", points.stream().map(point -> new Point2D(point[0], point[1])).collect(Collectors.toList()),
						xmin != null && xmax != null ? new NumberAxis(null, xmin, xmax, null) : null,
						ymin != null && ymax != null ? new NumberAxis(null, ymin, ymax, null) : null,
						new GraphStyle(new Rectangle(dotSize, dotSize), null, null))
				);

		return new Plot2D(plotables);
	}

	public static Plot2D createScatter(final double[] xValues, double[] yValues, final double xmin, final double xmax, final int dotSize) {

		final List<Point2D> points = new ArrayList<Point2D>();
		for(int i=0; i<xValues.length; i++) {
			points.add(new Point2D(xValues[i], yValues[i]));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Scatter", points, new GraphStyle(new Rectangle(dotSize, dotSize), null, null))
				);

		return new Plot2D(plotables);
	}

	public static Plot2D createScatter(final List<Double> x, final List<Double> y, final double xmin, final double xmax, final int dotSize) {
		return createScatter(
				ArrayUtils.toPrimitive(x.toArray(new Double[x.size()])),
				ArrayUtils.toPrimitive(y.toArray(new Double[y.size()])),
				xmin, xmax, dotSize);
	}

	public static Plot2D updateScatter(final Plot2D plot, final double[] xValues, double[] yValues, final double xmin, final double xmax, final int dotSize) {
		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<xValues.length; i++) {
			series.add(new Point2D(xValues[i], yValues[i]));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Scatter", series, new GraphStyle(new Rectangle(dotSize, dotSize), null, null))
				);

		return plot.update(plotables);
	}

	public static Plot2D updatePlotScatter(final Plot2D plot, final List<Double> x, final List<Double> y, final double xmin, final double xmax, final int dotSize) {
		return updateScatter(
				plot,
				ArrayUtils.toPrimitive(x.toArray(new Double[x.size()])),
				ArrayUtils.toPrimitive(y.toArray(new Double[y.size()])),
				xmin, xmax, dotSize);
	}

	public static Plot2D createScatter(final RandomVariable x, final RandomVariable y, final double xmin, final double xmax, final int dotSize) {
		return createScatter(x.getRealizations(), y.getRealizations(), xmin, xmax, dotSize);
	}

	public static Plot2D createScatter(final RandomVariable x, final RandomVariable y, final double xmin, final double xmax) {

		return createScatter(x, y, xmin, xmax, 1);
	}

	public static Plot2D createScatter(final RandomVariable x, final RandomVariable y) {
		return createScatter(x, y, x.getMin(), x.getMax());
	}

	/*
	 *
	 * Histograms
	 */

	public static Plot2D createHistogram(final RandomVariable randomVariable, final int numberOfPoints, final double standardDeviations) {

		final double[][] histogram = randomVariable.getHistogram(numberOfPoints, standardDeviations);

		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<histogram[0].length; i++) {
			series.add(new Point2D(histogram[0][i],histogram[1][i]));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Histogram", series, new GraphStyle(new Rectangle(10, 2), null, Color.DARK_GRAY, Color.LIGHT_GRAY))
				);

		Plot2D plot = new Plot2D(plotables);
		plot.setXAxisLabel("values");
		plot.setYAxisLabel("frequency");

		return plot;
	}

	public static Plot2D createHistogram(final List<Double> values, final int numberOfPoints, final double standardDeviations) {

		RandomVariable randomVariable = new RandomVariableFromDoubleArray(0.0, ArrayUtils.toPrimitive(values.toArray(new Double[values.size()])));

		return createHistogram(randomVariable, numberOfPoints, standardDeviations);
	}

	public static Plot2D updateHistogram(final Plot2D histogram, final RandomVariable randomVariable, final int numberOfPoints, final double standardDeviations) {

		final double[][] histogramValues = randomVariable.getHistogram(numberOfPoints, standardDeviations);

		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<histogramValues[0].length; i++) {
			series.add(new Point2D(histogramValues[0][i],histogramValues[1][i]));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Histogram", series, new GraphStyle(new Rectangle(10, 2), null, Color.DARK_GRAY, Color.LIGHT_GRAY))
				);

		histogram.update(plotables);

		return histogram;
	}

	public static Plot2D updateHistogram(final Plot2D histogram, final List<Double> values, final int numberOfPoints, final double standardDeviations) {
		RandomVariable randomVariable = new RandomVariableFromDoubleArray(0.0, ArrayUtils.toPrimitive(values.toArray(new Double[values.size()])));

		return updateHistogram(histogram, randomVariable, numberOfPoints, standardDeviations);
	}

	/**
	 * Create a histogram behind a value scatter plot.
	 *
	 * @param randomVariableX The random variable for the independent.
	 * @param randomVariableY The random variable for the dependent.
	 * @param numberOfPoints The number of bins to be used for the histogram.
	 * @param standardDeviations The standard deviations to be covered by the independent.
	 * @return The plot.
	 */
	public static Plot createHistogramBehindValues(final RandomVariable randomVariableX, final RandomVariable randomVariableY, final int numberOfPoints, final double standardDeviations) {
		return createHistogramBehindValues(randomVariableX, randomVariableY, numberOfPoints, standardDeviations, null, null);
	}

	public static Plot createHistogramBehindValues(final RandomVariable randomVariableX, final RandomVariable randomVariableY, final int numberOfPoints, final double standardDeviations, final Double xmin, final Double xmax) {

		/*
		 * Create historgram
		 */
		final double[][] histogram = randomVariableX.getHistogram(numberOfPoints, standardDeviations);
		final List<Point2D> seriesForHistogram = new ArrayList<Point2D>();
		for(int i=0; i<histogram[0].length; i++) {
			seriesForHistogram.add(new Point2D(histogram[0][i],histogram[1][i]));
		}

		/*
		 * Create scatter
		 */
		final List<Point2D> seriesForScatter = new ArrayList<Point2D>();
		for(int i=0; i<randomVariableX.size(); i++) {
			seriesForScatter.add(new Point2D(randomVariableX.get(i), randomVariableY.get(i)));
		}

		/*
		 * Create axis
		 *
		 * The scatter and the histogram should be displaced on different axis.
		 * This will be the case if we pass different Axis objects.
		 */
		final NumberAxis domainAxis = new NumberAxis("underlying value", xmin, xmax);
		final NumberAxis rangeAxisHistogram = new NumberAxis("frequency", null, null);
		final NumberAxis rangeAxisScatter = new NumberAxis("value", null, null);

		/*
		 * Create plot
		 */
		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Scatter", seriesForScatter, domainAxis, rangeAxisScatter, new GraphStyle(new Rectangle(new Point(-2,-2), new Dimension(4,4)), null, Color.RED)),
				new PlotablePoints2D("Histogram", seriesForHistogram, domainAxis, rangeAxisHistogram, new GraphStyle(new Rectangle(2, 2), null, Color.DARK_GRAY, Color.LIGHT_GRAY))
				);

		return new Plot2D(plotables);
	}

	/*
	 *
	 * Densities
	 */

	public static Plot2D createDensity(final RandomVariable randomVariable, final int numberOfPoints, final double standardDeviations) {

		final double[][] histogram = randomVariable.getHistogram(numberOfPoints, standardDeviations);

		double length = histogram[0][histogram[0].length-1] - histogram[0][0];
		
		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<histogram[0].length; i++) {
			series.add(new Point2D(histogram[0][i],histogram[1][i]*numberOfPoints/length));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Histogram", series, new GraphStyle(new Rectangle(10, 2), null, Color.DARK_GRAY, Color.LIGHT_GRAY))
				);

		Plot2D plot = new Plot2D(plotables);
		plot.setXAxisLabel("values");
		plot.setYAxisLabel("relative frequency");

		return plot;
	}

	public static Plot2D createDensity(final List<Double> values, final int numberOfPoints, final double standardDeviations) {

		RandomVariable randomVariable = new RandomVariableFromDoubleArray(0.0, ArrayUtils.toPrimitive(values.toArray(new Double[values.size()])));

		return createDensity(randomVariable, numberOfPoints, standardDeviations);
	}

	public static Plot2D updateDensity(final Plot2D histogram, final RandomVariable randomVariable, final int numberOfPoints, final double standardDeviations) {

		final double[][] histogramValues = randomVariable.getHistogram(numberOfPoints, standardDeviations);

		double length = histogramValues[0][histogramValues[0].length-1] - histogramValues[0][0];

		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<histogramValues[0].length; i++) {
			series.add(new Point2D(histogramValues[0][i],histogramValues[1][i]*numberOfPoints/length));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Histogram", series, new GraphStyle(new Rectangle(10, 2), null, Color.DARK_GRAY, Color.LIGHT_GRAY))
				);

		histogram.update(plotables);

		return histogram;
	}

	public static Plot2D updateDensity(final Plot2D histogram, final List<Double> values, final int numberOfPoints, final double standardDeviations) {
		RandomVariable randomVariable = new RandomVariableFromDoubleArray(0.0, ArrayUtils.toPrimitive(values.toArray(new Double[values.size()])));

		return updateDensity(histogram, randomVariable, numberOfPoints, standardDeviations);
	}

	/**
	 * Create a histogram behind a value scatter plot.
	 *
	 * @param randomVariableX The random variable for the independent.
	 * @param randomVariableY The random variable for the dependent.
	 * @param numberOfPoints The number of bins to be used for the histogram.
	 * @param standardDeviations The standard deviations to be covered by the independent.
	 * @return The plot.
	 */
	public static Plot createDensityBehindValues(final RandomVariable randomVariableX, final RandomVariable randomVariableY, final int numberOfPoints, final double standardDeviations) {
		return createDensityBehindValues(randomVariableX, randomVariableY, numberOfPoints, standardDeviations, null, null);
	}

	public static Plot createDensityBehindValues(final RandomVariable randomVariableX, final RandomVariable randomVariableY, final int numberOfPoints, final double standardDeviations, final Double xmin, final Double xmax) {

		/*
		 * Create historgram
		 */
		final double[][] histogram = randomVariableX.getHistogram(numberOfPoints, standardDeviations);
		double length = histogram[0][histogram[0].length-1] - histogram[0][0];
		final List<Point2D> seriesForHistogram = new ArrayList<Point2D>();
		for(int i=0; i<histogram[0].length; i++) {
			seriesForHistogram.add(new Point2D(histogram[0][i],histogram[1][i]*numberOfPoints/length));
		}

		/*
		 * Create scatter
		 */
		final List<Point2D> seriesForScatter = new ArrayList<Point2D>();
		for(int i=0; i<randomVariableX.size(); i++) {
			seriesForScatter.add(new Point2D(randomVariableX.get(i), randomVariableY.get(i)));
		}

		/*
		 * Create axis
		 *
		 * The scatter and the histogram should be displaced on different axis.
		 * This will be the case if we pass different Axis objects.
		 */
		final NumberAxis domainAxis = new NumberAxis("underlying value", xmin, xmax);
		final NumberAxis rangeAxisHistogram = new NumberAxis("frequency", null, null);
		final NumberAxis rangeAxisScatter = new NumberAxis("value", null, null);

		/*
		 * Create plot
		 */
		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Scatter", seriesForScatter, domainAxis, rangeAxisScatter, new GraphStyle(new Rectangle(new Point(-2,-2), new Dimension(4,4)), null, Color.RED)),
				new PlotablePoints2D("Histogram", seriesForHistogram, domainAxis, rangeAxisHistogram, new GraphStyle(new Rectangle(2, 2), null, Color.DARK_GRAY, Color.LIGHT_GRAY))
				);

		return new Plot2D(plotables);
	}

	/*
	 * Backward compatiblity
	 */

	@Deprecated(forRemoval = true)
	public static Plot2D createPlotOfHistogram(final RandomVariable randomVariable, final int numberOfPoints, final double standardDeviations) {
		return createHistogram(randomVariable, numberOfPoints, standardDeviations);
	}

	@Deprecated(forRemoval = true)
	public static Plot2D updatePlotOfHistogram(final Plot2D histogram, final RandomVariable randomVariable, final int numberOfPoints, final double standardDeviations) {
		return updateHistogram(histogram, randomVariable, numberOfPoints, standardDeviations);
	}

	@Deprecated(forRemoval = true)
	public static Plot createPlotOfHistogramBehindValues(final RandomVariable randomVariableX, final RandomVariable randomVariableY, final int numberOfPoints, final double standardDeviations) {
		return createHistogramBehindValues(randomVariableX, randomVariableY, numberOfPoints, standardDeviations);
	}

	@Deprecated(forRemoval = true)
	public static Plot createPlotOfHistogramBehindValues(final RandomVariable randomVariableX, final RandomVariable randomVariableY, final int numberOfPoints, final double standardDeviations, final Double xmin, final Double xmax) {
		return createHistogramBehindValues(randomVariableX, randomVariableY, numberOfPoints, standardDeviations, xmin, xmax);
	}

	@Deprecated(forRemoval = true)
	public static Plot2D createPlotScatter(final double[] xValues, double[] yValues, final double xmin, final double xmax, final int dotSize) {
		return createScatter(xValues, yValues, xmin, xmax, dotSize);
	}

	@Deprecated(forRemoval = true)
	public static Plot2D createPlotScatter(final List<Double> x, final List<Double> y, final double xmin, final double xmax, final int dotSize) {
		return createScatter(x, y, xmin, xmax, dotSize);
	}

	@Deprecated(forRemoval = true)
	public static Plot2D updatePlotScatter(final Plot2D plot, final double[] xValues, double[] yValues, final double xmin, final double xmax, final int dotSize) {
		return updateScatter(plot, xValues, yValues, xmin, xmax, dotSize);
	}

	@Deprecated(forRemoval = true)
	public static Plot2D createPlotScatter(final RandomVariable x, final RandomVariable y, final double xmin, final double xmax, final int dotSize) {
		return createScatter(x, y, xmin, xmax, dotSize);
	}

	@Deprecated(forRemoval = true)
	public static Plot2D createPlotScatter(final RandomVariable x, final RandomVariable y, final double xmin, final double xmax) {
		return createScatter(x, y, xmin, xmax);
	}
}
