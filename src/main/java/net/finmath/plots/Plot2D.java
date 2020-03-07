/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import net.finmath.plots.jfreechart.JFreeChartUtilities;
import net.finmath.plots.jfreechart.StyleGuide;

/**
 * Small convenient wrapper for JFreeChart line plot derived.
 *
 * @author Christian Fries
 */
public class Plot2D implements Plot {

	private List<Plotable2D> plotables;

	private String title = "";
	private String xAxisLabel = "x";
	private String yAxisLabel = "y";
	private NumberFormat xAxisNumberFormat;
	private NumberFormat yAxisNumberFormat;
	private Boolean isLegendVisible = false;

	private transient JFreeChart chart;
	private final Object updateLock = new Object();

	private Double ymin;
	private Double ymax;

	public Plot2D(final List<Plotable2D> plotables) {
		super();
		this.plotables = plotables;
	}

	public Plot2D(final double xmin, final double xmax, final int numberOfPointsX, final List<Named<DoubleUnaryOperator>> doubleUnaryOperators) {
		this(doubleUnaryOperators.stream().map(namedFunction -> { return new PlotableFunction2D(xmin, xmax, numberOfPointsX, namedFunction, null); }).collect(Collectors.toList()));
	}

	public Plot2D(final double xmin, final double xmax, final int numberOfPointsX, final DoubleUnaryOperator function) {
		this(xmin, xmax, numberOfPointsX, Collections.singletonList(new Named<DoubleUnaryOperator>("",function)));
	}

	public Plot2D(final double xmin, final double xmax, final DoubleUnaryOperator function) {
		this(xmin, xmax, 300, Collections.singletonList(new Named<DoubleUnaryOperator>("",function)));
	}

	public Plot2D(final double xmin, final double xmax, final int numberOfPointsX, final Function<Double, Double> function) {
		this(xmin, xmax, numberOfPointsX, Collections.singletonList(new Named<DoubleUnaryOperator>("", new DoubleUnaryOperator() {
			@Override
			public double applyAsDouble(double operand) {
				return function.apply(operand);
			}
		})));
	}

	private void init() {
		synchronized (updateLock) {
			if(chart != null) {
				return;
			}

			/*
			 * Creates an empty chart (the update method will set the data)
			 */
			final XYLineAndShapeRenderer renderer	= new XYLineAndShapeRenderer();
			final XYSeriesCollection data = new XYSeriesCollection();
			chart = JFreeChartUtilities.getXYPlotChart(title, xAxisLabel, "#.##" /* xAxisNumberFormat */, yAxisLabel, "#.##" /* yAxisNumberFormat */, data, renderer, isLegendVisible);
		}
	}

	private void update() {
		synchronized (updateLock) {

			final Map<net.finmath.plots.axis.NumberAxis, Integer> rangeAxisMap = new HashMap<net.finmath.plots.axis.NumberAxis, Integer>();
			for(int functionIndex=0; functionIndex<plotables.size(); functionIndex++) {
				final XYSeriesCollection data = new XYSeriesCollection();
				final Plotable2D plotable = plotables.get(functionIndex);

				final List<Point2D> plotableSeries = plotable.getSeries();
				final XYSeries series = new XYSeries(plotable.getName());
				for(int i = 0; i<plotableSeries.size(); i++) {
					series.add(plotableSeries.get(i).getX(), plotableSeries.get(i).getY());
				}
				data.addSeries(series);

				/*
				 * Define renderer from style
				 */
				final GraphStyle style = plotable.getStyle();
				final XYItemRenderer renderer;

				Color color = style != null ? plotable.getStyle().getColor() : null;
				if(color == null) {
					color = getDefaultColor(functionIndex);
				}

				if(style != null) {
					if(style.getFillColor() != null) {
						renderer	= new XYAreaRenderer();
						renderer.setSeriesPaint(0, style.getFillColor());
						if(style.getClass() != null) {
							((XYAreaRenderer)renderer).setSeriesOutlinePaint(0, style.getColor());
							((XYAreaRenderer)renderer).setOutline(true);
						}
					}
					else {
						renderer = new XYLineAndShapeRenderer();
						((XYLineAndShapeRenderer)renderer).setSeriesShapesVisible(0, style.getShape() != null);
						((XYLineAndShapeRenderer)renderer).setSeriesLinesVisible(0, style.getStroke() != null);
						renderer.setSeriesPaint(0, color);
					}
					renderer.setSeriesShape(0, plotable.getStyle().getShape());
					renderer.setSeriesStroke(0, plotable.getStyle().getStroke());
				}
				else {
					renderer = new XYLineAndShapeRenderer();
					renderer.setSeriesPaint(0, color);
				}

				if(chart != null) {
					chart.getXYPlot().setDataset(functionIndex, data);
					chart.getXYPlot().setRenderer(functionIndex, renderer);

					NumberAxis domain = (NumberAxis) chart.getXYPlot().getDomainAxis();
					if(plotable.getDomainAxis() != null) {
						domain = plotable.getDomainAxis().getImplementationJFree();
					}
					if(xAxisNumberFormat != null) {
						domain.setNumberFormatOverride(xAxisNumberFormat);
					}
					if(!domain.equals(chart.getXYPlot().getDomainAxis())) {
						chart.getXYPlot().setDomainAxis(functionIndex, domain);
					}

					NumberAxis range = (NumberAxis) chart.getXYPlot().getRangeAxis();
					if(plotable.getRangeAxis() != null) {
						range = plotable.getRangeAxis().getImplementationJFree();
					}
					if(yAxisNumberFormat != null) {
						range.setNumberFormatOverride(yAxisNumberFormat);
					}

					rangeAxisMap.putIfAbsent(plotable.getRangeAxis(), functionIndex);

					chart.getXYPlot().mapDatasetToRangeAxis(functionIndex, rangeAxisMap.get(plotable.getRangeAxis()));
					chart.getXYPlot().setRangeAxis(rangeAxisMap.get(plotable.getRangeAxis()), range);

					(new StyleGuide(2)).applyStyleToChart2(chart);

					/*
					if(ymin != null && ymax != null) {
						range.setAutoRange(false);
						range.setRange(new Range(ymin, ymax));
						System.out.println("Setting axis " + ymin);
					}
					else {
						System.out.println("Setting axis " + ymin);
						range.setAutoRange(true);
					}
					*/
				}
			}
		}
	}

	private Color getDefaultColor(final int functionIndex) {
		switch (functionIndex) {
		case 0:
			return new java.awt.Color(255, 0,  0);
		case 1:
			return new java.awt.Color(0, 255,  0);
		case 2:
			return new java.awt.Color(0, 0,  255);
		default:
			return new java.awt.Color(0, 0,  0);
		}
	}

	@Override
	public void show() {
		init();
		update(plotables);
		final JPanel chartPanel = new ChartPanel(chart,
				800, 400,   // size
				128, 128,   // minimum size
				2024, 2024, // maximum size
				false, true, true, false, true, false);    // useBuffer, properties, save, print, zoom, tooltips

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				synchronized (updateLock) {
					final JFrame frame = new JFrame();
					frame.add(chartPanel);
					frame.setVisible(true);
					frame.pack();
				}
			}
		});
	}

	@Override
	public void saveAsJPG(final File file, final int width, final int height) throws IOException {
		init();
		update(plotables);
		synchronized (updateLock) {
			JFreeChartUtilities.saveChartAsJPG(file, chart, width, height);
		}
	}

	@Override
	public void saveAsPDF(final File file, final int width, final int height) throws IOException {
		init();
		update(plotables);
		synchronized (updateLock) {
			JFreeChartUtilities.saveChartAsPDF(file, chart, width, height);
		}
	}

	@Override
	public void saveAsSVG(final File file, final int width, final int height) throws IOException {
		init();
		update(plotables);
		synchronized (updateLock) {
			JFreeChartUtilities.saveChartAsSVG(file, chart, width, height);
		}
	}

	public Plot2D update(final List<Plotable2D> plotables) {
		this.plotables = plotables;
		synchronized (updateLock) {
			if(chart != null) {
				update();
			}
		}
		return this;
	}

	@Override
	public Plot2D setTitle(final String title) {
		this.title = title;
		update();
		return this;
	}

	@Override
	public Plot2D setXAxisLabel(final String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		synchronized (updateLock) {
			if(chart != null) {
				chart.getXYPlot().getDomainAxis().setLabel(xAxisLabel);
			}
		}
		return this;
	}

	@Override
	public Plot2D setYAxisLabel(final String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		synchronized (updateLock) {
			if(chart != null) {
				chart.getXYPlot().getRangeAxis().setLabel(yAxisLabel);
			}
		}
		return this;
	}

	@Override
	public Plot setZAxisLabel(final String zAxisLabel) {
		throw new UnsupportedOperationException("The 2D plot does not suport a z-axis. Try 3D plot instead.");
	}

	public Plot2D setXAxisNumberFormat(final NumberFormat xAxisNumberFormat) {
		this.xAxisNumberFormat = xAxisNumberFormat;
		return this;
	}

	public Plot2D setYAxisNumberFormat(final NumberFormat yAxisNumberFormat) {
		this.yAxisNumberFormat = yAxisNumberFormat;
		return this;
	}

	public Plot setYRange(final double ymin, final double ymax) {
		this.ymin = ymin;
		this.ymax = ymax;

		update();

		return this;
	}

	/**
	 * @param isLegendVisible the isLegendVisible to set
	 */
	@Override
	public Plot setIsLegendVisible(final Boolean isLegendVisible) {
		this.isLegendVisible = isLegendVisible;
		update();
		return this;
	}

	@Override
	public String toString() {
		return "Plot2D [plotables=" + plotables + ", title=" + title + ", xAxisLabel=" + xAxisLabel + ", yAxisLabel="
				+ yAxisLabel + ", xAxisNumberFormat=" + xAxisNumberFormat + ", yAxisNumberFormat=" + yAxisNumberFormat
				+ ", isLegendVisible=" + isLegendVisible + ", ymin=" + ymin + ", ymax=" + ymax + "]";
	}
}
