/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.finmath.plots.axis.NumberAxis;
import net.finmath.stochastic.RandomVariable;

public class PlotablePoints2D implements Plotable2D {

	private final String name;
	private final List<Point2D> series;
	private final NumberAxis domainAxis;
	private final NumberAxis rangeAxis;
	private final GraphStyle style;

	public PlotablePoints2D(final String name, final List<Point2D> series, final NumberAxis domainAxis, final NumberAxis rangeAxis,
			final GraphStyle style) {
		super();
		this.name = name;
		this.series = series;
		this.domainAxis = domainAxis;
		this.rangeAxis = rangeAxis;
		this.style = style;
	}

	public PlotablePoints2D(final String name, final List<Point2D> series, final GraphStyle style) {
		this(name, series, null, null, style);
	}

	public static PlotablePoints2D of(final String name, double[] xValues, double[] yValues, final GraphStyle style) {
		final List<Point2D> series = new ArrayList<Point2D>();
		for(int i=0; i<xValues.length; i++) {
			series.add(new Point2D(xValues[i], yValues[i]));
		}

		return new PlotablePoints2D(name, series, style);
	}

	public static PlotablePoints2D of(final String name, RandomVariable x, RandomVariable y, final GraphStyle style) {
		return of(name, x.getRealizations(), y.getRealizations(), style);
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
