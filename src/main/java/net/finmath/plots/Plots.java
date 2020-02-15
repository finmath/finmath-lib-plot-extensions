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

import net.finmath.plots.axis.NumberAxis;
import net.finmath.stochastic.RandomVariable;

/**
 * A factory for various plots.
 *
 * @author Christian Fries
 */
public class Plots {

	public static Plot createPlotOfHistogramBehindValues(final RandomVariable randomVariableX, final RandomVariable randomVariableY, final int numberOfPoints, final double standardDeviations) {
		return createPlotOfHistogramBehindValues(randomVariableX, randomVariableY, numberOfPoints, standardDeviations, null, null);
	}

	public static Plot createPlotOfHistogramBehindValues(final RandomVariable randomVariableX, final RandomVariable randomVariableY, final int numberOfPoints, final double standardDeviations, final Double xmin, final Double xmax) {

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

	public static Plot2D createPlotOfHistogram(final RandomVariable randomVariable, final int numberOfPoints, final double standardDeviations) {

		final double[][] histogram = randomVariable.getHistogram(numberOfPoints, standardDeviations);

		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<histogram[0].length; i++) {
			series.add(new Point2D(histogram[0][i],histogram[1][i]));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Histogram", series, new GraphStyle(new Rectangle(10, 2), null, Color.DARK_GRAY, Color.LIGHT_GRAY))
				);

		return new Plot2D(plotables);
	}


	public static Plot2D updatePlotOfHistogram(final Plot2D historgram, final RandomVariable randomVariable, final int numberOfPoints, final double standardDeviations) {

		final double[][] histogram = randomVariable.getHistogram(numberOfPoints, standardDeviations);

		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<histogram[0].length; i++) {
			series.add(new Point2D(histogram[0][i],histogram[1][i]));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Histogram", series, new GraphStyle(new Rectangle(10, 2), null, Color.DARK_GRAY, Color.LIGHT_GRAY))
				);

		historgram.update(plotables);

		return historgram;
	}

	public static Plot2D createPlotScatter(final RandomVariable x, final RandomVariable y, final double xmin, final double xmax) {

		final double[] xValues = x.getRealizations();
		final double[] yValues = y.getRealizations();

		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<xValues.length; i++) {
			series.add(new Point2D(xValues[i], yValues[i]));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Scatter", series, new GraphStyle(new Rectangle(1, 1), null, null))
				);

		return new Plot2D(plotables);
	}


}
