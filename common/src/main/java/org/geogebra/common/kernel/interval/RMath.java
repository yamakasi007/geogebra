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
}
