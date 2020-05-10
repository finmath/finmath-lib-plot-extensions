/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Small convenient wrapper for Java FX line plot.
 *
 * @author Christian Fries
 */
public class Plot2DBarFX implements Plot {

	private List<PlotableCategories> plotables;

	private String title = "";
	private String xAxisLabel = "x";
	private String yAxisLabel = "y";
	private NumberFormat xAxisNumberFormat;
	private NumberFormat yAxisNumberFormat;
	private Double yAxisLowerBound;
	private Double yAxisUpperBound;
	private Double yAxisTick;
	private Boolean isLegendVisible = false;
	private Boolean isSeriesStacked = false;

	private XYChart<String,Number> chart;

	private final Object updateLock = new Object();


	public Plot2DBarFX(final List<PlotableCategories> plotables, final String title, final String xAxisLabel, final String yAxisLabel,
			final NumberFormat yAxisNumberFormat, final Double yAxisLowerBound, final Double yAxisUpperBound, final Double yAxisTick,
			final Boolean isLegendVisible) {
		super();
		this.plotables = plotables;
		this.title = title;
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		this.yAxisNumberFormat = yAxisNumberFormat;
		this.yAxisLowerBound = yAxisLowerBound;
		this.yAxisUpperBound = yAxisUpperBound;
		this.yAxisTick = yAxisTick;
		this.isLegendVisible = isLegendVisible;
	}

	public Plot2DBarFX(final List<PlotableCategories> plotables) {
		super();
		this.plotables = plotables;
	}

	/**
	 *
	 */
	public Plot2DBarFX() {
	}

	public static Plot2DBarFX of(final String[] labels, final double[] values, final String title, final String xAxisLabel, final String yAxisLabel, final NumberFormat yAxisNumberFormat, final boolean isLegendVisible) {
		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;
		final List<PlotableCategories> plotables = new ArrayList<PlotableCategories>();

		final String name = "Swaption";
		final List<Category2D> histogramAsList = new ArrayList<Category2D>();
		for(int i=0; i<values.length; i++) {
			final double value = values[i];
			histogramAsList.add(new Category2D(labels != null ? labels[i] : ""+i, value));
			min = Math.min(min, value);
			max = Math.max(max, value);
		}

		final PlotableCategories histo = new PlotableCategories() {

			@Override
			public String getName() {
				return name;
			}

			@Override
			public GraphStyle getStyle() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Category2D> getSeries() {
				return histogramAsList;
			}
		};

		plotables.add(histo);

		return new Plot2DBarFX(plotables, title, xAxisLabel, yAxisLabel, yAxisNumberFormat, min, max, (max-min)/10.0, isLegendVisible);
	}

	private void init() {
		//defining the axes
		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setAutoRanging(true);
		xAxis.setAnimated(false);

		final NumberAxis yAxis = new NumberAxis(yAxisLowerBound, yAxisUpperBound, yAxisTick);
		xAxis.setLabel(xAxisLabel);
		yAxis.setLabel(yAxisLabel);

		// creating the chart
		chart = isSeriesStacked ? new StackedBarChart<String,Number>(xAxis,yAxis) : new BarChart<String,Number>(xAxis,yAxis);
		chart.setAnimated(true);

		update();
	}

	private void update() {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				if(plotables == null) {
					return;
				}
				chart.setTitle(title);
				for(int functionIndex=0; functionIndex<plotables.size(); functionIndex++) {
					final PlotableCategories plotable = plotables.get(functionIndex);
					final GraphStyle style = plotable.getStyle();
					Color color = getColor(style);
					if(color == null) {
						color = getDefaultColor(functionIndex);
					}

					//					final String rgba = String.format("%d, %d, %d, %f", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255), (float)color.getOpacity());

					final List<Category2D> plotableSeries = plotable.getSeries();
					XYChart.Series<String, Number> series = null;
					if(functionIndex < chart.getData().size()) {
						series = chart.getData().get(functionIndex);
					}
					if(series == null) {
						series = new XYChart.Series<String, Number>();
						chart.getData().add(functionIndex, series);
					}
					series.setName(plotable.getName());
					for(int i = 0; i<plotableSeries.size(); i++) {
						XYChart.Data<String, Number> data = null;
						if(i < series.getData().size()) {
							data = series.getData().get(i);
						}
						if(data == null) {
							data = new XYChart.Data<String, Number>(plotableSeries.get(i).getName(), plotableSeries.get(i).getValue());
							/*
							if(style != null && style.getShape() != null) {
								data.setNode(new javafx.scene.shape.Rectangle(30,30, color));
							}
							 */
							series.getData().add(i, data);
						}
						data.setXValue(plotableSeries.get(i).getName());
						data.setYValue(plotableSeries.get(i).getValue());
					}

					/*
					 * Apply style to line
					 */
					//					if(style != null && style.getStoke() != null) series.getNode().setStyle("-fx-stroke: rgba(" + rgba + ");");
					//					else series.getNode().setStyle("-fx-stroke: none;");

					/*
			String rgb = String.format("%d, %d, %d", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),(int) (color.getBlue() * 255));
			series.getNode().setStyle("-fx-stroke: rgba(" + rgb + ",1.0);  -fx-background-color: #FF0000, white;");
			series.getNode().setStyle("-fx-stroke: rgba(" + rgb + ",1.0);  -fx-background-color: #FF0000, white;");
					 */
					//			lineChart.setStyle("-fx-create-symbols: false;");

					//			.default-color2.chart-line-symbol { -fx-background-color: #dda0dd, white; }
				}
				/*
				Node[] legendItems = chart.lookupAll(".chart-legend-item-symbol").toArray(new Node[0]);
				for(int i = 0; i<legendItems.length; i++) {
					Node legendItemNode = legendItems[i];
					Color color = getDefaultColor(i);
					String rgba = String.format("%d, %d, %d, %f", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255), (float)color.getOpacity());
					legendItemNode.setStyle("-fx-background-color: rgba("+rgba+");");
					chart.applyCss();
				}
				chart.applyCss();
				 */
			}
		});
	}

	private Color getColor(final GraphStyle style) {
		final java.awt.Color awtColor = style != null ? style.getColor() : null;
		Color color = null;
		if(awtColor != null) {
			color = new Color(awtColor.getRed()/255.0, awtColor.getGreen()/255.0, awtColor.getBlue()/255.0, awtColor.getAlpha()/255.0);
		}
		return color;
	}

	private Color getDefaultColor(final int functionIndex) {
		switch (functionIndex) {
		case 0:
			return new Color(1.0, 0,  0, 1.0);
		case 1:
			return new Color(0, 1.0,  0, 1.0);
		case 2:
			return new Color(0, 0,  1.0, 1.0);
		default:
			return new Color(0, 0,  0, 1.0);
		}
	}

	public Chart get() {
		init();
		return chart;
	}

	@Override
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// This method is invoked on Swing thread
				final JFrame frame = new JFrame("FX");
				final JFXPanel fxPanel = new JFXPanel();
				frame.add(fxPanel);
				frame.setVisible(true);
				frame.setSize(800, 600);
				//				frame.setSize(960, 540+22);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						init();

						fxPanel.setScene(new Scene(chart, 800,600));
						//						fxPanel.setScene(new Scene(chart,960,540+22));
					}
				});
				update();
			}
		});
	}

	@Override
	public void saveAsJPG(final File file, final int width, final int height) throws IOException {
	}

	public void saveAsPNG(final File file, final int width, final int height) throws IOException {
		if(chart == null) {
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {

					chart.setAnimated(false);

					final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));

					final BufferedImage imageWithAlpha = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
					final Graphics2D g2 = imageWithAlpha.createGraphics();
					final Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);

					final WritableImage image = chart.getScene().snapshot(null);
					ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(image, null), "png", out);

					/*
		// Strip alpha channel
		BufferedImage imageWithoutAlpha = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = imageWithoutAlpha.createGraphics();
		graphics.drawImage(imageWithAlpha, null, 0, 0);

		ImageIO.write(imageWithoutAlpha, "jpg", out);
					 */

					out.close();

				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}});
	}

	@Override
	public void saveAsPDF(final File file, final int width, final int height) throws IOException {
	}

	@Override
	public void saveAsSVG(final File file, final int width, final int height) throws IOException {
	}

	public Plot2DBarFX update(final List<PlotableCategories> plotables) {
		if(chart != null) {
			chart.setAnimated(true);
		}
		this.plotables = plotables;
		synchronized (updateLock) {
			if(chart != null) {
				update();
			}
		}
		if(chart != null) {
			chart.setAnimated(false);
		}
		return this;
	}

	@Override
	public Plot2DBarFX setTitle(final String title) {
		this.title = title;
		return this;
	}

	@Override
	public Plot2DBarFX setXAxisLabel(final String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		return this;
	}

	@Override
	public Plot2DBarFX setYAxisLabel(final String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		return this;
	}

	public Plot2DBarFX setYAxisRange(final Double min, final Double max) {
		if(chart == null || chart.getYAxis() == null) {
			return this;
		}
		if(min == null || max == null) {
			chart.getYAxis().setAutoRanging(true);
		}
		else {
			chart.getYAxis().setAutoRanging(false);
			((NumberAxis)chart.getYAxis()).setLowerBound(min);
			((NumberAxis)chart.getYAxis()).setUpperBound(max);
		}
		return this;
	}

	@Override
	public Plot setZAxisLabel(final String zAxisLabel) {
		throw new UnsupportedOperationException("The 2D plot does not suport a z-axis. Try 3D plot instead.");
	}

	/**
	 * @param isLegendVisible the isLegendVisible to set
	 */
	@Override
	public Plot setIsLegendVisible(final Boolean isLegendVisible) {
		this.isLegendVisible = isLegendVisible;
		return this;
	}

	public void setIsSeriesStacked(final Boolean isSeriesStacked) {
		this.isSeriesStacked = isSeriesStacked;
	}
}
