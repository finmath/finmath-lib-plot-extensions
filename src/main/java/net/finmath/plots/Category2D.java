/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 21 May 2018
 */
package net.finmath.plots;

public class Category2D {
	private String name;
	private Number value;

	public Category2D(String name, Number value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Number getValue() {
		return value;
	}
}