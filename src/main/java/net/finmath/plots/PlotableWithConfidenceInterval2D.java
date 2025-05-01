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
 * A plotable extended with a set of intervalls.
 *
 * @author Christian Fries
 */
public interface PlotableWithConfidenceInterval2D extends Plotable2D {

	List<Pair<Double,Double>> getConfidenceIntervall();
}
