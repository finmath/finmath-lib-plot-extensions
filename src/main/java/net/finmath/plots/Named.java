/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 24 May 2018
 */

package net.finmath.plots;

/**
 * A named object of type T.
 *
 * @author Christian Fries
 */
public class Named<T extends Object> {

	private final String name;
	private final T object;

	/**
	 * Create the named object.
	 *
	 * @param name Name of the object.
	 * @param object The object.
	 */
	public Named(final String name, final T object) {
		super();
		this.name = name;
		this.object = object;
	}

	/**
	 * Get the name of the object.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the object.
	 *
	 * @return the object.
	 */
	public T get() {
		return object;
	}
}
