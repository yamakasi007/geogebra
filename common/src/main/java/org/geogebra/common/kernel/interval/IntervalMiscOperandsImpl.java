package org.geogebra.common.kernel.interval;

public class IntervalMiscOperandsImpl implements IntervalMiscOperands {
	public static final Interval LOG_EXP_10 = new Interval(10, 10).log();
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

	@Override
	public Interval log() {
		if (!interval.isEmpty()) {
			double low = interval.getLow();
			interval.set(low <= 0 ? Double.NEGATIVE_INFINITY : RMath.logLow(low),
					RMath.logHigh(interval.getHigh()));
		}
		return interval;
	}

	@Override
	public Interval log10() {
		if (!interval.isEmpty()) {
			interval.log().divide(LOG_EXP_10);
		}

		return interval;
	}
}
