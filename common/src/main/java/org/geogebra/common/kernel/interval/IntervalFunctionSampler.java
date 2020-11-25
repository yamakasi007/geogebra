package org.geogebra.common.kernel.interval;

import java.util.List;

import org.geogebra.common.kernel.geos.GeoFunction;

/**
 * Class to provide samples of the given function as a
 * list of (x, y) pairs, where both x and y are intervals.
 *
 * @author Laszlo
 */
public class IntervalFunctionSampler {

	private final IntervalFunction function;
	private final int numberOfSamples;
	private final LinearSpace space;

	/**
	 *
	 * @param geo function to get sampled
	 * @param range (x, y) range.
	 * @param numberOfSamples the sample rate.
	 */
	public IntervalFunctionSampler(GeoFunction geo, IntervalTuple range,
			int numberOfSamples) {
		this.function = new IntervalFunction(geo);
		this.numberOfSamples = numberOfSamples;
		space = new LinearSpace();
		update(range);
	}

	/**
	 * Gets the samples with the predefined range and sample rate
	 *
	 * @return the sample list
	 */
	public IntervalTupleList result() {
		try {
			return evaluateOnSpace(space);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new IntervalTupleList();
	}

	/**
	 * Gets the samples with the predefined range and sample rate
	 *
	 * @return the sample list
	 */
	public IntervalTupleList result(LinearSpace space) {
		try {
			return evaluateOnSpace(space);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new IntervalTupleList();
	}

	private IntervalTupleList evaluateOnSpace(LinearSpace space) throws Exception {
		List<Double> xCoords = space.values();
		IntervalTupleList samples = new IntervalTupleList();
		if (xCoords.size() == 1) {
			Interval x = new Interval(xCoords.get(0));
			Interval y = function.evaluate(x);
			samples.add(new IntervalTuple(x, y));
			return samples;
		}

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

		return samples;
	}

	/**
	 * Updates the range on which sampler has to run.
	 *
	 * @param range the new (x, y) range
	 */
	public void update(IntervalTuple range) {
		space.update(range.x(), numberOfSamples);
	}

	public IntervalTupleList extendTo(double max) {
		return evaluateAtDomain(space.extendKeepSize(max));
	}

	private IntervalTupleList evaluateAtDomain(LinearSpace domain) {
		try {
			return evaluateOnSpace(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public IntervalTupleList shrinkTo(double min) {
		return evaluateAtDomain(space.shrinkKeepSize(min));
	}
}
