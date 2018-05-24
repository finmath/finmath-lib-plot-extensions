/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 24 May 2018
 */

package net.finmath.plots;

/**
 * @author Christian Fries
 *
 */
public interface Plot {

	void show() throws Exception;

	Plot setTitle(String title);

	Plot setXAxisLabel(String xAxisLabel);

	Plot setYAxisLabel(String yAxisLabel);

	Plot setZAxisLabel(String zAxisLabel);

}