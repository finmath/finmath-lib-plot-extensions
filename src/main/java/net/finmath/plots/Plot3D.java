/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.function.DoubleBinaryOperator;

import javax.swing.JFrame;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.Settings;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.text.ITextRenderer;
import org.jzy3d.plot3d.text.renderers.TextBitmapRenderer;

/**
 * Small convenient wrapper for JZY3D derived from the JZY3D SurfaceDemo.
 *
 * @author Christian Fries
 */
public class Plot3D implements Plot {

	private final double xmin, xmax;
	private final double ymin, ymax;
	private final int numberOfPointsX, numberOfPointsY;
	private final Named<DoubleBinaryOperator> function;

	private String title = "";
	private String xAxisLabel = "x";
	private String yAxisLabel = "y";
	private String zAxisLabel = "z";
	private Boolean isLegendVisible;

	private transient Frame frame;
	private transient Surface surface;
	private final Object updateLock = new Object();
	private Chart chart;

	public Plot3D(final double xmin, final double xmax, final double ymin, final double ymax, final int numberOfPointsX, final int numberOfPointsY, final Named<DoubleBinaryOperator> function) {
		super();
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.numberOfPointsX = numberOfPointsX;
		this.numberOfPointsY = numberOfPointsY;
		this.function = function;
	}

	public Plot3D(final double xmin, final double xmax, final double ymin, final double ymax, final int numberOfPointsX, final int numberOfPointsY, final DoubleBinaryOperator function) {
		this(xmin, xmax, ymin, ymax, numberOfPointsX, numberOfPointsY, new Named<DoubleBinaryOperator>("",function));
	}

	class Surface extends AbstractAnalysis {

		@Override
		public String getName() {
			return function.getName();
		}

		@Override
		public void init() {
			// Define a function to plot
			final Mapper mapper = new Mapper() {
				private final DoubleBinaryOperator functionToPlot = Plot3D.this.function.get();
				@Override
				public double f(final double x, final double y) {
					return functionToPlot.applyAsDouble(x,y);
				}
			};

			// Create the object to represent the function over the given range.
			final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(new Range((float)xmin,(float)xmax), numberOfPointsX, new Range((float)ymin,(float)ymax), numberOfPointsY), mapper);
			surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
			surface.setFaceDisplayed(true);
			surface.setWireframeDisplayed(true);
			surface.setWireframeColor(Color.BLACK);

			// Create a chart
			chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
			chart.getScene().getGraph().add(surface);

			chart.getAxeLayout().setXAxeLabelDisplayed(true);
			chart.getAxeLayout().setYAxeLabelDisplayed(true);
			//			chart.getAxeLayout().setXTickLabelDisplayed(true);
			chart.getAxeLayout().setXAxeLabel(xAxisLabel);
			chart.getAxeLayout().setYAxeLabel(yAxisLabel);
			chart.getAxeLayout().setZAxeLabel(zAxisLabel);

			//			chart.getAxeLayout().setYTickRenderer( new DateTickRenderer( "dd/MM/yyyy" ) );
			//			chart.getAxeLayout().setZAxeLabel( "Z" );
			//chart.getAxeLayout().setZTickRenderer( new ScientificNotationTickRenderer(2) );


			final AxeBox box = (AxeBox)chart.getView().getAxe();
			//			ITextRenderer renderer2 = new JOGLTextRenderer(new ShadowedTextStyle(128f, 10, java.awt.Color.RED, java.awt.Color.CYAN));
			//			ITextRenderer renderer3 = new TextBillboardRenderer();
			final ITextRenderer renderer = new TextBitmapRenderer(TextBitmapRenderer.Font.TimesRoman_24);
			box.setTextRenderer(renderer);

			chart.addKeyboardCameraController();
			chart.addKeyboardScreenshotController();
			chart.addMouseCameraController();
		}
	}

	@Override
	public void show() throws Exception {
		if(surface != null) close();
		surface = this.new Surface();

		Settings.getInstance().setHardwareAccelerated(true);
		surface.init();

		/*
        ChartLauncher.instructions();
        ChartLauncher.openChart(chart, new Rectangle(800, 400), "3D");
        ICameraMouseController mouse = configureControllers(chart, title, allowSlaveThreadOnDoubleClick, startThreadImmediatly);
        chart.render();
        frame(chart, bounds, title);
		 */

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				synchronized (updateLock) {
					if(frame != null) frame.dispose();

					frame = new JFrame();
					frame.add((Component)surface.getChart().getCanvas());

					frame.setSize(800, 400);
					frame.setVisible(true);
					frame.pack();
				}
			}
		});
	}

	@Override
	public void close() {
		synchronized (updateLock) {
			if(frame != null) frame.dispose();
		}
	}

	@Override
	public void saveAsJPG(final File file, final int width, final int height) throws IOException {
		(this.new Surface()).getChart().screenshot(file);
	}

	@Override
	public void saveAsPDF(final File file, final int width, final int height) {
		throw new UnsupportedOperationException("Save as PDF is not supported for this plot. Use saveAsJPG instead.");
	}

	@Override
	public void saveAsSVG(final File file, final int width, final int height) {
		throw new UnsupportedOperationException("Save as SVG is not supported for this plot. Use saveAsJPG instead.");
	}

	@Override
	public Plot setTitle(final String title) {
		this.title = title;
		return this;
	}

	@Override
	public Plot setXAxisLabel(final String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		return this;
	}

	@Override
	public Plot setYAxisLabel(final String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		return this;
	}

	@Override
	public Plot setZAxisLabel(final String zAxisLabel) {
		this.zAxisLabel = zAxisLabel;
		return this;
	}

	@Override
	public Plot setIsLegendVisible(final Boolean isLegendVisible) {
		this.isLegendVisible = isLegendVisible;
		return this;
	}

	@Override
	public String toString() {
		return "Plot3D [xmin=" + xmin + ", xmax=" + xmax + ", ymin=" + ymin + ", ymax=" + ymax + ", numberOfPointsX="
				+ numberOfPointsX + ", numberOfPointsY=" + numberOfPointsY + ", function=" + function + ", title="
				+ title + ", xAxisLabel=" + xAxisLabel + ", yAxisLabel=" + yAxisLabel + ", zAxisLabel=" + zAxisLabel
				+ ", isLegendVisible=" + isLegendVisible + "]";
	}
}
