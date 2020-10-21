package org.geogebra.common.kernel.interval;

public interface IntervalMiscOperands {
	/**
	 * Gives e^x where e is the euclidean constant
	 *
	 * @return e^x
	 */
	Interval exp();

	/**
	 *
	 * @return the natural logarithm of the interval.
	 */
	Interval log();

	/**
	 *
	 * @return base 10 logarithm of the interval
	 */
	Interval log10();

	/**
	 *
	 * @return base 2 logarithm of the interval
	 */
	Interval log2();
}
