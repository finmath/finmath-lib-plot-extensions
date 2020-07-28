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

public class PlotableFunction2D implements Plotable2D {

	private final double xmin, xmax;
	private final int numberOfPointsX;
	private final Named<DoubleUnaryOperator> namedFunction;
	private final GraphStyle style;

	public PlotableFunction2D(final double xmin, final double xmax, final int numberOfPointsX, final Named<DoubleUnaryOperator> namedFunction, final GraphStyle style) {
		super();
		this.xmin = xmin;
		this.xmax = xmax;
		this.numberOfPointsX = numberOfPointsX;
		this.namedFunction = namedFunction;
		this.style = style;

		if(numberOfPointsX < 2) {
			throw new IllegalArgumentException("Number of points needs to be larger than 1.");
		}
	}

	public PlotableFunction2D(final double xmin, final double xmax, final int numberOfPointsX, final Named<DoubleUnaryOperator> namedFunction) {
		this(xmin, xmax, numberOfPointsX, namedFunction, new GraphStyle(new Rectangle(-2, -3, 5, 5), new BasicStroke(3.0f), null));
	}

	public PlotableFunction2D(double xmin, double xmax, int numberOfPointsX, DoubleUnaryOperator doubleUnaryOperator) {
		this(xmin, xmax, numberOfPointsX, new Named<DoubleUnaryOperator>("", doubleUnaryOperator));
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
			series.add(new Point2D(x, y));
		}
		return series;
	}

	@Override
	public GraphStyle getStyle() {
		return style;
	}
}
