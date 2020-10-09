package org.geogebra.common.kernel.interval;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Interval {
	private double low;
	private double high;

	public Interval(double low, double high) {
		this.low = low;
		this.high = high;
	}

	/**
	 * Interval addition
	 *
	 * @param other interval to add
	 */
	public Interval add(Interval other) {
		low += other.low;
		high += other.high;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Interval interval = (Interval) o;
		return Double.compare(interval.low, low) == 0 &&
				Double.compare(interval.high, high) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(low, high);
	}

	@Override
	public String toString() {
		return "Interval [" +
				low +
				", "
				+ high +
				']';
	}

	public Interval sub(Interval other) {
		low -= other.high;
		high -= other.low;
		return this;
	}

	public Interval multiply(Interval other) {
		getBoundsFromList(Arrays.asList(low * other.low, low * other.high, high * other.low,
				high * other.high));
		return this;
	}

	protected void getBoundsFromList(List<Double> list) {
		Collections.sort(list);

		// to avoid -0.0, see testMultiplication5
		low = list.get(0) + 0.0;
		high = list.get(3) + 0.0;
	}

	public Interval div(Interval other) throws IntervalDivisionByZero {
		if (other.hasZero()) {
			throw new IntervalDivisionByZero();
		}
		getBoundsFromList(Arrays.asList(low / other.low, low / other.high, high / other.low,
				high / other.high));
		return this;
	}

	private boolean hasZero() {
		return low <= 0 && high >= 0;
	}
}
