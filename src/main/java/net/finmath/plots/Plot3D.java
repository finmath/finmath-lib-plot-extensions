/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart3d.Chart3D;
import org.jfree.chart3d.Chart3DFactory;
import org.jfree.chart3d.Chart3DPanel;
import org.jfree.chart3d.axis.ValueAxis3D;
import org.jfree.chart3d.data.Range;
import org.jfree.chart3d.data.function.Function3D;
import org.jfree.chart3d.graphics3d.Dimension3D;
import org.jfree.chart3d.plot.XYZPlot;
import org.jfree.chart3d.renderer.GradientColorScale;
import org.jfree.chart3d.renderer.xyz.SurfaceRenderer;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

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


	// size of graph
	private int size = 500;

	// variables for mouse interaction
	private double mousePosX, mousePosY;
	private double mouseOldX, mouseOldY;
	private final Rotate rotateX = new Rotate(20, Rotate.X_AXIS);
	private final Rotate rotateY = new Rotate(-45, Rotate.Y_AXIS);



	private transient JFrame frame;
	private StackPane root;
	private final Object updateLock = new Object();
	private Chart3D chart;

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

	@Override
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// This method is invoked on Swing thread
				if(frame != null) frame.dispose();

				frame = new JFrame("FX");
				final JFXPanel fxPanel = new JFXPanel();
				frame.add(fxPanel);
				frame.setVisible(true);
				frame.setSize(800, 600);
				//				frame.setSize(960, 540+22);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						init();
						final JPanel chartPanel = new Chart3DPanel(chart);
						chartPanel.setSize(new Dimension(800, 400));
						/*
								800, 400,   // size
								128, 128,   // minimum size
								2024, 2024, // maximum size
								false, true, true, false, true, false);    // useBuffer, properties, save, print, zoom, tooltips
*/
						java.awt.EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								synchronized (updateLock) {
									if(frame != null) frame.dispose();

									frame = new JFrame();
									frame.add(chartPanel);
									frame.setVisible(true);
									frame.pack();
								}
							}
						});						
					}

				});
				update();
			}

		});
	}

	@Override
	public void close() {
		synchronized (updateLock) {
			if(frame != null) frame.dispose();
		}
	}

	private void init() {
        chart = Chart3DFactory.createSurfaceChart(
                title,
                function.getName(),
                (x,y) -> function.get().applyAsDouble(x, y), xAxisLabel, zAxisLabel, yAxisLabel);
 
        XYZPlot plot = (XYZPlot) chart.getPlot();
        plot.setDimensions(new Dimension3D(10, 10, 10));
        ValueAxis3D xAxis = plot.getXAxis();
        xAxis.setRange(xmin, xmax);
        ValueAxis3D zAxis = plot.getZAxis();
        zAxis.setRange(ymin, ymax);
        SurfaceRenderer renderer = (SurfaceRenderer) plot.getRenderer();
        renderer.setDrawFaceOutlines(true);
        renderer.setColorScale(new GradientColorScale(plot.getYAxis().getRange(),Color.RED, Color.YELLOW));
	}

	private void update() {
		// TODO Auto-generated method stub

	}


	@Override
	public void saveAsJPG(final File file, final int width, final int height) throws IOException {
		throw new UnsupportedOperationException("Save as PDF is not supported for this plot. Use saveAsJPG instead.");
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
