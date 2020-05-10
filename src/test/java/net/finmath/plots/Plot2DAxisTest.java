package net.finmath.plots;

import static org.junit.Assert.fail;

import org.junit.Test;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.BrownianMotionFromMersenneRandomNumbers;
import net.finmath.montecarlo.BrownianMotionLazyInit;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.time.TimeDiscretizationFromArray;

/**
 * Testing Plot2D with different axis.
 *
 * @author Christian Fries
 */
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
		final var brownianMotion = new BrownianMotionFromMersenneRandomNumbers(td, 1, 100000, 3231);
		final var process = new EulerSchemeFromProcessModel(model, brownianMotion);

		// Using the process (Euler scheme), create an MC simulation of a Black-Scholes model
		final var simulation = new MonteCarloAssetModel(process);

		final EuropeanOption europeanOption = new EuropeanOption(maturity, strike);

		try {
			final Plot plot = Plots.createPlotOfHistogramBehindValues(simulation.getAssetValue(maturity, 0 /* assetIndex */), europeanOption.getValue(0.0, simulation), 100, 5.0);
			plot.show();
			Thread.sleep(20000);
		} catch (final Exception e) {
			e.printStackTrace();
			fail("Failing with exception " + e.getMessage());
		}
	}
}
