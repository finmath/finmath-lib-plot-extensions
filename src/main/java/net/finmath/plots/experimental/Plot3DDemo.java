/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.experimental;

import java.util.function.DoubleBinaryOperator;

import net.finmath.functions.AnalyticFormulas;

/**
 * Plots the value of an option under the Black-Scholes model as a function of strike and time-to-maturity.
 *
 * @author Christian Fries
 */
public class Plot3DDemo {

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

		DoubleBinaryOperator function = (strike, timeToMaturity) -> {
			double optionMaturity = timeToMaturity;
			double optionStrike = strike;

			double z = AnalyticFormulas.blackScholesOptionValue(initialStockValue, riskFreeRate, volatility, optionMaturity, optionStrike);

			return z;
		};

		Plot plot = new Plot3D(0,300.0, 0, 10, 100, 100, function);
		plot.setXAxisLabel("strike").setYAxisLabel("time to maturity").setZAxisLabel("value");
		plot.show();
	}
}
