/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.demo;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

import net.finmath.plots.GraphStyle;
import net.finmath.plots.Named;
import net.finmath.plots.Plot2D;
import net.finmath.plots.Plotable2D;
import net.finmath.plots.PlotableFunction2D;
import net.finmath.plots.PlotablePoints2D;
import net.finmath.plots.Point2D;

/**
 * Plots a function and two scatters
 *
 * @author Christian Fries
 */
public class Plot2DDemo8 {

	/**
	 * Run the demo.
	 *
	 * @param args Not used.
	 * @throws Exception Exception from the graphics backend.
	 */
	public static void main(final String[] args) throws Exception {

		final Random random = new Random(3141);
		final int numberOfSamplePoints = 100;

		final List<Point2D> series1 = new ArrayList<Point2D>();
		final List<Point2D> series2 = new ArrayList<Point2D>();
		for(int i=0; i<numberOfSamplePoints; i++) {
			double x = 2*random.nextDouble()-1.0;
			double y = x*x;
			double e1 = 1*random.nextDouble()-0.5;
			double e2 = 2*random.nextDouble()-1.0;
			series1.add(new Point2D(x, y + e1));
			series2.add(new Point2D(x, y + e2));
		}

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotableFunction2D(-1, 1, 1000, new Named<DoubleUnaryOperator>("True Function", x -> x*x), new GraphStyle(new Rectangle(2, 2), null, Color.GREEN)),
				new PlotablePoints2D("Values 1", series1, new GraphStyle(new Rectangle(3, 3), null, Color.BLUE)),
				new PlotablePoints2D("Values 2", series2, new GraphStyle(new Rectangle(3, 3), null, Color.RED))
				);

		final Plot2D plot = new Plot2D(plotables);

		plot
		.setTitle("Two Scatters and a Function")
		.setXAxisLabel("x")
		.setYAxisLabel("y")
		.setIsLegendVisible(true)
		.show();

		// plot.saveAsPDF(new File("Two scatters and a Function.pdf"), 800, 600);
	}
}
