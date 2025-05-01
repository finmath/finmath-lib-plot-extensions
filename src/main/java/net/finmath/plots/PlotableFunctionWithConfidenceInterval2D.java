/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import org.apache.commons.lang3.tuple.Pair;

import net.finmath.plots.axis.NumberAxis;

public class PlotableFunctionWithConfidenceInterval2D implements PlotableWithConfidenceInterval2D {

	private final double xmin, xmax;
	private final int numberOfPointsX;
	private final Named<DoubleUnaryOperator> namedFunction;
	private final Named<DoubleUnaryOperator> namedFunctionLowerBound;
	private final Named<DoubleUnaryOperator> namedFunctionUpperBound;
	private final GraphStyle style;
	private final NumberAxis domainAxis;
	private final NumberAxis rangeAxis;

	public PlotableFunctionWithConfidenceInterval2D(final double xmin, final double xmax, final int numberOfPointsX, final Named<DoubleUnaryOperator> namedFunction, final Named<DoubleUnaryOperator> namedFunctionLowerBound, final Named<DoubleUnaryOperator> namedFunctionUpperBound, NumberAxis domainAxis, NumberAxis rangeAxis, GraphStyle style) {
		super();
		this.xmin = xmin;
		this.xmax = xmax;
		this.numberOfPointsX = numberOfPointsX;
		this.namedFunction = namedFunction;
		this.namedFunctionLowerBound = namedFunctionLowerBound;
		this.namedFunctionUpperBound = namedFunctionUpperBound;
		this.domainAxis = domainAxis;
		this.rangeAxis = rangeAxis;
		this.style = style;

		if(numberOfPointsX < 2) {
			throw new IllegalArgumentException("Number of points needs to be larger than 1.");
		}
	}

	public PlotableFunctionWithConfidenceInterval2D(final double xmin, final double xmax, final int numberOfPointsX, final Named<DoubleUnaryOperator> namedFunction, final Named<DoubleUnaryOperator> namedFunctionLowerBound, final Named<DoubleUnaryOperator> namedFunctionUpperBound, final GraphStyle style) {
		this(xmin, xmax, numberOfPointsX, namedFunction, namedFunctionLowerBound, namedFunctionUpperBound, null, null, style);
	}

	public PlotableFunctionWithConfidenceInterval2D(final double xmin, final double xmax, final int numberOfPointsX, final Named<DoubleUnaryOperator> namedFunction, final Named<DoubleUnaryOperator> namedFunctionLowerBound, final Named<DoubleUnaryOperator> namedFunctionUpperBound) {
		this(xmin, xmax, numberOfPointsX, namedFunction, namedFunctionLowerBound, namedFunctionUpperBound, new GraphStyle(new Rectangle(-2, -3, 5, 5), new BasicStroke(3.0f), null));
	}

	public PlotableFunctionWithConfidenceInterval2D(double xmin, double xmax, int numberOfPointsX, DoubleUnaryOperator doubleUnaryOperator, DoubleUnaryOperator doubleUnaryOperatorLowerBound, DoubleUnaryOperator doubleUnaryOperatorUpperBound) {
		this(xmin, xmax, numberOfPointsX, new Named<DoubleUnaryOperator>("", doubleUnaryOperator), new Named<DoubleUnaryOperator>("", doubleUnaryOperatorLowerBound), new Named<DoubleUnaryOperator>("", doubleUnaryOperatorUpperBound));
	}

	@Override
	public String getName() {
		return namedFunction.getName();
	}

	@Override
	public List<Point2D> getSeries() {
		final List<Point2D> series = new ArrayList<Point2D>();
		final DoubleUnaryOperator function = namedFunction.get();
		for(int i = 0; i<numberOfPointsX; i++) {
			final double x = xmin + i * ((xmax-xmin) / (numberOfPointsX-1));
			final double y = function.applyAsDouble(x);
			// The point is ignored if NaN or Infinity
			if(Double.isFinite(y)) series.add(new Point2D(x, y));
		}
		return series;
	}

	@Override
	public List<Pair<Double, Double>> getConfidenceIntervall() {
		final List<Pair<Double, Double>> series = new ArrayList<Pair<Double, Double>>();
		final DoubleUnaryOperator lowerBound = namedFunctionLowerBound.get();
		final DoubleUnaryOperator upperBound = namedFunctionUpperBound.get();
		for(int i = 0; i<numberOfPointsX; i++) {
			final double x = xmin + i * ((xmax-xmin) / (numberOfPointsX-1));
			final double l = lowerBound.applyAsDouble(x);
			final double u = upperBound.applyAsDouble(x);
			// The point is ignored if NaN or Infinity
			if(Double.isFinite(l) && Double.isFinite(u)) series.add(Pair.of(l,u));
		}
		return series;
	}

	@Override
	public GraphStyle getStyle() {
		return style;
	}

	@Override
	public NumberAxis getDomainAxis() {
		return domainAxis;
	}

	@Override
	public NumberAxis getRangeAxis() {
		return rangeAxis;
	}
}
