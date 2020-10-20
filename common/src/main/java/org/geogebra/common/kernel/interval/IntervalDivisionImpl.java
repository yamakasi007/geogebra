package org.geogebra.common.kernel.interval;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.geogebra.common.kernel.interval.IntervalConstants.WHOLE;
import static org.geogebra.common.kernel.interval.IntervalConstants.ZERO;

public class IntervalDivisionImpl implements IntervalDivision {
	private Interval interval;

	public IntervalDivisionImpl(Interval interval) {
		this.interval = interval;
	}

	@Override
	public Interval divide(Interval other) {
		if (interval.isEmpty() || other.isEmpty()) {
			interval.setEmpty();
			return interval;
		}

		if (other.hasZero()) {
			if (other.getLow() != 0) {
				if (other.getHigh() != 0) {
					return zero();
				} else {
					return negative(other.getLow());
				}
			} else {
				if (other.getHigh() != 0) {
					return positive(other.getHigh());
				} else {
					interval.setEmpty();
				}
			}
		} else {
			return nonZero(other);
		}
		return interval;
	}

	private Interval nonZero(Interval other) {
		double xl = interval.getLow();
  		double xh = interval.getHigh();
  		double yl = other.getLow();
  		double yh = other.getHigh();
		if (xh < 0) {
			if (yh < 0) {
				interval.set(RMath.divLow(xh, xl), RMath.divHigh(xl, yh));
			} else {
				interval.set(RMath.divLow(xl, yl), RMath.divHigh(xh, yh));
			}
		} else if (xl < 0) {
			if (yh < 0) {
				interval.set(RMath.divLow(xh, yh), RMath.divHigh(xl, yh));
			} else {
				interval.set(RMath.divLow(xl, yl), RMath.divHigh(xh, yl));
			}
		} else {
			if (yh < 0) {
				interval.set(RMath.divLow(xh, yh), RMath.divHigh(xl, yl));
			} else {
				interval.set(RMath.divLow(xl, yh), RMath.divHigh(xh, yl));
			}
		}
		return interval;
	}

	private Interval positive(double x) {
		if (interval.equals(ZERO)) {
			return interval;
		}

		if (interval.hasZero()) {
			return WHOLE;
		}

		if (interval.getHigh() < 0) {
			interval.set(NEGATIVE_INFINITY, RMath.divHigh(interval.getHigh(), x));
		} else {
			interval.set(RMath.divLow(interval.getLow(), x), POSITIVE_INFINITY);
		}
		return interval;
	}

	private Interval negative(double x) {
		if (interval.equals(ZERO)) {
			return interval;
		}

		if (interval.hasZero()) {
			return WHOLE;
		}

		if (interval.getHigh() < 0) {
			interval.set(RMath.divLow(interval.getHigh(), x), POSITIVE_INFINITY);
		} else {
			interval.set(NEGATIVE_INFINITY, RMath.divHigh(interval.getLow(), x));
		}
		return interval;
	}

	private Interval zero() {
		if (interval.equals(ZERO)) {
			return interval;
		}
		interval.setWhole();
		return interval;
	}
}
