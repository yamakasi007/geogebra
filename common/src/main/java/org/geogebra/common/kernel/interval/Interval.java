package org.geogebra.common.kernel.interval;

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
	public void add(Interval other) {
		low += other.low;
		high += other.high;
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

	public void sub(Interval argument) {
		low -= argument.high;
		high -= argument.low;
	}
}
