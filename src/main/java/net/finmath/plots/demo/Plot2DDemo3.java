/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.demo;

import java.awt.Rectangle;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

import net.finmath.marketdata.model.curves.CurveInterface;
import net.finmath.marketdata.model.curves.locallinearregression.CurveEstimation;
import net.finmath.plots.GraphStyle;
import net.finmath.plots.Named;
import net.finmath.plots.Plot2D;
import net.finmath.plots.Plotable2D;
import net.finmath.plots.PlotableFunction2D;
import net.finmath.plots.PlotablePoints2D;
import net.finmath.plots.Point2D;

/**
 * Plots the regression estimation of a curve.
 *
 * @author Christian Fries
 */
public class Plot2DDemo3 {

	/**
	 * Run the demo.
	 *
	 * @param args Not used.
	 * @throws Exception Exception from the graphics backend.
	 */
	public static void main(String[] args) throws Exception {

		LocalDate date = LocalDate.now();

		Plot2D plot = new Plot2D(new ArrayList<Plotable2D>());
		plot
		.setXAxisLabel("time")
		.setYAxisLabel("value")
		.setIsLegendVisible(true)
		.show();

		for(int bandwidthIndex=0; bandwidthIndex<20; bandwidthIndex++) {
			double bandwidth = 10.0+bandwidthIndex*5;

			Random random = new Random(3141);
			int numberOfSamplePoints = 100;
			double[] xValues = new double[numberOfSamplePoints];
			double[] yValues = new double[numberOfSamplePoints];
			for(int i=0; i<numberOfSamplePoints; i++) {
				xValues[i] = random.nextDouble() * 360.0;
				yValues[i] = 10.0*Math.sin(Math.PI * xValues[i]/180.0) + 10.0*(random.nextDouble()-0.5);
			}


			CurveEstimation estimatedcurve = new CurveEstimation(date, bandwidth, xValues, yValues, xValues.clone(), 0.0);
			CurveInterface regressionCurve = estimatedcurve.getRegressionCurve();

			DoubleUnaryOperator function = (x) -> {
				return regressionCurve.getValue(x);
			};

			List<Point2D> series = new ArrayList<Point2D>();
			for(int i=0; i<xValues.length; i++) {
				series.add(new Point2D(xValues[i],yValues[i]));
			}

			List<Plotable2D> plotables = Arrays.asList(
					new PlotableFunction2D(0.0, 360.0, 1000, new Named<DoubleUnaryOperator>("Regression Curve", function), null),
					new PlotablePoints2D("Values", series, new GraphStyle(new Rectangle(1, 1), null, null))
					);

			plot.update(plotables)
			.setTitle("Local Linear Regression (bandwidth = " + bandwidth + ")");

			Thread.sleep(500);
		}

		// plot.saveAsPDF(new File("LocalLinearRegression-" + bandwidth + ".pdf"), 800, 600);
	}
}
