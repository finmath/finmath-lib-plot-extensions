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

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Some utilities for JFreeChart
 * 
 * @author Christian Fries
 */

public class JFreeChartUtilities {

	public static JFreeChart getContourPlot(String labelX, String labelY, String labelZ, RealMatrix dataMatrix) {
		/*
		 * Generate contour plot
		 */
		int			numberOfValues	= dataMatrix.getColumnDimension()*dataMatrix.getRowDimension();
		double[]	xValues			= new double[numberOfValues];
		double[]	yValues			= new double[numberOfValues];
		double[]	zValues			= new double[numberOfValues];
		int valueIndex = 0;
		for(int col=0; col<dataMatrix.getColumnDimension(); col++) {
			for(int row=0; row<dataMatrix.getRowDimension(); row++) {
				xValues[valueIndex] = (double)col;
				yValues[valueIndex] = (double)row;
				zValues[valueIndex] = dataMatrix.getEntry(row, col);
				valueIndex++;
			}			
		}

		DefaultXYZDataset dataset = new DefaultXYZDataset();
		dataset.addSeries("Correlation", new double[][] { xValues, yValues, zValues});

		return getContourPlot(dataset, new XYBlockRenderer(), new HuePaintScale(-1.0,1.0), new NumberAxis(labelX), new NumberAxis(labelY), new NumberAxis(labelZ), dataMatrix.getColumnDimension(), dataMatrix.getRowDimension());
	}

	public static JFreeChart getContourPlot(DefaultXYZDataset dataset, XYBlockRenderer renderer, HuePaintScale paintScale, NumberAxis xAxis, NumberAxis yAxis, NumberAxis zAxis, int xItems, int yItems)
	{
		xAxis.setNumberFormatOverride(new DecimalFormat(" 0.00"));
		yAxis.setNumberFormatOverride(new DecimalFormat(" 0.00"));
		xAxis.setLabelFont(StyleGuide.axisLabelFont);
		yAxis.setLabelFont(StyleGuide.axisLabelFont);
		xAxis.setTickLabelFont(StyleGuide.tickLabelFont);
		yAxis.setTickLabelFont(StyleGuide.tickLabelFont);

		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer); 
		plot.setBackgroundPaint(Color.lightGray); 
		plot.setDomainGridlinePaint(Color.white); 
		plot.setRangeGridlinePaint(Color.white); 
		//        plot.setForegroundAlpha(0.66f); 
		plot.setAxisOffset(new org.jfree.chart.ui.RectangleInsets(5, 5, 5, 5)); 

		JFreeChart chart = new JFreeChart(null, StyleGuide.titleFont, plot, false);
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
		PaintScaleLegend psl = new PaintScaleLegend(renderer.getPaintScale(), zAxis); 
		psl.setAxisOffset(5.0); 
		psl.setPosition(RectangleEdge.LEFT); 
		psl.setMargin(new RectangleInsets(5, 5, 5, 5)); 
		chart.addSubtitle(psl); 

		updateContourPlot(dataset, renderer, paintScale, xAxis, yAxis, zAxis, xItems, yItems);

		return chart;
	}

	public static void updateContourPlot(DefaultXYZDataset dataset, XYBlockRenderer renderer, HuePaintScale paintScale, NumberAxis xAxis, NumberAxis yAxis, NumberAxis zAxis, int xItems, int yItems)
	{
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
			xAxis.setLabelFont(StyleGuide.axisLabelFont);
			xAxis.setTickLabelFont(StyleGuide.tickLabelFont);
			xAxis.setRange(minX, maxX);
		}

		if(yAxis != null) {
			yAxis.setNumberFormatOverride(new DecimalFormat(" 0.00"));
			yAxis.setLabelFont(StyleGuide.axisLabelFont);
			yAxis.setTickLabelFont(StyleGuide.tickLabelFont);
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
			paintScale.lowerBound = minZ;
			paintScale.upperBound = maxZ;
		}
	}

	public static JFreeChart getXYLinesPlotChart(
			String title,
			String xAxisLabel, String xAxisNumberFormat, 
			String yAxisLabel, String yAxisNumberFormat, 
			double[] xValues, double[] yValues)
	{
		/*
		 * Create data series
		 */
		XYSeriesCollection data = new XYSeriesCollection();
		XYSeries series = new XYSeries("1");
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
			String title,
			String xAxisLabel, String xAxisNumberFormat, 
			String yAxisLabel, String yAxisNumberFormat, 
			RealMatrix xyData)
	{
		/*
		 * Create data series
		 */
		XYSeriesCollection data = new XYSeriesCollection();
		for(int col=1; col<xyData.getColumnDimension(); col++) {
			XYSeries series = new XYSeries("" + col);
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
			String title,
			String xAxisLabel, String xAxisNumberFormat, 
			String yAxisLabel, String yAxisNumberFormat,
			RealMatrix xyData)
	{
		/*
		 * Create data series
		 */
		XYSeriesCollection data = new XYSeriesCollection();
		for(int col=0; col<xyData.getColumnDimension(); col++) {
			XYSeries series = new XYSeries("" + col);
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
			String title,
			String xAxisLabel, String xAxisNumberFormat, 
			String yAxisLabel, String yAxisNumberFormat, 
			XYSeriesCollection data)
	{
		StandardXYItemRenderer renderer	= new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
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
			String title,
			String xAxisLabel, String xAxisNumberFormat, 
			String yAxisLabel, String yAxisNumberFormat, 
			XYSeriesCollection data,
			AbstractXYItemRenderer renderer)
	{
		return getXYPlotChart(title, xAxisLabel, xAxisNumberFormat, yAxisLabel, yAxisNumberFormat, data, renderer, false);
	}

	public static JFreeChart getXYPlotChart(
			String title,
			String xAxisLabel, String xAxisNumberFormat, 
			String yAxisLabel, String yAxisNumberFormat, 
			XYSeriesCollection data,
			AbstractXYItemRenderer renderer,
			boolean legend)
	{		
		NumberAxis xAxis = new NumberAxis(xAxisLabel);
		NumberAxis yAxis = new NumberAxis(yAxisLabel);

		xAxis.setNumberFormatOverride(new DecimalFormat(xAxisNumberFormat, new DecimalFormatSymbols(Locale.ENGLISH)));
		yAxis.setNumberFormatOverride(new DecimalFormat(yAxisNumberFormat, new DecimalFormatSymbols(Locale.ENGLISH)));

		xAxis.setLabelFont(StyleGuide.getAxisLabelFont());
		yAxis.setLabelFont(StyleGuide.getAxisLabelFont());
		xAxis.setTickLabelFont(StyleGuide.getTickLabelFont());
		yAxis.setTickLabelFont(StyleGuide.getTickLabelFont());

		yAxis.setAutoRangeIncludesZero(false);

		XYPlot plot = new XYPlot(data, xAxis, yAxis, renderer);
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		new StyleGuide(2).applyStyleToXYPlot2(plot);
		JFreeChart chart = new JFreeChart(title, StyleGuide.getTitleFont(), plot, legend);
		new StyleGuide(2).applyStyleToChart2(chart);

		return chart;
	}

	public static JFreeChart getXYPlotLogChart(
			String title,
			String xAxisLabel, String xAxisNumberFormat, 
			String yAxisLabel, String yAxisNumberFormat, 
			XYSeriesCollection data,
			AbstractXYItemRenderer renderer,
			boolean legend)
	{		
		NumberAxis xAxis = new LogarithmicAxis(xAxisLabel);
		NumberAxis yAxis = new NumberAxis(yAxisLabel);

		xAxis.setNumberFormatOverride(new DecimalFormat(xAxisNumberFormat, new DecimalFormatSymbols(Locale.ENGLISH)));
		yAxis.setNumberFormatOverride(new DecimalFormat(yAxisNumberFormat, new DecimalFormatSymbols(Locale.ENGLISH)));

		xAxis.setLabelFont(StyleGuide.axisLabelFont);
		yAxis.setLabelFont(StyleGuide.axisLabelFont);
		xAxis.setTickLabelFont(StyleGuide.tickLabelFont);
		yAxis.setTickLabelFont(StyleGuide.tickLabelFont);

		yAxis.setAutoRangeIncludesZero(false);

		XYPlot plot = new XYPlot(data, xAxis, yAxis, renderer);
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		JFreeChart chart = new JFreeChart(title, StyleGuide.titleFont, plot, legend);

		return chart;
	}

	public static void saveChartAsPDF(File file, JFreeChart chart, int width,
			int height) throws IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		writeChartAsPDF(out, chart, width, height);
		out.close();
	}

	public static void saveChartAsJPG(File file, JFreeChart chart, int width, int height) throws IOException {
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));

		BufferedImage imageWithAlpha = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = imageWithAlpha.createGraphics();
		Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);

		chart.draw(g2, r2D);
		g2.dispose();

		// Strip alpha channel
		BufferedImage imageWithoutAlpha = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = imageWithoutAlpha.createGraphics();
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
	public static void writeChartAsPDF(OutputStream out, JFreeChart chart,
			int width, int height) throws IOException {
		Rectangle pagesize = new Rectangle(width, height);
		Document document = new Document(pagesize, 50, 50, 50, 50);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.addAuthor("Christian Fries");
			document.addSubject("Chart");
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate tp = cb.createTemplate(width, height);
			Graphics2D g2 = new PdfGraphics2D(tp, width, height);
			Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);

			chart.draw(g2, r2D);
			g2.dispose();
			cb.addTemplate(tp, 0, 0);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		}
		document.close();
	}
}
