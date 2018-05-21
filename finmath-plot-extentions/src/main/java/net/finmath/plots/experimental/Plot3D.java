/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots.experimental;

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
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * Small convenient wrapper for JZY3D derived from the JZY3D SurfaceDemo.
 * 
 * @author Christian Fries
 */
public class Plot3D {

	private double xmin, xmax;
	private double ymin, ymax;
	private int numberOfPointsX, numberOfPointsY;
	private DoubleBinaryOperator function;

	
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
	        surface.setWireframeDisplayed(false);

	        // Create a chart
	        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
	        chart.getScene().getGraph().add(surface);
	    }
	}

	public void show() throws Exception {
		AnalysisLauncher.open(this.new Surface());
	}
}
