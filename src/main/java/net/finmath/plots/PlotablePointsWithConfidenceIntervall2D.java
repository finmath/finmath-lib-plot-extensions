/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import org.apache.commons.lang3.tuple.Pair;

import net.finmath.plots.axis.NumberAxis;
import net.finmath.stochastic.RandomVariable;

public class PlotablePointsWithConfidenceIntervall2D implements PlotableWithConfidenceInterval2D {

	private final String name;
	private final List<Point2D> series;
	private final Named<DoubleUnaryOperator> namedFunctionLowerBound;
	private final Named<DoubleUnaryOperator> namedFunctionUpperBound;
	private final NumberAxis domainAxis;
	private final NumberAxis rangeAxis;
	private final GraphStyle style;

	public PlotablePointsWithConfidenceIntervall2D(final String name, final List<Point2D> series, Named<DoubleUnaryOperator> namedFunctionLowerBound, Named<DoubleUnaryOperator> namedFunctionUpperBound, final NumberAxis domainAxis, final NumberAxis rangeAxis,
			final GraphStyle style) {
		super();
		this.name = name;
		this.series = series;
		this.namedFunctionLowerBound = namedFunctionLowerBound;
		this.namedFunctionUpperBound = namedFunctionUpperBound;
		this.domainAxis = domainAxis;
		this.rangeAxis = rangeAxis;
		this.style = style;
	}

	public PlotablePointsWithConfidenceIntervall2D(final String name, final List<Point2D> series, Named<DoubleUnaryOperator> namedFunctionLowerBound, Named<DoubleUnaryOperator> namedFunctionUpperBound, final GraphStyle style) {
		this(name, series, namedFunctionLowerBound, namedFunctionUpperBound, null, null, style);
	}

	public static PlotablePointsWithConfidenceIntervall2D of(final String name, double[] xValues, double[] yValues, Named<DoubleUnaryOperator> namedFunctionLowerBound, Named<DoubleUnaryOperator> namedFunctionUpperBound, final GraphStyle style) {
		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<xValues.length; i++) {
			series.add(new Point2D(xValues[i], yValues[i]));
		}

		return new PlotablePointsWithConfidenceIntervall2D(name, series, namedFunctionLowerBound, namedFunctionUpperBound, style);
	}

	public static PlotablePointsWithConfidenceIntervall2D of(final String name, RandomVariable x, RandomVariable y, Named<DoubleUnaryOperator> namedFunctionLowerBound, Named<DoubleUnaryOperator> namedFunctionUpperBound, final GraphStyle style) {
		return of(name, x.getRealizations(), y.getRealizations(), namedFunctionLowerBound, namedFunctionUpperBound, style);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Point2D> getSeries() {
		return series;
	}

	@Override
	public List<Pair<Double, Double>> getConfidenceIntervall() {
		final List<Pair<Double, Double>> seriesConfidenceIntervall = new ArrayList<Pair<Double, Double>>();
		final DoubleUnaryOperator lowerBound = namedFunctionLowerBound.get();
		final DoubleUnaryOperator upperBound = namedFunctionUpperBound.get();
		for(int i = 0; i<series.size(); i++) {
			final double x = series.get(i).getX();
			final double y = series.get(i).getY();
			final double l = y+lowerBound.applyAsDouble(x);
			final double u = y+upperBound.applyAsDouble(x);
			seriesConfidenceIntervall.add(Pair.of(l,u));
		}
		return seriesConfidenceIntervall;
	}

	@Override
	public NumberAxis getDomainAxis() {
		return domainAxis;
	}

	@Override
	public NumberAxis getRangeAxis() {
		return rangeAxis;
	}

	@Override
	public GraphStyle getStyle() {
		return style;
	}
}
