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
	private GraphStyle style;
	
	public PlotablePoints2D(String name, List<Point2D> series, GraphStyle style) {
		super();
		this.name = name;
		this.series = series;
		this.style = style;
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
	public GraphStyle getStyle() {
		return style;
	}

}
