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

	public LinearSpace(int start, int end, int count) {
		this();
		update(new Interval(start, end), count);
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
		step = interval.getLength() / count;
		fill(interval.getLow(), interval.getHigh(), step);
		scale = size() > 2 ? values.get(1) - values.get(0) : 0;
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

	public LinearSpace appendKeepSize(double delta) {
		LinearSpace result = new LinearSpace();
		double max = getLastValue() + Math.max(delta, step) + step;
		double t = getLastValue() + step;
		while (t <= max) {
			result.values.add(t);
			values.remove(0);
			values.add(t);
			t += step;
		}
		return result;
	}

	private double getLastValue() {
		return values.get(size() - 1);
	}

	private int size() {
		return values.size();
	}

	public LinearSpace prependKeepSize(double delta) {
		LinearSpace result = new LinearSpace();
		double min = getFirstValue() - Math.max(delta, step);
		double t = getFirstValue();
		while (min < t) {
			t -= step;
			result.values.add(0, t);
			values.remove(size() - 1);
			values.add(0, t);
		}
		return result;
	}

	private double getFirstValue() {
		return values.isEmpty() ? 0 : values.get(0);
	}

	private double getDiff(double delta) {
		double m = Math.floor(delta / step) + 1;
		double diff = (m * step) + step;
	//	Log.debug("[DIFF] step: " + step + " delta: " + delta + " result: " + diff);
		return diff;
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

}
