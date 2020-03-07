/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

/**
 * Class representing a point in F^2 where F is the set of <code>double</double> floating point numbers.
 * 
 * @author Christian Fries
 */
public class Point2D {
	private final double x;
	private final double y;

	/**
	 * Create object representing a point in F^2 where F is the set of <code>double</double> floating point numbers.
	 * 
	 * @param x The x value of (x,y).
	 * @param y The y value of (x,y).
	 */
	public Point2D(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the x value of (x,y).
	 * 
	 * @return the x value of (x,y).
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y value of (x,y).
	 * 
	 * @return the y value of (x,y).
	 */
	public double getY() {
		return y;
	}
}
