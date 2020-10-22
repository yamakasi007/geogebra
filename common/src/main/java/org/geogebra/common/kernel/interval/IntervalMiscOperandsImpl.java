package org.geogebra.common.kernel.interval;

public class IntervalMiscOperandsImpl implements IntervalMiscOperands {
	private static final Interval LOG_EXP_2 = new Interval(2, 2).log();
	private static final Interval LOG_EXP_10 = new Interval(10, 10).log();
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
	public Interval log2() {
		if (!interval.isEmpty()) {
			interval.log().divide(LOG_EXP_2);
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

	@Override
	public Interval hull(Interval other) {
		if (interval.isEmpty() && other.isEmpty()) {
			interval.setEmpty();
 		} else if (interval.isEmpty()) {
			interval.set(other);
		} else if (other.isEmpty()) {
			// nothing
		} else {
			interval.set(Math.min(interval.getLow(), other.getLow()),
					Math.max(interval.getHigh(), other.getHigh()));
		}

		return interval;
	}

	@Override
	public Interval intersection(Interval other) {
		if (interval.isEmpty() || other.isEmpty()) {
			interval.setEmpty();
		} else {
			double low = Math.max(interval.getLow(), other.getLow());
			double high = Math.min(interval.getHigh(), other.getHigh());
			if (low <= high) {
				interval.set(low, high);
			} else {
				interval.setEmpty();
			}
		}
		return interval;
	}

	@Override
	public Interval union(Interval other) throws IntervalsNotOverlapException {
		if (!interval.isOverlap(other)) {
			throw new IntervalsNotOverlapException();
		}
		interval.set(Math.min(interval.getLow(), other.getLow()),
				Math.max(interval.getHigh(), other.getHigh()));
		return interval;
	}
}
