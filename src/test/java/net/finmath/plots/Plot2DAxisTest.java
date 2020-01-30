package net.finmath.plots;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.BrownianMotionLazyInit;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloAssetModel;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.*;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretizationFromArray;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Plot2DAxisTest {

	@Test
	public void test() throws CalculationException, InterruptedException {


		double modelInitialValue = 100.0;
		double modelRiskFreeRate = 0.05;
		double modelVolatility = 0.20;

		double maturity = 3.0;
		double strike = 106.0;

		// Create a model
		var model = new BlackScholesModel(modelInitialValue, modelRiskFreeRate, modelVolatility);

		// Create a corresponding MC process
		var td = new TimeDiscretizationFromArray(0.0, 300, 0.01);
		var brownianMotion = new BrownianMotionLazyInit(td, 1, 10000, 3231);
		var process = new EulerSchemeFromProcessModel(brownianMotion);

		// Using the process (Euler scheme), create an MC simulation of a Black-Scholes model
		var simulation = new MonteCarloAssetModel(model, process);


		EuropeanOption europeanOption = new EuropeanOption(maturity, strike);

		RandomVariable valueOfEuropeanOption = europeanOption.getValue(0.0, simulation).average();

		var value = valueOfEuropeanOption.doubleValue();


		createPlotOfHistogramBehindValues(simulation.getAssetValue(maturity, 0 /* assetIndex */), europeanOption.getValue(0.0, simulation), 100, 5.0).show();

		Thread.sleep(5000);
	}

	public static Plot2D createPlotOfHistogramBehindValues(RandomVariable randomVariableX, RandomVariable randomVariableY, int numberOfPoints, double standardDeviations) {

		/*
		 * Create historgram
		 */
		double[][] histogram = randomVariableX.getHistogram(numberOfPoints, standardDeviations);	
		List<Point2D> seriesForHistogram = new ArrayList<Point2D>();
		for(int i=0; i<histogram[0].length; i++) {
			seriesForHistogram.add(new Point2D(histogram[0][i],histogram[1][i]));
		}

		/*
		 * Create scatter
		 */
		List<Point2D> seriesForScatter = new ArrayList<Point2D>();
		for(int i=0; i<randomVariableX.size(); i++) {
			seriesForScatter.add(new Point2D(randomVariableX.get(i), randomVariableY.get(i)));
		}

		/*
		 * The scatter and the histogram should be displaced on different axis.
		 * This will be the case if we pass different Axis objects.
		 */
		NumberAxis domainAxis = new NumberAxis();
		NumberAxis rangeAxis1 = new NumberAxis(-0.01, 0.1);
		NumberAxis rangeAxis2 = new NumberAxis(-20.0, 200.0);
		
		List<Plotable2D> plotables = Arrays.asList(
				new PlotablePoints2D("Scatter", seriesForScatter, domainAxis, rangeAxis2, new GraphStyle(new Rectangle(1, 1), null, Color.red)),
				new PlotablePoints2D("Histogram", seriesForHistogram, domainAxis, rangeAxis1, new GraphStyle(new Rectangle(10, 2), null, Color.BLUE))
				);

		return new Plot2D(plotables);
	}
}
