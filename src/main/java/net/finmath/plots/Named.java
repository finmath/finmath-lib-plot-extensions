/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 24 May 2018
 */

package net.finmath.plots;

/**
 * A named object.
 * 
 * @author Christian Fries
 */
public class Named<T extends Object> {

	private final String name;
	private final T object;

	public Named(String name, T function) {
		super();
		this.name = name;
		this.object = function;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the function
	 */
	public T get() {
		return object;
	}
}
