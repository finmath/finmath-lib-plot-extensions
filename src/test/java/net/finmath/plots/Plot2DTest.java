package net.finmath.plots;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

import org.junit.Test;

import net.finmath.functions.AnalyticFormulas;

public class Plot2DTest {

	@Test
	public void test() {
		final double initialStockValue = 100;
		final double riskFreeRate = 0.04;
		final double volatility = 0.40;

		DoubleUnaryOperator function = (strike) -> {
			final double optionMaturity = 1.0;
			double optionStrike = strike;

			double z = AnalyticFormulas.blackScholesOptionValue(initialStockValue, riskFreeRate, volatility, optionMaturity, optionStrike);

			return z;
		};

		Plot plot = new Plot2D(0.0, 300.0, 100, Arrays.asList(
				new Named<DoubleUnaryOperator>("Maturity 1", function)));

		plot.setTitle("Black-Scholes Model European Option Value").setXAxisLabel("strike").setYAxisLabel("value").setIsLegendVisible(true);
		try {
			plot.show();
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
