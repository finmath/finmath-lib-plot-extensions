/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.demo;

import java.util.function.DoubleUnaryOperator;

import net.finmath.functions.AnalyticFormulas;
import net.finmath.plots.Plot;
import net.finmath.plots.Plot2D;

/**
 * Plots the value of an option under the Black-Scholes model as a function of strike and time-to-maturity.
 *
 * @author Christian Fries
 */
public class Plot2DDemo {

	/**
	 * Run the demo.
	 * Plots the value of an option under the Black-Scholes model as a function of strike and time-to-maturity.
	 * 
	 * @param args Not used.
	 * @throws Exception Exception from the graphics backend.
	 */
	public static void main(String[] args) throws Exception {

		final double initialStockValue = 100;
		final double riskFreeRate = 0.04;
		final double volatility = 0.40;
		final double optionMaturity = 1.0;

		DoubleUnaryOperator function = (strike) -> {
			double optionStrike = strike;

			double z = AnalyticFormulas.blackScholesOptionValue(initialStockValue, riskFreeRate, volatility, optionMaturity, optionStrike);

			return z;
		};

		Plot plot = new Plot2D(0,300.0, 100, function);
		plot.setTitle("Black-Scholes Model European Option Value").setXAxisLabel("strike").setYAxisLabel("value");
		plot.show();
	}
}
