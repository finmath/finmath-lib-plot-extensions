/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.finmath.plots.axis.NumberAxis;

/**
 * A two dimensional plotable.
 *
 * @author Christian Fries
 */
public interface Plotable2D extends Plotable {

	List<Point2D> getSeries();
	
	default List<Pair<Double,Double>> getError() { return null; }

	GraphStyle getStyle();

	default NumberAxis getDomainAxis() { return null; }

	default NumberAxis getRangeAxis() { return null; }
}
