package org.geogebra.common.kernel.interval;

import static org.apache.commons.math3.util.FastMath.nextAfter;

/**
 * Utility class to determine the previous/next numbers
 * for algebra functions.
 *
 * @author Laszlo
 */
public class RMath {

	/**
	 *
	 * @param v reference number
	 * @return previous number of v
	 */
	public static double prev(double v) {
		if (v == Double.POSITIVE_INFINITY) {
			return v;
		}
		return nextAfter(v, Double.NEGATIVE_INFINITY);
	}

	/**
	 *
	 * @param v reference number
	 * @return next number of v
	 */
	public static double next(double v) {
		if (v == Double.NEGATIVE_INFINITY) {
			return v;
		}
		return nextAfter(v, Double.POSITIVE_INFINITY);
	}

	/**
	 *
	 * @param x nominator
	 * @param y denominator
	 * @return the previous number of x/y
	 */
	public static double divLow(double x, double y) {
		return prev(x / y);
	}

	/**
	 *
	 * @param x nominator
	 * @param y denominator
	 * @return the next number of x/y
	 */
	public static double divHigh(double x, double y) {
		return next(x / y);
	}

	/**
	 *
	 * @param x argument
	 * @param y argument
	 * @return the previous number of x * y
	 0*/
	public static double mulLow(double x, double y) {
		return prev(x * y);
	}


	/**
	 *
	 * @param x argument
	 * @param y argument
	 * @return the next number of x * y
	 */
	public static double mulHigh(double x, double y) {
		return next(x * y);
	}

	/**
	 *
	 * @param x any double.
	 * @param power to raise of.
	 * @return the previous number of x^{power}
	 */
	public static double powLow(double x, double power) {
		if (power % 1 != 0) {
			// power has decimals
			return prev(Math.pow(x, power));
		}

		int n = (int)power;
		double y = (n & 1) == 1 ? x : 1;
		n >>= 1;
		while (n > 0) {
			x = mulLow(x, x);
			if ((n & 1) == 1) {
				y = mulLow(x, y);
			}
			n >>= 1;
		}
		return y;
	}

	/**
	 *
	 * @param x any double.
	 * @param power to raise of.
	 * @return the next number of x^{power}
	 */
	public static double powHigh(double x, double power) {
		if (power % 1 != 0) {
			// power has decimals
			return next(Math.pow(x, power));
		}

		int n = (int)power;
		double y = (n & 1) == 1 ? x : 1;
		n >>= 1;
		while (n > 0) {
			x = mulHigh(x, x);
			if ((n & 1) == 1) {
				y = mulHigh(x, y);
			}
			n >>= 1;
		}
		return y;
	}
}
