/*
 * Created on 26.12.2004
 *
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 */

package net.finmath.plots.jfreechart;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;

/**
 * @author Christian Fries
 */
public class StyleGuide {

	private final String fontName = "Arial";
	private final Font titleFont;
	private final Font axisLabelFont;
	private final Font annotationFont;
	private final Font tickLabelFont;

	private final Color chartBackgroundPaint;
	private final Color plotBackgroundPaint;

	public StyleGuide(final double scale) {
		titleFont			= new Font(Font.DIALOG, Font.PLAIN, (int)Math.round(10*scale));
		axisLabelFont		= new Font(Font.DIALOG, Font.PLAIN, (int)Math.round(10*scale));
		annotationFont		= new Font(Font.DIALOG, Font.PLAIN, (int)Math.round(8*scale));
		tickLabelFont		= new Font(Font.SERIF, Font.PLAIN, (int)Math.round(9*scale));

		chartBackgroundPaint	= new java.awt.Color(250, 250, 250);
		//		chartBackgroundPaint	= new java.awt.Color(255, 255, 255);
		plotBackgroundPaint		= new java.awt.Color(255, 255, 255);
	}

	public static void applyStyleToChart(final JFreeChart chart) {
		new StyleGuide(1).applyStyleToChart2(chart);
	}

	public static void applyStyleToXYPlot(final XYPlot xyPlot) {
		new StyleGuide(1).applyStyleToXYPlot2(xyPlot);
	}

	public void applyStyleToChart2(final JFreeChart chart) {
		chart.setBackgroundPaint(chartBackgroundPaint);

		if(chart.getTitle() != null) {
			chart.getTitle().setFont(titleFont);
		}

		final LegendTitle legend = chart.getLegend();
		if(legend != null) {
			legend.setBackgroundPaint(chartBackgroundPaint);
		}

		final XYPlot xyPlot = chart.getXYPlot();
		if(xyPlot != null) {
			applyStyleToXYPlot2(xyPlot);
		}
	}

	public void applyStyleToXYPlot2(final XYPlot xyPlot) {
		for(int i=0; i < xyPlot.getDomainAxisCount(); i++) {
			final ValueAxis axis = xyPlot.getDomainAxis(i);
			if(axis != null) {
				axis.setTickLabelFont(tickLabelFont);
				axis.setLabelFont(axisLabelFont);
			}
		}
		xyPlot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		for(int i=0; i < xyPlot.getRangeAxisCount(); i++) {
			final ValueAxis axis = xyPlot.getRangeAxis(i);
			if(axis != null) {
				axis.setTickLabelFont(tickLabelFont);
				axis.setLabelFont(axisLabelFont);
			}
		}
		xyPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		xyPlot.setBackgroundPaint(plotBackgroundPaint);
	}

	/**
	 * @return Font for axis labels.
	 */
	public Font getAxisLabelFont() {
		return axisLabelFont;
	}

	/**
	 * @return Font for axis tick labels.
	 */
	public Font getTickLabelFont() {
		return tickLabelFont;
	}

	/**
	 * @return Title font.
	 */
	public Font getTitleFont() {
		return titleFont;
	}
}
