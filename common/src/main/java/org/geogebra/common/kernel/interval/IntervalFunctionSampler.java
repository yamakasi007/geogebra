package org.geogebra.common.kernel.interval;

import java.util.List;

import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.util.debug.Log;

public class IntervalFunctionSampler {

	private final IntervalFunction function;
	private final int numberOfSamples;
	private IntervalTuple range;
	private final LinearSpace space;
	private IntervalTupleList oldSamples = new IntervalTupleList();

	public IntervalFunctionSampler(GeoFunction geo, IntervalTuple range,
			int numberOfSamples) {
		this.function = new IntervalFunction(geo);
		this.range = range;
		this.numberOfSamples = numberOfSamples;
		space = new LinearSpace();
		update(range);
	}

	public IntervalTupleList result() {
		try {
			return interval1d();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new IntervalTupleList();
	}

	private IntervalTupleList interval1d() throws Exception {
		List<Double> xCoords = space.values();
		IntervalTupleList samples = new IntervalTupleList();
		for (int i = 0; i < xCoords.size() - 1; i += 1) {
			Interval x = new Interval(xCoords.get(i), xCoords.get(i + 1));
			Interval y = function.evaluate(x);

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
		oldSamples = samples;
		return samples;

	}

	private void detectAsimptote(IntervalTupleList samples) {
		double yMin = range.y().getLow();
		double yMax = range.y().getHigh();
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

	public void update(IntervalTuple range) {
		this.range = range;
		space.update(range.x(), numberOfSamples);

	}

	public void resample(IntervalTuple range, int numberOfSamples) {
		if (this.range.x().getWidth() == range.x().getWidth())  {
			Log.debug("[SAMPLER] NO resampling");
		} else {
			Log.debug("[SAMPLER] Resampling");
		}
	}
}
