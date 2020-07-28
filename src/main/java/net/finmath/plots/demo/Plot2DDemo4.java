/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.demo;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import net.finmath.plots.Named;
import net.finmath.plots.Plot;
import net.finmath.plots.Plot2D;

/**
 * Plots the value of a finite difference approximation of the derivative of (exp(x)-x) at 0.
 *
 * @author Christian Fries
 */
public class Plot2DDemo4 {

	private static final int numberOfPointsToPlot = 200;
	private static final boolean isSavePlotsToFile = true;

	/**
	 * Run the demo.
	 * Plots the value of a finite difference approximation of the derivative of (exp(x)-x) at 0.
	 *
	 * @param args Not used.
	 * @throws Exception Exception from the graphics backend.
	 */
	public static void main(final String[] args) throws Exception {

		final double x			= 0.0;

		final DoubleUnaryOperator functionDerivativeCentralError = (shiftScale) -> {
			final double shiftSize = Math.pow(10, -shiftScale);	// shift = 10^{-scale}

			final double valueUp	= Math.exp(x + shiftSize);
			final double valueDown	= Math.exp(x - shiftSize);

			final double derivativeFiniteDifference = (valueUp-valueDown) / 2 / shiftSize - 1;

			return derivativeFiniteDifference;
		};

		final DoubleUnaryOperator functionDerivativeForwardError = (shiftScale) -> {
			final double shiftSize = Math.pow(10, -shiftScale);	// shift = 10^{-scale}

			final double valueUp	= Math.exp(x + shiftSize);
			final double value		= Math.exp(x);

			final double derivativeFiniteDifference = (valueUp-value) / shiftSize - 1;

			return derivativeFiniteDifference;
		};

		final DoubleUnaryOperator functionDerivativeAnalytic = (shiftScale) -> {

			return Math.exp(x) - 1;
		};

		final Plot plotDerivativeForward = new Plot2D(1, 15.0, numberOfPointsToPlot, List.of(
				new Named<DoubleUnaryOperator>("Finite Difference Approximation", functionDerivativeForwardError),
				new Named<DoubleUnaryOperator>("Analytic", functionDerivativeAnalytic)));
		plotDerivativeForward.setTitle("(One Sided Finite Difference) Derivative of exp(x) at x = " + x)
		.setXAxisLabel("scale (h = 10^{-scale})")
		.setYAxisLabel("error (approx. value - 1.0)")
		.setIsLegendVisible(true);
		((Plot2D)plotDerivativeForward).setYAxisNumberFormat(new DecimalFormat("0.0E00"));
		plotDerivativeForward.show();
		if(isSavePlotsToFile) plotDerivativeForward.saveAsPDF(new File("exp-x-forward-fd-large.pdf"), 900, 600);

		final Plot plotDerivativeForward3 = new Plot2D(14.0, 16.5, numberOfPointsToPlot, List.of(
				new Named<DoubleUnaryOperator>("Finite Difference Approximation", functionDerivativeForwardError),
				new Named<DoubleUnaryOperator>("Analytic", functionDerivativeAnalytic)));
		plotDerivativeForward3.setTitle("(One Sided Finite Difference) Derivative of exp(x) at x = " + x)
		.setXAxisLabel("scale (h = 10^{-scale})")
		.setYAxisLabel("error (approx. value - 1.0)")
		.setIsLegendVisible(true);
		((Plot2D)plotDerivativeForward3).setYAxisNumberFormat(new DecimalFormat("0.0E00"));
		plotDerivativeForward3.show();
		if(isSavePlotsToFile) plotDerivativeForward3.saveAsPDF(new File("exp-x-forward-fd-corner.pdf"), 900, 600);

		final Plot plotDerivativeForward2 = new Plot2D(6, 10.0, numberOfPointsToPlot, List.of(
				new Named<DoubleUnaryOperator>("Finite Difference Approximation", functionDerivativeForwardError),
				new Named<DoubleUnaryOperator>("Analytic", functionDerivativeAnalytic)));
		plotDerivativeForward2
		.setTitle("(One Sided Finite Difference) Derivative of exp(x) at x = " + x)
		.setXAxisLabel("scale (h = 10^{-scale})")
		.setYAxisLabel("error (approx. value - 1.0)")
		.setIsLegendVisible(true);
		((Plot2D)plotDerivativeForward2).setYAxisNumberFormat(new DecimalFormat("0.0E00"));
		plotDerivativeForward2.show();
		if(isSavePlotsToFile) plotDerivativeForward2.saveAsPDF(new File("exp-x-forward-fd-zoom.pdf"), 900, 600);

		final Plot plotDerivativeCentral1 = new Plot2D(1.0, 20.0, numberOfPointsToPlot, List.of(
				new Named<DoubleUnaryOperator>("Finite Difference Approximation", functionDerivativeCentralError),
				new Named<DoubleUnaryOperator>("Analytic", functionDerivativeAnalytic)));
		plotDerivativeCentral1
		.setTitle("(Central Finite Difference) Derivative of exp(x) at x = " + x)
		.setXAxisLabel("scale (h = 10^{-scale})")
		.setYAxisLabel("error (approx. value - 1.0)")
		.setIsLegendVisible(true);
		((Plot2D)plotDerivativeCentral1).setYAxisNumberFormat(new DecimalFormat("0.0E00"));
		plotDerivativeCentral1.show();
		if(isSavePlotsToFile) plotDerivativeCentral1.saveAsPDF(new File("exp-x-central-fd-large.pdf"), 900, 600);

		final Plot plotDerivativeCentral2 = new Plot2D(4, 7, numberOfPointsToPlot, List.of(
				new Named<DoubleUnaryOperator>("Finite Difference Approximation", functionDerivativeCentralError),
				new Named<DoubleUnaryOperator>("Analytic", functionDerivativeAnalytic)));
		plotDerivativeCentral2
		.setTitle("(Central Finite Difference) Derivative of exp(x) at x = " + x)
		.setXAxisLabel("scale (h = 10^{-scale})")
		.setYAxisLabel("error (approx. value - 1.0)")
		.setIsLegendVisible(true);
		((Plot2D)plotDerivativeCentral2).setYAxisNumberFormat(new DecimalFormat("0.0E00"));
		((Plot2D)plotDerivativeCentral2).setYRange(-5E-10, 5E-10);
		plotDerivativeCentral2.show();
		if(isSavePlotsToFile) plotDerivativeCentral2.saveAsPDF(new File("exp-x-central-fd-zoom.pdf"), 900, 600);
	}
}
