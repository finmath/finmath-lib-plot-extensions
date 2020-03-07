/*
 * Created on 26.12.2004
 *
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 */

package net.finmath.plots.jfreechart;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.PaintScale;

/**
 * A color scale using alpha and gray.
 * 
 * @author Christian Fries
 */
public class AlphaGrayScale implements PaintScale {
	private double lowerBound;
	private double upperBound;
	private double frequency;
	private double thickness;

	public AlphaGrayScale(final double lowerBound, final double upperBound, final double frequency, final double thickness) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.frequency = frequency;
		this.thickness = thickness;
	}

	@Override
	public double getLowerBound() {
		return lowerBound;
	}

	@Override
	public double getUpperBound() {
		return upperBound;
	}

	@Override
	public Paint getPaint(final double value) {
		final float level = (float)Math.sin(0.5*Math.PI*(1.0-thickness));

		if(Double.isNaN(value)) {
			return Color.GRAY;
		}
		final double percentageValue = Math.min(Math.max((value-lowerBound) / (upperBound-lowerBound),0.0),1.0);
		return new Color(0.0f, 0.0f, 0.0f, (float)Math.max(Math.sin(10*2*Math.PI*percentageValue)-level,0.0)/(1.0f-level));
	}
}
