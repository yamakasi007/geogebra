package org.geogebra.common.kernel.interval;

public interface IntervalDivision {

	/**
	 * Interval division
	 *
	 * @param other to divide this interval with.
	 * @return this as result.
	 */
	Interval divide(Interval other);
}
