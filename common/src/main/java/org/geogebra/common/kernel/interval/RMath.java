package org.geogebra.common.kernel.interval;

public class RMath {
	public static double prev(double v) {
		if (v == Double.POSITIVE_INFINITY) {
			return v;
		}
		return Math.nextAfter(v, Double.NEGATIVE_INFINITY);
	}

	public static double next(double v) {
		if (v == Double.NEGATIVE_INFINITY) {
			return v;
		}
		return Math.nextAfter(v, Double.POSITIVE_INFINITY);
	}

	public static double divLo(double x, double y) {
		return prev(x / y);
	}

	public static double divHi(double x, double y) {
		return next(x / y);
	}

	public static double powLo(double x, double power) {
		if (power % 1 != 0) {
			// power has decimals
			return prev(Math.pow(x, power));
		}

		int n = (int)power;
		double y = (n & 1) == 1 ? x : 1;
		n >>= 1;
		while (n > 0) {
			x = mulLo(x, x);
			if ((n & 1) == 1) {
				y = mulLo(x, y);
			}
			n >>= 1;
		}
		return y;
	}

	public static double mulLo(double x, double y) {
		return prev(x * y);
	}

	public static double mulHi(double x, double y) {
		return next(x * y);
	}

	public static double powHi(double x, double power) {
		if (power % 1 != 0) {
			// power has decimals
			return next(Math.pow(x, power));
		}

		int n = (int)power;
		double y = (n & 1) == 1 ? x : 1;
		n >>= 1;
		while (n > 0) {
			x = mulHi(x, x);
			if ((n & 1) == 1) {
				y = mulHi(x, y);
			}
			n >>= 1;
		}
		return y;

	}
}
