package org.geogebra.common.kernel.interval;

public class IntervalTuple {
	private final Interval x;
	private final Interval y;

	public IntervalTuple(Interval x, Interval y) {
		this.x = x;
		this.y = y;
	}

	public Interval x() {
		return x;
	}

	public Interval y() {
		return y;
	}
}
