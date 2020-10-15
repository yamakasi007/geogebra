package org.geogebra.common.kernel.interval;

import static org.geogebra.common.kernel.interval.IntervalConstants.PI;
import static org.geogebra.common.kernel.interval.IntervalConstants.PI_HIGH;
import static org.geogebra.common.kernel.interval.IntervalConstants.PI_LOW;
import static org.geogebra.common.kernel.interval.IntervalConstants.PI_TWICE;
import static org.geogebra.common.kernel.interval.IntervalConstants.PI_TWICE_LOW;
import static org.geogebra.common.kernel.interval.IntervalConstants.ZERO;

class IntervalTrigonometric {
	private Interval interval;

	IntervalTrigonometric(Interval interval) {
		this.interval = interval;
	}

	Interval cos() {
		if (interval.isEmpty() || interval.isOnlyInfinity()) {
			return ZERO;
		}

		Interval cache = new Interval(interval);
		Interval pi = new Interval(PI);
		Interval pi2 = new Interval(PI_TWICE);
		cache.fmod(pi2);
		if (cache.getWidth() >= PI_TWICE_LOW) {
			interval.set(-1, 1);
			return interval;
		}

		if (cache.getLow() >= PI_HIGH) {
			cache.subtract(pi);
			interval.negative();
			return interval;
		}

		double low = cache.getLow();
		double high = cache.getHigh();
  		double rlo = RMath.cosLow(high);
  		double rhi = RMath.cosHigh(low);
		// it's ensured that t.lo < pi and that t.lo >= 0
		if (high <= PI_LOW) {
			// when t.hi < pi
			// [cos(t.lo), cos(t.hi)]
			interval.set(rlo, rhi);
		} else if (high <= PI_TWICE_LOW) {
			// when t.hi < 2pi
			// [-1, max(cos(t.lo), cos(t.hi))]
			interval.set(-1, Math.max(rlo, rhi));
		} else {
			// t.lo < pi and t.hi > 2pi
			interval.set(-1, 1);
		}

		return interval;
	}
}
