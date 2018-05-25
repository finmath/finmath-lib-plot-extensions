/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.io.File;
import java.io.IOException;
import java.util.function.DoubleBinaryOperator;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
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

	private double xmin, xmax;
	private double ymin, ymax;
	private int numberOfPointsX, numberOfPointsY;
	private DoubleBinaryOperator function;

	private String title = "";
	private String xAxisLabel = "x";
	private String yAxisLabel = "y";
	private String zAxisLabel = "z";
	private Boolean isLegendVisible;

	public Plot3D(double xmin, double xmax, double ymin, double ymax, int numberOfPointsX, int numberOfPointsY, DoubleBinaryOperator function) {
		super();
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.numberOfPointsX = numberOfPointsX;
		this.numberOfPointsY = numberOfPointsY;
		this.function = function;
	}

	class Surface extends AbstractAnalysis {

		public void init() {
			// Define a function to plot
			Mapper mapper = new Mapper() {
				@Override
				public double f(double x, double y) {
					return Plot3D.this.function.applyAsDouble(x,y);
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


			AxeBox box = (AxeBox)chart.getView().getAxe();
			//			ITextRenderer renderer2 = new JOGLTextRenderer(new ShadowedTextStyle(128f, 10, java.awt.Color.RED, java.awt.Color.CYAN));
			//			ITextRenderer renderer3 = new TextBillboardRenderer();
			ITextRenderer renderer = new TextBitmapRenderer(TextBitmapRenderer.Font.TimesRoman_24);
			box.setTextRenderer(renderer);
		}
	}

	@Override
	public void show() throws Exception {
		AnalysisLauncher.open(this.new Surface());
	}

	@Override
	public void saveAsJPG(File file, int width, int height) throws IOException {
		(this.new Surface()).getChart().screenshot(file);
	}
	
	@Override
	public void saveAsPDF(File file, int width, int height) throws IOException {
		throw new UnsupportedOperationException("Save as PDF is not supported for this plot. Use saveAsJPG instead.");
	}

	@Override
	public void saveAsSVG(File file, int width, int height) throws IOException {
		throw new UnsupportedOperationException("Save as SVG is not supported for this plot. Use saveAsJPG instead.");
	}

	@Override
	public Plot setTitle(String title) {
		this.title = title;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.finmath.plots.experimental.Plot#setXAxisLabel(java.lang.String)
	 */
	@Override
	public Plot setXAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.finmath.plots.experimental.Plot#setYAxisLabel(java.lang.String)
	 */
	@Override
	public Plot setYAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
		return this;
	}

	@Override
	public Plot setZAxisLabel(String zAxisLabel) {
		this.zAxisLabel = zAxisLabel;
		return this;
	}

	@Override
	public Plot setIsLegendVisible(Boolean isLegendVisible) {
		this.isLegendVisible = isLegendVisible;
		return this;
	}
}
