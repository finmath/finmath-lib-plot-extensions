/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.paint.Color;

/**
 * Small convenient wrapper for Java FX line plot.
 * 
 * @author Christian Fries
 */
public class Plot2DFX implements Plot {

	private List<Plotable2D> plotables;

	private String title = "";
	private String xAxisLabel = "x";
	private String yAxisLabel = "y";
	private NumberFormat xAxisNumberFormat;
	private NumberFormat yAxisNumberFormat;
	private Boolean isLegendVisible = false;

	LineChart<Number,Number> chart;
	private Object updateLock = new Object();

	public Plot2DFX(double xmin, double xmax, int numberOfPointsX, DoubleUnaryOperator function) {
		this(xmin, xmax, numberOfPointsX, Collections.singletonList(new Named<DoubleUnaryOperator>("",function)));
	}

	public Plot2DFX(double xmin, double xmax, int numberOfPointsX, List<Named<DoubleUnaryOperator>> doubleUnaryOperators) {
		this(doubleUnaryOperators.stream().map(namedFunction -> { return new PlotableFunction2D(xmin, xmax, numberOfPointsX, namedFunction, null); }).collect(Collectors.toList()));
	}

	public Plot2DFX(List<Plotable2D> plotables) {
		super();
		this.plotables = plotables;
	}

	private void init() {
		//defining the axes
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(xAxisLabel);
		yAxis.setLabel(yAxisLabel);
		//creating the chart
		chart = new LineChart<Number,Number>(xAxis,yAxis);
		update();
	}	

	private void update() {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				chart.setTitle(title);
				for(int functionIndex=0; functionIndex<plotables.size(); functionIndex++) {
					Plotable2D plotable = plotables.get(functionIndex);
					GraphStyle style = plotable.getStyle();
					Color color = getColor(style);
					if(color == null) color = getDefaultColor(functionIndex);

					String rgba = String.format("%d, %d, %d, %f", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255), (float)color.getOpacity());

					List<Point2D> plotableSeries = plotable.getSeries();
					XYChart.Series series = null;
					if(functionIndex < chart.getData().size()) series = chart.getData().get(functionIndex);
					if(series == null) {
						series = new XYChart.Series();
						chart.getData().add(functionIndex,series);
					}
					series.setName(plotable.getName());
					for(int i = 0; i<plotableSeries.size(); i++) {
						XYChart.Data<Number, Number> data = null;
						if(i < series.getData().size()) data = (Data<Number, Number>) series.getData().get(i);
						if(data == null) {
							data = new XYChart.Data(plotableSeries.get(i).getX(), plotableSeries.get(i).getY());
							if(style.getShape() != null) {
								data.setNode(new javafx.scene.shape.Rectangle(3,3, color));
							}
							series.getData().add(i, data);
						}
						data.setXValue(plotableSeries.get(i).getX());
						data.setYValue(plotableSeries.get(i).getY());
					}

					/*
					 * Apply style to line
					 */
					if(style.getStoke() != null) series.getNode().setStyle("-fx-stroke: rgba(" + rgba + ");");
					else series.getNode().setStyle("-fx-stroke: none;");

					/*
			String rgb = String.format("%d, %d, %d", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),(int) (color.getBlue() * 255));
			series.getNode().setStyle("-fx-stroke: rgba(" + rgb + ",1.0);  -fx-background-color: #FF0000, white;");
			series.getNode().setStyle("-fx-stroke: rgba(" + rgb + ",1.0);  -fx-background-color: #FF0000, white;");
					 */
					//			lineChart.setStyle("-fx-create-symbols: false;");

					//			.default-color2.chart-line-symbol { -fx-background-color: #dda0dd, white; }
				}
				Node[] legendItems = chart.lookupAll(".chart-legend-item-symbol").toArray(new Node[0]);
				for(int i = 0; i<legendItems.length; i++) {
					Node legendItemNode = legendItems[i];
					Color color = getDefaultColor(i);
					String rgba = String.format("%d, %d, %d, %f", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255), (float)color.getOpacity());
					legendItemNode.setStyle("-fx-background-color: rgba("+rgba+");");
					chart.applyCss();
				}
				chart.applyCss();
			}
		});
	}

	private Color getColor(GraphStyle style) {
		java.awt.Color awtColor = style.getColor();
		Color color = null;
		if(awtColor != null) {
			color = new Color(awtColor.getRed()/255.0, awtColor.getGreen()/255.0, awtColor.getBlue()/255.0, awtColor.getAlpha()/255.0);
		}
		return color;
	}

	private Color getDefaultColor(int functionIndex) {
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

	@Override
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// This method is invoked on Swing thread
				JFrame frame = new JFrame("FX");
				final JFXPanel fxPanel = new JFXPanel();
				frame.add(fxPanel);
				frame.setVisible(true);
				frame.setSize(800, 600);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						init();

						fxPanel.setScene(new Scene(chart,800,600));
					}
				});
				update();
			}
		});
	}

	@Override
	public void saveAsJPG(File file, int width, int height) throws IOException {
	}

	@Override
	public void saveAsPDF(File file, int width, int height) throws IOException {
	}

	@Override
	public void saveAsSVG(File file, int width, int height) throws IOException {
	}

	public Plot2DFX update(List<Plotable2D> plotables) {
		this.plotables = plotables;
		synchronized (updateLock) {
			if(chart != null) {
				update();
			}
		}
		return this;
	}

	@Override
	public Plot2DFX setTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	public Plot2DFX setXAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		return this;
	}

	@Override
	public Plot2DFX setYAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		return this;
	}

	@Override
	public Plot setZAxisLabel(String zAxisLabel) {
		throw new UnsupportedOperationException("The 2D plot does not suport a z-axis. Try 3D plot instead.");
	}

	/**
	 * @param isLegendVisible the isLegendVisible to set
	 */
	public Plot setIsLegendVisible(Boolean isLegendVisible) {
		this.isLegendVisible = isLegendVisible;
		return this;
	}
}
