package org.geogebra.common.kernel.interval;

import java.util.List;

import org.geogebra.common.kernel.geos.GeoFunction;

public class IntervalFunctionEvaluator {

	private final GeoFunction function;
	private final int numberOfSamples;
	private Interval range;
	private final LinearSpace space;

	public IntervalFunctionEvaluator(GeoFunction function, Interval range, int numberOfSamples) {
		this.function = function;
		this.numberOfSamples = numberOfSamples;
		space = new LinearSpace();
		update(range);
	}

	public IntervalTupleList result() {
		return interval1d();
	}

	private IntervalTupleList interval1d() {
		List<Double> xCoords = space.values();
		IntervalTupleList samples = new IntervalTupleList();
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
//		detectAsimptote(samples);
		samples.setDeltaX(space.getScale());
		return samples;

	}

	private void detectAsimptote(IntervalTupleList samples) {
		double yMin = range.getLow();
		double yMax = range.getHigh();
		for (int i = 1; i < samples.size() - 1; i++) {
			if (samples.get(i) != null) {
				IntervalTuple prev = samples.get(i - 1);
      			IntervalTuple next = samples.get(i + 1);
				if (prev != null && next != null && !prev.y().isOverlap(next.y())) {
					if (prev.y().getLow() > next.y().getHigh()) {
						prev.y().set(Math.max(yMax, prev.y().getHigh()),
								Math.min(yMin, next.y().getLow()));
					}

					if (prev.y().getHigh() < next.y().getLow()) {
						prev.y().set(Math.min(yMin, prev.y().getLow()),
								Math.max(yMax, next.y().getHigh()));
					}
				}
			}
		}
	}

	private Interval evaluate(Interval x) {
		return new Interval((new Interval(x)).pow(4)).sin();
	}

	public void update(Interval range) {
		this.range = range;
		space.update(range, numberOfSamples);

	}
}
