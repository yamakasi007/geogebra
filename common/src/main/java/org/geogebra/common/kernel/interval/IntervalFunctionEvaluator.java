package org.geogebra.common.kernel.interval;

import java.util.ArrayList;
import java.util.List;

import org.geogebra.common.kernel.geos.GeoFunction;

public class IntervalFunctionEvaluator {

	private final GeoFunction function;
	private int numberOfSamples;
	private final Interval range;

	public IntervalFunctionEvaluator(GeoFunction function, Interval range, int numberOfSamples) {
		this.range = range;
		this.function = function;
		this.numberOfSamples = numberOfSamples;
	}

	public List<IntervalTuple> result() {
		return interval1d();
	}

	private List<IntervalTuple> interval1d() {
		LinearSpace space = new LinearSpace(range, numberOfSamples);
		List<Double> xCoords = space.values();
		List<IntervalTuple> samples = new ArrayList<>();
		for (int i = 0; i < xCoords.size() - 1; i += 1) {
			Interval x = new Interval(xCoords.get(i), xCoords.get(i + 1));
			Interval y = evaluate(x);

			if (!y.isEmpty() && !y.isWhole()) {
				samples.add(new IntervalTuple(x, y));
			}
			if (y.isWhole()) {
				// means that the next and prev intervals need to be fixed
				samples.add(null);
			}
		}
		return samples;

	}

	private Interval evaluate(Interval x) {
		return x.sin();
	}
}
