package net.finmath.plots;

import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.BrownianMotionLazyInit;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.plots.axis.NumberAxis;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretizationFromArray;

public class Plot2DAxisTest {

	@Test
	public void test() throws CalculationException, InterruptedException {


		final double modelInitialValue = 100.0;
		final double modelRiskFreeRate = 0.05;
		final double modelVolatility = 0.20;

		final double maturity = 3.0;
		final double strike = 106.0;

		// Create a model
		final var model = new BlackScholesModel(modelInitialValue, modelRiskFreeRate, modelVolatility);

		// Create a corresponding MC process
		final var td = new TimeDiscretizationFromArray(0.0, 300, 0.01);
		final var brownianMotion = new BrownianMotionLazyInit(td, 1, 10000, 3231);
		final var process = new EulerSchemeFromProcessModel(brownianMotion);

		// Using the process (Euler scheme), create an MC simulation of a Black-Scholes model
		final var simulation = new MonteCarloAssetModel(model, process);

		final EuropeanOption europeanOption = new EuropeanOption(maturity, strike);

		try {
			final Plot2D plot = createPlotOfHistogramBehindValues(simulation.getAssetValue(maturity, 0 /* assetIndex */), europeanOption.getValue(0.0, simulation), 100, 5.0);
			plot.show();
			Thread.sleep(2000);
		} catch (final Exception e) {
			fail("Failing with exception " + e.getMessage());
		}
	}

	private static Plot2D createPlotOfHistogramBehindValues(final RandomVariable randomVariableX, final RandomVariable randomVariableY, final int numberOfPoints, final double standardDeviations) {

		/*
		 * Create historgram
		 */
		final double[][] histogram = randomVariableX.getHistogram(numberOfPoints, standardDeviations);
		final List<Point2D> seriesForHistogram = new ArrayList<Point2D>();
		for(int i=0; i<histogram[0].length; i++) {
			seriesForHistogram.add(new Point2D(histogram[0][i],histogram[1][i]));
		}

		/*
		 * Create scatter
		 */
		final List<Point2D> seriesForScatter = new ArrayList<Point2D>();
		for(int i=0; i<randomVariableX.size(); i++) {
			seriesForScatter.add(new Point2D(randomVariableX.get(i), randomVariableY.get(i)));
		}

		/*
		 * The scatter and the histogram should be displaced on different axis.
		 * This will be the case if we pass different Axis objects.
		 */
		final NumberAxis domainAxis = new NumberAxis();
		final NumberAxis rangeAxis1 = new NumberAxis(-0.01, 0.1);
		final NumberAxis rangeAxis2 = new NumberAxis(-20.0, 200.0);

		final List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Scatter", seriesForScatter, domainAxis, rangeAxis2, new GraphStyle(new Rectangle(1, 1), null, Color.red)),
				new PlotablePoints2D("Histogram", seriesForHistogram, domainAxis, rangeAxis1, new GraphStyle(new Rectangle(10, 2), null, Color.BLUE))
				);

		return new Plot2D(plotables);
	}
}
