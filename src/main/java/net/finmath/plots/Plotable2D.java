/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

import java.util.List;

import net.finmath.plots.axis.NumberAxis;

/**
 * A two dimensional plotable.
 * 
 * @author Christian Fries
 */
public interface Plotable2D extends Plotable {

	List<Point2D> getSeries();

	GraphStyle getStyle();

	default NumberAxis getDomainAxis() { return null; }

	default NumberAxis getRangeAxis() { return null; }
}
