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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;

/**
 * @author Christian Fries
 */
public class StyleGuide {
	
	static Font titleFont;
    static Font axisLabelFont;
    static Font annotationFont;
    static Font tickLabelFont;

    private Color chartBackgroundPaint;
    private Color plotBackgroundPaint;

    public StyleGuide(double scale) {
        titleFont			= new Font("SansSerif", Font.BOLD, (int)Math.round(10*scale));
        axisLabelFont		= new Font("SansSerif", Font.PLAIN, (int)Math.round(10*scale));
        annotationFont		= new Font("SansSerif", Font.PLAIN, (int)Math.round(8*scale));
        tickLabelFont		= new Font("SansSerif", Font.PLAIN, (int)Math.round(9*scale));

        chartBackgroundPaint	= new java.awt.Color(247, 247, 247);
        plotBackgroundPaint		= new java.awt.Color(255, 255, 255);
	}

	public static void applyStyleToChart(JFreeChart chart) {
		new StyleGuide(1).applyStyleToChart2(chart);
	}

	public static void applyStyleToXYPlot(XYPlot xyPlot) {
		new StyleGuide(1).applyStyleToXYPlot2(xyPlot);
	}
    
	public void applyStyleToChart2(JFreeChart chart) {
		chart.setBackgroundPaint(chartBackgroundPaint);

		if(chart.getTitle() != null) {
			chart.getTitle().setFont(titleFont);
		}
		
		LegendTitle legend = chart.getLegend();
		if(legend != null) {
			legend.setBackgroundPaint(chartBackgroundPaint);
		}
		
		XYPlot xyPlot = chart.getXYPlot();
		if(xyPlot != null) {
			applyStyleToXYPlot2(xyPlot);
		}
	}
	
	public void applyStyleToXYPlot2(XYPlot xyPlot) {
	    if(xyPlot.getDomainAxis() != null) {
	        xyPlot.getDomainAxis().setTickLabelFont(tickLabelFont);
	        xyPlot.getDomainAxis().setLabelFont(axisLabelFont);
		    xyPlot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
	    }
	    if(xyPlot.getRangeAxis() != null) {
	        xyPlot.getRangeAxis().setTickLabelFont(tickLabelFont);
	        xyPlot.getRangeAxis().setLabelFont(axisLabelFont);
		    xyPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
	    }
	    xyPlot.setBackgroundPaint(plotBackgroundPaint);
	}

	/**
     * @return Font for axis labels.
     */
    public static Font getAxisLabelFont() {
        return new Font("SansSerif", Font.PLAIN, (int)Math.round(10*1));
    }

	/**
     * @return Font for axis tick labels.
     */
    public static Font getTickLabelFont() {
        return new Font("SansSerif", Font.PLAIN, (int)Math.round(9*1));
    }

	/**
     * @return Title font.
     */
    public static Font getTitleFont() {
        return new Font("SansSerif", Font.PLAIN, (int)Math.round(10*1));
    }
}
