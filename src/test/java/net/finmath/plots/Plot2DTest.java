package net.finmath.plots;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

import org.junit.jupiter.api.Test;

import net.finmath.functions.AnalyticFormulas;

/**
 * Testing Plot2D.
 *
 * @author Chrisitan Fries
 */
public class Plot2DTest {

	@Test
	public void test() {
		final double initialStockValue = 100;
		final double riskFreeRate = 0.04;
		final double volatility = 0.40;

		final DoubleUnaryOperator function = (strike) -> {
			final double optionMaturity = 1.0;
			final double optionStrike = strike;

			final double z = AnalyticFormulas.blackScholesOptionValue(initialStockValue, riskFreeRate, volatility, optionMaturity, optionStrike);

			return z;
		};

		final Plot plot = new Plot2D(0.0, 300.0, 100, Arrays.asList(
				new Named<DoubleUnaryOperator>("Maturity 1", function)));

		plot.setTitle("Black-Scholes Model European Option Value").setXAxisLabel("strike").setYAxisLabel("value").setIsLegendVisible(true);
		try {
			plot.show();
			plot.saveAsJPG(new File("Test.jpg"), 800, 400);
			plot.saveAsSVG(new File("Test.svg"), 800, 400);
			plot.saveAsPDF(new File("Test.pdf"), 800, 400);
			Thread.sleep(10000);
		} catch (final Exception e) {
			throw(new RuntimeException(e));
//			fail("Failing with exception " + e.getMessage());
		}
	}

	@Test
	public void testFX() {
		final double initialStockValue = 100;
		final double riskFreeRate = 0.04;
		final double volatility = 0.40;

		final DoubleUnaryOperator function = (strike) -> {
			final double optionMaturity = 1.0;
			final double optionStrike = strike;

			final double z = AnalyticFormulas.blackScholesOptionValue(initialStockValue, riskFreeRate, volatility, optionMaturity, optionStrike);

			return z;
		};

		final Plot plot = new Plot2DFX(0.0, 300.0, 100, Arrays.asList(
				new Named<DoubleUnaryOperator>("Maturity 1", function)));

		plot.setTitle("Black-Scholes Model European Option Value").setXAxisLabel("strike").setYAxisLabel("value").setIsLegendVisible(true);
		try {
			plot.show();
			Thread.sleep(10000);
		} catch (final Exception e) {
			fail("Failing with exception " + e.getMessage());
		}
	}
}
