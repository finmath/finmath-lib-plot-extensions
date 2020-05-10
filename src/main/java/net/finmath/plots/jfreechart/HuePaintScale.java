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
 * A color scale.
 *
 * @author Christian Fries
 */
public class HuePaintScale implements PaintScale {
	private double lowerBound;
	private double upperBound;

	public HuePaintScale(final double lowerBound, final double upperBound) {
		this.setLowerBound(lowerBound);
		this.setUpperBound(upperBound);
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
		if(value > getUpperBound() || value < getLowerBound() || Double.isNaN(value)) {
			return Color.GRAY;
		}
		return Color.getHSBColor((float) ((value-getLowerBound()) / (getUpperBound()-getLowerBound()) * 240.0/360.0), 1.0f, 1.0f);
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}
}
