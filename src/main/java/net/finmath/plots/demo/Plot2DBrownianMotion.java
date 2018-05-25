/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.demo;

import java.util.function.DoubleFunction;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationInterface;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.plots.Named;
import net.finmath.plots.Plot;
import net.finmath.plots.PlotProcess2D;
import net.finmath.stochastic.RandomVariableInterface;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationInterface;

/**
 * Plots the value of an option under the Black-Scholes model as a function of strike and time-to-maturity.
 *
 * @author Christian Fries
 */
public class Plot2DBrownianMotion {

	/**
	 * Run the demo.
	 * Plots the value of an option under the Black-Scholes model as a function of strike and time-to-maturity.
	 * 
	 * @param args Not used.
	 * @throws Exception Exception from the graphics backend.
	 */
	public static void main(String[] args) throws Exception {

		final double initialValue = 100;
		final double riskFreeRate = 0.04;
		final double volatility = 0.40;
		final double optionMaturity = 1.0;
		final int numberOfTimeSteps = 100;
		final double deltaT = 0.1;
		final int numberOfPaths = 1000;

		TimeDiscretizationInterface timeDiscretization = new TimeDiscretization(0.0, numberOfTimeSteps, deltaT);
		AssetModelMonteCarloSimulationInterface blackScholesMonteCarlo = new MonteCarloBlackScholesModel(timeDiscretization, numberOfPaths, initialValue, riskFreeRate, volatility);

		DoubleFunction<RandomVariableInterface> function = (time) -> {
			try {
				return blackScholesMonteCarlo.getAssetValue(time, 0 /* assetIndex */);
			} catch (ArrayIndexOutOfBoundsException | CalculationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		};

		Plot plot = new PlotProcess2D(timeDiscretization, new Named<>("Black-Scholes",function));
		plot.setTitle("Black-Scholes Model Path").setXAxisLabel("time").setYAxisLabel("value");
		plot.show();
		
		// To save as PDF uncomment the following line
		// plot.saveAsPDF(new File("Black-Scholes Model Path.pdf"), 800, 400);
	}
}
