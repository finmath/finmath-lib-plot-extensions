/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

import java.util.List;

import net.finmath.plots.axis.NumberAxis;

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
