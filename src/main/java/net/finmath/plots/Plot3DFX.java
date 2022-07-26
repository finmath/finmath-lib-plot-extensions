/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */

package net.finmath.plots;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
import javafx.scene.paint.Color;
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
public class Plot3DFX implements Plot {

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

	public Plot3DFX(final double xmin, final double xmax, final double ymin, final double ymax, final int numberOfPointsX, final int numberOfPointsY, final Named<DoubleBinaryOperator> function) {
		super();
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		this.numberOfPointsX = numberOfPointsX;
		this.numberOfPointsY = numberOfPointsY;
		this.function = function;
	}

	public Plot3DFX(final double xmin, final double xmax, final double ymin, final double ymax, final int numberOfPointsX, final int numberOfPointsY, final DoubleBinaryOperator function) {
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

						// scene
						Scene scene = new Scene(root, 800, 600, true, SceneAntialiasing.BALANCED);
						scene.setCamera(new PerspectiveCamera());

						scene.setOnMousePressed(me -> {
							mouseOldX = me.getSceneX();
							mouseOldY = me.getSceneY();
						});
						scene.setOnMouseDragged(me -> {
							mousePosX = me.getSceneX();
							mousePosY = me.getSceneY();
							rotateX.setAngle(rotateX.getAngle() - (mousePosY - mouseOldY));
							rotateY.setAngle(rotateY.getAngle() + (mousePosX - mouseOldX));
							mouseOldX = mousePosX;
							mouseOldY = mousePosY;

						});

						fxPanel.setScene(scene);
						//						fxPanel.setScene(new Scene(chart,960,540+22));
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
		// create axis walls
		Group cube = createCube(size);

		// initial cube rotation
		cube.getTransforms().addAll(rotateX, rotateY);

		// add objects to scene
		root = new StackPane();
		root.getChildren().add(cube);

		// perlin noise
		float[][] noiseArray = createNoise( size);

		// mesh
		TriangleMesh mesh = new TriangleMesh();

		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				mesh.getPoints().addAll(x, noiseArray[x][z], z);
			}
		}

		// texture
		int length = size;
		float total = length;

		for (float x = 0; x < length - 1; x++) {
			for (float y = 0; y < length - 1; y++) {

				float x0 = x / total;
				float y0 = y / total;
				float x1 = (x + 1) / total;
				float y1 = (y + 1) / total;

				mesh.getTexCoords().addAll( //
						x0, y0, // 0, top-left
						x0, y1, // 1, bottom-left
						x1, y1, // 2, top-right
						x1, y1 // 3, bottom-right
						);


			}
		}

		// faces
		for (int x = 0; x < length - 1; x++) {
			for (int z = 0; z < length - 1; z++) {

				int tl = x * length + z; // top-left
				int bl = x * length + z + 1; // bottom-left
				int tr = (x + 1) * length + z; // top-right
				int br = (x + 1) * length + z + 1; // bottom-right

				int offset = (x * (length - 1) + z ) * 8 / 2; // div 2 because we have u AND v in the list

				// working
				mesh.getFaces().addAll(bl, offset + 1, tl, offset + 0, tr, offset + 2);
				mesh.getFaces().addAll(tr, offset + 2, br, offset + 3, bl, offset + 1);

			}
		}


		// material
		Image diffuseMap = createImage(size, noiseArray);

		PhongMaterial material = new PhongMaterial();
		material.setDiffuseMap(diffuseMap);
		material.setSpecularColor(Color.WHITE);

		// mesh view
		MeshView meshView = new MeshView(mesh);
		meshView.setTranslateX(-0.5 * size);
		meshView.setTranslateZ(-0.5 * size);
		meshView.setMaterial(material);
		meshView.setCullFace(CullFace.NONE);
		meshView.setDrawMode(DrawMode.FILL);
		meshView.setDepthTest(DepthTest.ENABLE);

		cube.getChildren().addAll(meshView);

		// testing / debugging stuff: show diffuse map on chart
		ImageView iv = new ImageView(diffuseMap);
		iv.setTranslateX(-0.5 * size);
		iv.setTranslateY(-0.10 * size);
		iv.setRotate(90);
		iv.setRotationAxis(new Point3D(1, 0, 0));
		cube.getChildren().add(iv);

		makeZoomable(root);

	}
	private void update() {
		// TODO Auto-generated method stub

	}


	public Image createImage(double size, float[][] noise) {

		int width = (int) size;
		int height = (int) size;

		WritableImage wr = new WritableImage(width, height);
		PixelWriter pw = wr.getPixelWriter();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				float value = noise[x][y];

				double gray = normalizeValue(value, -250, 250.0, 0., 1.);

				gray = clamp(gray, 0, 1);


				Color color = Color.RED;
				if(gray < 0.5) color = Color.RED.interpolate(Color.YELLOW, gray*2);
				else color = Color.YELLOW.interpolate(Color.BLUE, gray*2-1);

				pw.setColor(x, y, color);

			}
		}

		return wr;

	}

	/**
	 * Axis wall
	 */
	public static class Axis extends Pane {

		private Rectangle wall;

		public Axis(double size) {

			// wall
			// first the wall, then the lines => overlapping of lines over walls
			// works
			wall = new Rectangle(size, size);
			getChildren().add(wall);

			// grid
			double zTranslate = 0;
			double lineWidth = 1.0;
			Color gridColor = Color.BLACK;

			for (int y = 0; y <= size; y += size / 10) {

				Line line = new Line(0, 0, size, 0);
				line.setStroke(gridColor);
				line.setFill(gridColor);
				line.setTranslateY(y);
				line.setTranslateZ(zTranslate);
				line.setStrokeWidth(lineWidth);

				getChildren().addAll(line);

			}

			for (int x = 0; x <= size; x += size / 10) {

				Line line = new Line(0, 0, 0, size);
				line.setStroke(gridColor);
				line.setFill(gridColor);
				line.setTranslateX(x);
				line.setTranslateZ(zTranslate);
				line.setStrokeWidth(lineWidth);

				getChildren().addAll(line);

			}

			for( int y=0; y <= size; y+=size/10) {

				Text text = new Text( ""+(size-y));
				text.setTranslateX(size + 10);

				text.setTranslateY(y);
				text.setTranslateZ(zTranslate);

				getChildren().addAll(text);

			}

		}

		public void setFill(Paint paint) {
			wall.setFill(paint);
		}

	}

	public void makeZoomable(StackPane control) {

		final double MAX_SCALE = 20.0;
		final double MIN_SCALE = 0.1;

		control.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {

				double delta = 1.2;

				double scale = control.getScaleX();

				if (event.getDeltaY() < 0) {
					scale /= delta;
				} else {
					scale *= delta;
				}

				scale = clamp(scale, MIN_SCALE, MAX_SCALE);

				control.setScaleX(scale);
				control.setScaleY(scale);

				event.consume();

			}

		});

	}

	/**
	 * Create axis walls
	 * @param size
	 * @return
	 */
	private Group createCube(int size) {

		Group cube = new Group();

		// size of the cube
		Color color = Color.DARKCYAN;

		List<Axis> cubeFaces = new ArrayList<>();
		Axis r;

		// back face
		r = new Axis(size);
		r.setFill(Color.rgb(32, 32, 32, 0.5));
		r.setTranslateX(-0.5 * size);
		r.setTranslateY(-0.5 * size);
		r.setTranslateZ(0.5 * size);

		cubeFaces.add(r);

		// bottom face
		r = new Axis(size);
		r.setFill(Color.rgb(32, 32, 32, 0.5));
		r.setTranslateX(-0.5 * size);
		r.setTranslateY(0);
		r.setRotationAxis(Rotate.X_AXIS);
		r.setRotate(90);

		cubeFaces.add(r);

		// right face
		r = new Axis(size);
		r.setFill(Color.rgb(32, 32, 32, 0.5));
		r.setTranslateX(-1 * size);
		r.setTranslateY(-0.5 * size);
		r.setRotationAxis(Rotate.Y_AXIS);
		r.setRotate(90);

		// cubeFaces.add( r);

		// left face
		r = new Axis(size);
		r.setFill(Color.rgb(32, 32, 32, 0.5));
		r.setTranslateX(0);
		r.setTranslateY(-0.5 * size);
		r.setRotationAxis(Rotate.Y_AXIS);
		r.setRotate(90);

		cubeFaces.add(r);

		// top face
		r = new Axis(size);
		r.setFill(Color.rgb(32, 32, 32, 0.5));
		r.setTranslateX(-0.5 * size);
		r.setTranslateY(-1 * size);
		r.setRotationAxis(Rotate.X_AXIS);
		r.setRotate(90);

		// cubeFaces.add( r);

		// front face
		r = new Axis(size);
		r.setFill(Color.rgb(32, 32, 32, 0.5));
		r.setTranslateX(-0.5 * size);
		r.setTranslateY(-0.5 * size);
		r.setTranslateZ(-0.5 * size);

		// cubeFaces.add( r);

		cube.getChildren().addAll(cubeFaces);

		return cube;
	}

	/**
	 * Create an array of the given size with values of perlin noise
	 * @param size
	 * @return
	 */
	private float[][] createNoise( int size) {
		float[][] noiseArray = new float[size][size];

		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {

				double value = function.get().applyAsDouble(xmin+(double)x/size*(xmax-xmin), ymin+(double)y/size*(ymax-ymin));

				min = Math.min(value, min);
				max = Math.max(value, max);
				noiseArray[x][y] = (float) value;
			}
		}

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				noiseArray[x][y] = (float) (0.5*size-((noiseArray[x][y]-min) / (max-min) * size));
			}
		}

		return noiseArray;

	}

	public static double normalizeValue(double value, double min, double max, double newMin, double newMax) {

		return (value - min) * (newMax - newMin) / (max - min) + newMin;

	}

	public static double clamp(double value, double min, double max) {

		if (Double.compare(value, min) < 0) {
			return min;
		}

		if (Double.compare(value, max) > 0) {
			return max;
		}

		return value;
	}

	@Override
	public Plot saveAsJPG(final File file, final int width, final int height) throws IOException {
		throw new UnsupportedOperationException("Save as PDF is not supported for this plot. Use saveAsJPG instead.");
	}

	@Override
	public Plot saveAsPDF(final File file, final int width, final int height) {
		throw new UnsupportedOperationException("Save as PDF is not supported for this plot. Use saveAsJPG instead.");
	}

	@Override
	public Plot saveAsSVG(final File file, final int width, final int height) {
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
