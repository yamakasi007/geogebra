package org.geogebra.common.kernel.interval;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for linear space.
 *
 * @author laszlo
 */
public class LinearSpace {
	public List<Double> values;
	private double scale;
	private Interval interval;
	private int count;
	private double step;

	public LinearSpace() {
		values = new ArrayList<>();
	}

	/**
	 * Updates the space.
	 * @param interval the base interval.
	 * @param count of the interval to divide.
	 */
	public void update(Interval interval, int count) {
		this.interval = interval;
		this.count = count;
		values.clear();
		step = interval.getWidth() / count;
		fill(interval.getLow(), interval.getHigh(), step);
		scale = values.size() > 2 ? values.get(1) - values.get(0) : 0;
	}

	private void fill(double start, double end, double step) {
		double current = start;
		while (current < end + step) {
			values.add(current);
			current += step;
		}
	}

	/**
	 *
	 * @return the value list of the space.
	 */
	public List<Double> values() {
		return values;
	}

	/**
	 *
	 * @return the difference between two neighbour values.
	 */
	public double getScale() {
		return scale;
	}

	public LinearSpace shiftBy(double delta) {
		double high = this.interval.getHigh();
		Interval intervalAdded = new Interval(high, high + delta);
		double t = intervalAdded.getLow();
		count = 0;
		LinearSpace result = new LinearSpace();
		while (t < intervalAdded.getHigh()) {
			t += step;
			result.values.add(t);
			count++;
		}
		return result;
	}

	private void update() {
		update(interval, count);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LinearSpace) {
			LinearSpace other = ((LinearSpace) obj);
			return values.equals(other.values);
		}
		return false;
	}

	@Override
	public String toString() {
		return values.toString();
	}

	public void shiftLeft(double v) {
	}
}