/*
https://java.com/de/download/mac_download.jsp * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import static org.junit.Assert.fail;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.collect.Streams;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Plots the value of an option under the Black-Scholes model as a function of strike and time-to-maturity.
 *
 * @author Christian Fries
 */
public class Plot2DBarTest {

	/**
	 * Run the demo.
	 * Plots the value of an option under the Black-Scholes model as a function of strike and time-to-maturity.
	 *
	 * @throws CalculationException Thrown if numerical calculation fails.
	 */
	@Test
	public void test() throws CalculationException {

		final double initialValue = 100;
		final double riskFreeRate = 0.04;
		final double volatility = 0.40;
		final int numberOfTimeSteps = 100;
		final double deltaT = 0.2;
		final int numberOfPaths = 100000;

		final TimeDiscretizationFromArray timeDiscretizationFromArray = new TimeDiscretizationFromArray(0.0, numberOfTimeSteps, deltaT);
		final AssetModelMonteCarloSimulationModel blackScholesMonteCarlo = new MonteCarloBlackScholesModel(timeDiscretizationFromArray, numberOfPaths, initialValue, riskFreeRate, volatility);

		final NumberFormat format = new DecimalFormat("0.00");

		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;
		final List<PlotableCategories> data = new ArrayList<PlotableCategories>();

		final double[][] histogramReference = blackScholesMonteCarlo.getAssetValue(10, 0).getHistogram(20, 4.0);

		for(double maturity= 2.0; maturity<=10.0; maturity+=2.0) {
			final double[] histogram = blackScholesMonteCarlo.getAssetValue(maturity, 0).getHistogram(histogramReference[0]);
			final BiFunction<Double, Double, Category2D> map = (x, y) -> { return new Category2D(format.format(x), y); };

			final String name = "Maturity " + maturity;
			final List<Category2D> histogramAsList = Streams.zip(Arrays.stream(histogramReference[0]).boxed(), Arrays.stream(histogram).boxed(), map).collect(Collectors.toList());
			final PlotableCategories histo = new PlotableCategories() {

				@Override
				public String getName() {
					return name;
				}

				@Override
				public GraphStyle getStyle() {
					return null;
				}

				@Override
				public List<Category2D> getSeries() {
					return histogramAsList;
				}
			};

			min = Math.min(min, Arrays.stream(histogram).min().getAsDouble());
			max = Math.max(max, Arrays.stream(histogram).max().getAsDouble());
			data.add(histo);
		}

		final Plot2DBarFX plot = new Plot2DBarFX(data, "Histogram", "value", "frequency", NumberFormat.getPercentInstance(), min, max, (max-min) / 10.0, false);
		try {
			plot.show();
			Thread.sleep(10000);
		} catch (final Exception e) {
			fail("Failing with exception " + e.getMessage());
		}

		// To save as PDF uncomment the following line
		// plot.saveAsPDF(new File("Black-Scholes Model Path.pdf"), 800, 400);
	}
}
