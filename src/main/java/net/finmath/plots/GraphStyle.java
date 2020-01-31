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

	private final Shape shape;
	private final Stroke stroke;
	private final Color color;
	private final Color fillColor;

	public GraphStyle(final Shape shape, final Stroke stroke, final Color color, final Color fillColor) {
		super();
		this.shape = shape;
		this.stroke = stroke;
		this.color = color;
		this.fillColor = fillColor;
	}
	
	public GraphStyle(final Shape shape, final Stroke stroke, final Color color) {
		this(shape, stroke, color, null);
	}
	
	public Shape getShape() {
		return shape;
	}

	public Stroke getStroke() {
		return stroke;
	}

	public Color getColor() {
		return color;
	}

	public Color getFillColor() {
		return fillColor;
	}
}