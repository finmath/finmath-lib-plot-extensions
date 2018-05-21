/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.experimental;

import java.util.function.DoubleBinaryOperator;

import net.finmath.functions.AnalyticFormulas;

/**
 * @author Christian Fries
 *
 */
public class Plot3DDemo {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		final double initialStockValue = 100;
		final double riskFreeRate = 0.04;
		final double volatility = 0.40;
		
		DoubleBinaryOperator function = new DoubleBinaryOperator() {
			
			public double applyAsDouble(double x, double y) {
				double optionMaturity = x;
				double optionStrike = initialStockValue * (1+y/100.0);
				
				double z = AnalyticFormulas.blackScholesOptionValue(initialStockValue, riskFreeRate, volatility, optionMaturity, optionStrike);
				
				return z;
			}
		};
		Plot3D plot = new Plot3D(0,10.0, 100, -50, 50, 100, function);
		plot.show();
	}

}
