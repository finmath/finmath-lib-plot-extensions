/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christianfries.com.
 *
 * Created on 15 Feb 2020
 */
package net.finmath.plots;

import net.finmath.stochastic.RandomVariable;

/**
 * Definition of a function that maps a Double to a RandomVariable, with the possibility
 * to throw an Exception.
 *
 * @author Christian Fries
 */
@FunctionalInterface
public interface DoubleToRandomVariableFunction {

	/**
	 * Applies this function to the given argument.
	 *
	 * @param value the function argument
	 * @return the function result
	 * @throws Exception Thrown if function evaluation fails
	 */
	RandomVariable apply(double value) throws Exception;

}
