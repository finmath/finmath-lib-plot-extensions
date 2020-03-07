/*
 * Created on 26.12.2004
 *
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 */

package net.finmath.plots.jfreechart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.commons.math3.linear.RealMatrix;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Some utilities for JFreeChart
 *
 * @author Christian Fries
 */

public class JFreeChartUtilities {

	public static JFreeChart getContourPlot(final String labelX, final String labelY, final String labelZ, final RealMatrix dataMatrix) {
		/*
		 * Generate contour plot
		 */
		final int			numberOfValues	= dataMatrix.getColumnDimension()*dataMatrix.getRowDimension();
		final double[]	xValues			= new double[numberOfValues];
		final double[]	yValues			= new double[numberOfValues];
		final double[]	zValues			= new double[numberOfValues];
		int valueIndex = 0;
		for(int col=0; col<dataMatrix.getColumnDimension(); col++) {
			for(int row=0; row<dataMatrix.getRowDimension(); row++) {
				xValues[valueIndex] = col;
				yValues[valueIndex] = row;
				zValues[valueIndex] = dataMatrix.getEntry(row, col);
				valueIndex++;
			}
		}

		final DefaultXYZDataset dataset = new DefaultXYZDataset();
		dataset.addSeries("Correlation", new double[][] { xValues, yValues, zValues});

		return getContourPlot(dataset, new XYBlockRenderer(), new HuePaintScale(-1.0,1.0), new NumberAxis(labelX), new NumberAxis(labelY), new NumberAxis(labelZ), dataMatrix.getColumnDimension(), dataMatrix.getRowDimension());
	}

	public static JFreeChart getContourPlot(final DefaultXYZDataset dataset, final XYBlockRenderer renderer, final HuePaintScale paintScale, final NumberAxis xAxis, final NumberAxis yAxis, final NumberAxis zAxis, final int xItems, final int yItems)
	{
		final StyleGuide style = new StyleGuide(1);

		xAxis.setNumberFormatOverride(new DecimalFormat(" 0.00"));
		yAxis.setNumberFormatOverride(new DecimalFormat(" 0.00"));
		xAxis.setLabelFont(style.getAxisLabelFont());
		yAxis.setLabelFont(style.getAxisLabelFont());
		xAxis.setTickLabelFont(style.getTickLabelFont());
		yAxis.setTickLabelFont(style.getTickLabelFont());

		final XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		//        plot.setForegroundAlpha(0.66f);
		plot.setAxisOffset(new org.jfree.chart.ui.RectangleInsets(5, 5, 5, 5));

		final JFreeChart chart = new JFreeChart(null, style.getTitleFont(), plot, false);
		chart.removeLegend();
		chart.setBackgroundPaint(Color.white);

		zAxis.setNumberFormatOverride(new DecimalFormat(" 0.00"));

		/*
		xAxis.setAutoRange(true);
		yAxis.setAutoRange(true);
		xAxis.setAutoRangeIncludesZero(false);
		yAxis.setAutoRangeIncludesZero(false);
		 */

		renderer.setPaintScale(paintScale);
		final PaintScaleLegend psl = new PaintScaleLegend(renderer.getPaintScale(), zAxis);
		psl.setAxisOffset(5.0);
		psl.setPosition(RectangleEdge.LEFT);
		psl.setMargin(new RectangleInsets(5, 5, 5, 5));
		chart.addSubtitle(psl);

		updateContourPlot(dataset, renderer, paintScale, xAxis, yAxis, zAxis, xItems, yItems);

		return chart;
	}

	public static void updateContourPlot(final DefaultXYZDataset dataset, final XYBlockRenderer renderer, final HuePaintScale paintScale, final NumberAxis xAxis, final NumberAxis yAxis, final NumberAxis zAxis, final int xItems, final int yItems)
	{
		final StyleGuide style = new StyleGuide(1);

		double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
		double minZ = Double.MAX_VALUE, maxZ = Double.MIN_VALUE;
		for(int i=0; i<dataset.getItemCount(0); i++) {
			minX = Math.min(dataset.getXValue(0, i),minX);            maxX = Math.max(dataset.getXValue(0, i),maxX);
			minY = Math.min(dataset.getYValue(0, i),minY);            maxY = Math.max(dataset.getYValue(0, i),maxY);
			if(!Double.isNaN(dataset.getZ(0, i).doubleValue())) {
				minZ = Math.min(dataset.getZValue(0, i),minZ);            maxZ = Math.max(dataset.getZValue(0, i),maxZ);
			}
		}

		if(xAxis != null) {
			xAxis.setNumberFormatOverride(new DecimalFormat(" 0.00"));
			xAxis.setLabelFont(style.getAxisLabelFont());
			xAxis.setTickLabelFont(style.getTickLabelFont());
			xAxis.setRange(minX, maxX);
		}

		if(yAxis != null) {
			yAxis.setNumberFormatOverride(new DecimalFormat(" 0.00"));
			yAxis.setLabelFont(style.getAxisLabelFont());
			yAxis.setTickLabelFont(style.getTickLabelFont());
			yAxis.setRange(minY, maxY);
		}

		if(zAxis != null) {
			zAxis.setRange(minZ, maxZ);
		}

		if(renderer != null) {
			renderer.setBlockWidth((maxX-minX)/(xItems-1));
			renderer.setBlockHeight((maxY-minY)/(yItems-1));
		}

		if(paintScale != null) {
			paintScale.setLowerBound(minZ);
			paintScale.setUpperBound(maxZ);
		}
	}

	public static JFreeChart getXYLinesPlotChart(
			final String title,
			final String xAxisLabel, final String xAxisNumberFormat,
			final String yAxisLabel, final String yAxisNumberFormat,
			final double[] xValues, final double[] yValues)
	{
		/*
		 * Create data series
		 */
		final XYSeriesCollection data = new XYSeriesCollection();
		final XYSeries series = new XYSeries("1");
		for(int i=0; i<xValues.length; i++) {
			series.add(xValues[i], yValues[i]);
		}
		data.addSeries(series);

		return getXYPlotChart(title,
				xAxisLabel, xAxisNumberFormat,
				yAxisLabel, yAxisNumberFormat,
				data);
	}

	public static JFreeChart getXYLinesPlotChart(
			final String title,
			final String xAxisLabel, final String xAxisNumberFormat,
			final String yAxisLabel, final String yAxisNumberFormat,
			final RealMatrix xyData)
	{
		/*
		 * Create data series
		 */
		final XYSeriesCollection data = new XYSeriesCollection();
		for(int col=1; col<xyData.getColumnDimension(); col++) {
			final XYSeries series = new XYSeries("" + col);
			for(int row=0; row<xyData.getRowDimension(); row++) {
				series.add(xyData.getEntry(row, 0), xyData.getEntry(row, col));
			}
			data.addSeries(series);
		}

		return getXYPlotChart(title,
				xAxisLabel, xAxisNumberFormat,
				yAxisLabel, yAxisNumberFormat,
				data);
	}

	public static JFreeChart getCategoryLinesPlotChart(
			final String title,
			final String xAxisLabel, final String xAxisNumberFormat,
			final String yAxisLabel, final String yAxisNumberFormat,
			final RealMatrix xyData)
	{
		/*
		 * Create data series
		 */
		final XYSeriesCollection data = new XYSeriesCollection();
		for(int col=0; col<xyData.getColumnDimension(); col++) {
			final XYSeries series = new XYSeries("" + col);
			for(int row=0; row<xyData.getRowDimension(); row++) {
				series.add(row, xyData.getEntry(row, col));
			}
			data.addSeries(series);
		}

		return getXYPlotChart(title,
				xAxisLabel, xAxisNumberFormat,
				yAxisLabel, yAxisNumberFormat,
				data);
	}

	public static JFreeChart getXYPlotChart(
			final String title,
			final String xAxisLabel, final String xAxisNumberFormat,
			final String yAxisLabel, final String yAxisNumberFormat,
			final XYSeriesCollection data)
	{
		final StandardXYItemRenderer renderer	= new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
		renderer.setSeriesPaint(0, new java.awt.Color(255, 0,  0));
		renderer.setSeriesPaint(1, new java.awt.Color(0, 255,   0));
		renderer.setSeriesPaint(2, new java.awt.Color(0,   0, 255));

		return getXYPlotChart(
				title,
				xAxisLabel, xAxisNumberFormat,
				yAxisLabel, yAxisNumberFormat,
				data,
				renderer);
	}

	public static JFreeChart getXYPlotChart(
			final String title,
			final String xAxisLabel, final String xAxisNumberFormat,
			final String yAxisLabel, final String yAxisNumberFormat,
			final XYSeriesCollection data,
			final AbstractXYItemRenderer renderer)
	{
		return getXYPlotChart(title, xAxisLabel, xAxisNumberFormat, yAxisLabel, yAxisNumberFormat, data, renderer, false);
	}

	public static JFreeChart getXYPlotChart(
			final String title,
			final String xAxisLabel, final String xAxisNumberFormat,
			final String yAxisLabel, final String yAxisNumberFormat,
			final XYSeriesCollection data,
			final AbstractXYItemRenderer renderer,
			final boolean legend)
	{
		final StyleGuide style = new StyleGuide(2);
		final NumberAxis xAxis = new NumberAxis(xAxisLabel);
		final NumberAxis yAxis = new NumberAxis(yAxisLabel);

		xAxis.setNumberFormatOverride(new DecimalFormat(xAxisNumberFormat, new DecimalFormatSymbols(Locale.ENGLISH)));
		yAxis.setNumberFormatOverride(new DecimalFormat(yAxisNumberFormat, new DecimalFormatSymbols(Locale.ENGLISH)));

		xAxis.setLabelFont(style.getAxisLabelFont());
		yAxis.setLabelFont(style.getAxisLabelFont());
		xAxis.setTickLabelFont(style.getTickLabelFont());
		yAxis.setTickLabelFont(style.getTickLabelFont());

		xAxis.setAutoRangeIncludesZero(false);
		yAxis.setAutoRangeIncludesZero(false);

		final XYPlot plot = new XYPlot(data, xAxis, yAxis, renderer);
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		style.applyStyleToXYPlot2(plot);

		final JFreeChart chart = new JFreeChart(title, style.getTitleFont(), plot, legend);
		style.applyStyleToChart2(chart);

		return chart;
	}

	public static JFreeChart getXYPlotLogChart(
			final String title,
			final String xAxisLabel, final String xAxisNumberFormat,
			final String yAxisLabel, final String yAxisNumberFormat,
			final XYSeriesCollection data,
			final AbstractXYItemRenderer renderer,
			final boolean legend)
	{
		final StyleGuide style = new StyleGuide(2);
		final NumberAxis xAxis = new LogarithmicAxis(xAxisLabel);
		final NumberAxis yAxis = new NumberAxis(yAxisLabel);

		xAxis.setNumberFormatOverride(new DecimalFormat(xAxisNumberFormat, new DecimalFormatSymbols(Locale.ENGLISH)));
		yAxis.setNumberFormatOverride(new DecimalFormat(yAxisNumberFormat, new DecimalFormatSymbols(Locale.ENGLISH)));

		xAxis.setLabelFont(style.getAxisLabelFont());
		yAxis.setLabelFont(style.getAxisLabelFont());
		xAxis.setTickLabelFont(style.getTickLabelFont());
		yAxis.setTickLabelFont(style.getTickLabelFont());

		yAxis.setAutoRangeIncludesZero(false);

		final XYPlot plot = new XYPlot(data, xAxis, yAxis, renderer);
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		final JFreeChart chart = new JFreeChart(title, style.getTitleFont(), plot, legend);

		return chart;
	}

	/**
	 * Write a chart to an file stream in PDF format.
	 *
	 * @param file The file to write to.
	 * @param chart The chart to write.
	 * @param width The width.
	 * @param height The height.
	 * @throws IOException Thrown if the file could not be written.
	 */
	public static void saveChartAsPDF(final File file, final JFreeChart chart, final int width, final int height) throws IOException {
		final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		writeChartAsPDF(out, chart, width, height);
		out.close();
	}

	/**
	 * Write a chart to an file stream in SVG format.
	 *
	 * @param file The file to write to.
	 * @param chart The chart to write.
	 * @param width The width.
	 * @param height The height.
	 * @throws IOException Thrown if the file could not be written.
	 */
	public static void saveChartAsSVG(final File file, final JFreeChart chart, final int width, final int height) throws IOException {
		final SVGGraphics2D g2 = new SVGGraphics2D(width, height);
		g2.setRenderingHint(JFreeChart.KEY_SUPPRESS_SHADOW_GENERATION, true);
		final java.awt.Rectangle r = new java.awt.Rectangle(0, 0, width, height);
		chart.draw(g2, r);
		SVGUtils.writeToSVG(file, g2.getSVGElement());

	}

	/**
	 * Write a chart to an file stream in JPG format.
	 *
	 * @param file The file to write to.
	 * @param chart The chart to write.
	 * @param width The width.
	 * @param height The height.
	 * @throws IOException Thrown if the file could not be written.
	 */
	public static void saveChartAsJPG(final File file, final JFreeChart chart, final int width, final int height) throws IOException {
		final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));

		final BufferedImage imageWithAlpha = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2 = imageWithAlpha.createGraphics();
		final Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);

		chart.draw(g2, r2D);
		g2.dispose();

		// Strip alpha channel
		final BufferedImage imageWithoutAlpha = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);

		final Graphics2D graphics = imageWithoutAlpha.createGraphics();
		graphics.drawImage(imageWithAlpha, null, 0, 0);

		ImageIO.write(imageWithoutAlpha, "jpg", out);

		out.close();
	}

	/**
	 * Writes a chart to an output stream in PDF format.
	 *
	 * @param out the output stream.
	 * @param chart the chart.
	 * @param width the chart width.
	 * @param height the chart height.
	 */
	public static void writeChartAsPDF(final OutputStream out, final JFreeChart chart, final int width, final int height) {
		final com.itextpdf.text.Rectangle pagesize = new com.itextpdf.text.Rectangle(width, height);
		final Document document = new Document(pagesize, 50, 50, 50, 50);
		try {
			final PdfWriter writer = PdfWriter.getInstance(document, out);
			document.addAuthor("Christian Fries");
			document.addSubject("Chart");
			document.open();
			final PdfContentByte cb = writer.getDirectContent();
			final PdfTemplate tp = cb.createTemplate(width, height);
			final Graphics2D g2 = new PdfGraphics2D(tp, width, height);
			final Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);

			chart.draw(g2, r2D);
			g2.dispose();
			cb.addTemplate(tp, 0, 0);
		} catch (final DocumentException de) {
			System.err.println(de.getMessage());
		}
		document.close();
	}
}
