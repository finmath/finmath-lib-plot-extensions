/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 24 May 2018
 */

package net.finmath.plots;

import java.io.File;
import java.io.IOException;

/**
 * @author Christian Fries
 */
public interface Plot {

	void show() throws Exception;

	void close() throws Exception;

	Plot saveAsJPG(File file, int width, int height) throws IOException;

	Plot saveAsPDF(File file, int width, int height) throws IOException;

	Plot saveAsSVG(File file, int width, int height) throws IOException;

	Plot setTitle(String title);

	Plot setXAxisLabel(String xAxisLabel);

	Plot setYAxisLabel(String yAxisLabel);

	Plot setZAxisLabel(String zAxisLabel);

	Plot setIsLegendVisible(Boolean isLegendVisible);
}
