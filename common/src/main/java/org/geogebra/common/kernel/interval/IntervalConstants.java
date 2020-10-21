package org.geogebra.common.kernel.interval;

public class IntervalConstants {
	public final static Interval WHOLE = new Interval(Double.NEGATIVE_INFINITY,
			Double.POSITIVE_INFINITY);
	public static final Interval ZERO = new Interval(0);
	public static final Interval ONE = new Interval(1);
	public static final double PI_LOW =  3.141592653589793;
	public static final double PI_HIGH = 3.1415926535897936;
	public static final double PI_HALF_LOW = PI_LOW / 2.0;
	public static final double PI_HALF_HIGH = PI_HIGH / 2.0;
	public static final double PI_TWICE_LOW = PI_LOW * 2.0;
	public static final double PI_TWICE_HIGH = PI_HIGH * 2.0;
	public static final Interval PI = new Interval(PI_LOW, PI_HIGH);
	public static final Interval PI_HALF = new Interval(PI_HALF_LOW, PI_HALF_HIGH);
	public static final Interval PI_TWICE = new Interval(PI_TWICE_LOW, PI_TWICE_HIGH);
	public static Interval empty() {return new Interval(Double.POSITIVE_INFINITY,
			Double.NEGATIVE_INFINITY);}
	public static Interval zero() {return new Interval(0);}

	public static Interval whole() {
		return new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}
}
