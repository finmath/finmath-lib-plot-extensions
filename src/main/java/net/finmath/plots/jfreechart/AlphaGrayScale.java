/*
 * Created on 26.12.2004
 *
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 */

package net.finmath.plots.jfreechart;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.PaintScale;

public class AlphaGrayScale implements PaintScale {
	double lowerBound;
	double upperBound;
	double frequency;
	double thickness;

	public AlphaGrayScale(double lowerBound, double upperBound, double frequency, double thickness) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.frequency = frequency;
		this.thickness = thickness;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public Paint getPaint(double value) {
		final float level = (float)Math.sin(0.5*Math.PI*(1.0-thickness));

		if(Double.isNaN(value)) return Color.GRAY;
		double percentageValue = Math.min(Math.max((value-lowerBound) / (upperBound-lowerBound),0.0),1.0);
		return new Color(0.0f, 0.0f, 0.0f, (float)Math.max(Math.sin(10*2*Math.PI*percentageValue)-level,0.0)/(1.0f-level));
	}
}
