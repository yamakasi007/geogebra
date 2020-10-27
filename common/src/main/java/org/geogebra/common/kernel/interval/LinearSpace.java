package org.geogebra.common.kernel.interval;

import java.util.ArrayList;
import java.util.List;

public class LinearSpace {
	public List<Double> values;
	public LinearSpace(Interval interval, int count) {
		values = new ArrayList<>();
		fill(interval.getLow(), interval.getHigh(), interval.getWidth() / count);
	}

	private void fill(double start, double end, double step) {
		double current = start;
		while (current < end + step) {
			values.add(current);
			current += step;
		}
	}

	public List<Double> values() {
		return values;
	}
}
