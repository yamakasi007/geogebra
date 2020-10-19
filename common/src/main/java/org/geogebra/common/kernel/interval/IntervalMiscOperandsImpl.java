package org.geogebra.common.kernel.interval;

public class IntervalMiscOperandsImpl implements IntervalMiscOperands {
	private Interval interval;

	public IntervalMiscOperandsImpl(Interval interval) {
		this.interval = interval;
	}

	@Override
	public Interval exp() {
		if (!interval.isEmpty()) {
			interval.set(RMath.expLow(interval.getLow()),
					RMath.expHigh(interval.getHigh()));
		}
		return interval;
	}
}
