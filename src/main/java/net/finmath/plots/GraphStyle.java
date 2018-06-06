/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * A stile for a given graph specifying color, shape and stroke.
 * If shape is null, no points will be drawn.
 * If stroke is null, no line will be drawn.
 * 
 * @author Christian Fries
 */
public class GraphStyle {
	
	private Shape shape;
	private Stroke stoke;
	private Color color;
	
	public GraphStyle(Shape shape, Stroke stoke, Color color) {
		super();
		this.shape = shape;
		this.stoke = stoke;
		this.color = color;
	}

	public Shape getShape() {
		return shape;
	}

	public Stroke getStoke() {
		return stoke;
	}

	public Color getColor() {
		return color;
	}
}