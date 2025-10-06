package net.finmath.plots;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import org.junit.jupiter.api.Test;

import net.finmath.plots.util.ColorUtils;

public class PlotableFunctionWithConfidenceInterval2DTest {

	// For plots: weight of the stroke
	private static Stroke dotted = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0f, new float[] {5,5}, 0);
	private static Stroke stroke = new BasicStroke(3.0f);
	
	@Test
	public void test() {
		double timeHorizonForPlotRates = 200.0;

		final List<Plotable2D> plotables = List.of(
				new PlotableFunctionWithConfidenceInterval2D(0.0, timeHorizonForPlotRates, (int)Math.round(timeHorizonForPlotRates/1.0)+1,
						new Named<DoubleUnaryOperator>("Experiment 1", (DoubleUnaryOperator)
								t -> t + 0.3 * t * t
								),
						new Named<DoubleUnaryOperator>("Experiment 1", (DoubleUnaryOperator)
								t -> t + 0.2 * t * t 
								),
						new Named<DoubleUnaryOperator>("Experiment 1", (DoubleUnaryOperator)
								t -> t + 0.5 * t * t								),
						new GraphStyle(null, dotted, Color.RED, ColorUtils.colorWithAlpha(Color.RED, 0.5))),
				new PlotableFunctionWithConfidenceInterval2D(0.0, timeHorizonForPlotRates, (int)Math.round(timeHorizonForPlotRates/1.0)+1,
						new Named<DoubleUnaryOperator>("Experiment 2", (DoubleUnaryOperator)
								t -> 0.9*t + 0.23 * t * t
								),
						new Named<DoubleUnaryOperator>("Experiment 2", (DoubleUnaryOperator)
								t -> 0.9*t + 0.18 * t * t 
								),
						new Named<DoubleUnaryOperator>("Experiment 2", (DoubleUnaryOperator)
								t -> 0.9*t + 0.28 * t * t
								),
						new GraphStyle(null, dotted, Color.BLUE, ColorUtils.colorWithAlpha(Color.BLUE, 0.5))),
 				new PlotableFunction2D(0.0, timeHorizonForPlotRates, (int)Math.round(timeHorizonForPlotRates/1.0)+1,
						new Named<DoubleUnaryOperator>("Benchmark", (DoubleUnaryOperator)
								t -> 1.0*t + 0.25 * t * t
								),
						new GraphStyle(null, stroke, Color.GREEN))
				);
		final Plot2D plot = new Plot2D(plotables);
		plot
		.setTitle("Example\n(with confidence intervall)")
		.setXAxisLabel("time t (in years since 2016)")
		.setYAxisLabel("values")
		.setYAxisNumberFormat(new DecimalFormat("0.0"))
		.setIsLegendVisible(true);
		plot.show();
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
