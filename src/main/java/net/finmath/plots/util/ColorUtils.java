package net.finmath.plots.util;

import java.awt.Color;

public class ColorUtils {

	/**
	 * Changes the alpha by a multiplicative factor
	 * 
	 * @param color Given color.
	 * @param alpha Alpha factor.
	 * @return New color with modified alpha.
	 */
	public static Color colorWithAlpha(Color color, double alpha) {
		int newAlpha = (int) Math.min(Math.round(color.getAlpha()*alpha),255);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), newAlpha);
	}
}
