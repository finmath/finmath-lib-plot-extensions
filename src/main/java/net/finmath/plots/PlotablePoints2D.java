/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

import java.util.List;

public class PlotablePoints2D implements Plotable2D {

	private String name;
	private List<Point2D> series;
	private NumberAxis domainAxis;
	private NumberAxis rangeAxis;
	private GraphStyle style;
	
	public PlotablePoints2D(String name, List<Point2D> series, NumberAxis domainAxis, NumberAxis rangeAxis,
			GraphStyle style) {
		super();
		this.name = name;
		this.series = series;
		this.domainAxis = domainAxis;
		this.rangeAxis = rangeAxis;
		this.style = style;
	}

	public PlotablePoints2D(String name, List<Point2D> series, GraphStyle style) {
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
