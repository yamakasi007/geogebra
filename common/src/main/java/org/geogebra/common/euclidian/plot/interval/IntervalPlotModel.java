package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.kernel.interval.IntervalFunctionSampler;
import org.geogebra.common.kernel.interval.IntervalTuple;
import org.geogebra.common.kernel.interval.IntervalTupleList;

public class IntervalPlotModel {
	private IntervalTuple range;
	private IntervalFunctionSampler sampler;
	private IntervalTupleList points;
	private final EuclidianView view;

	public IntervalPlotModel(IntervalTuple range,
			IntervalFunctionSampler sampler, EuclidianView view) {
		this.range = range;
		this.sampler = sampler;
		this.view = view;
	}

	public void updateAll() {
		updateRanges();
		updateSampler();
	}

	private void updateRanges() {
		range.x().set(view.getXmin(), view.getXmax());
		range.y().set(view.getYmin(), view.getYmax());
	}

	private void updateSampler() {
		sampler.update(range);
		points = sampler.result();
	}

	public boolean isEmpty() {
		return points.isEmpty();
	}

	public IntervalTupleList getPoints() {
		return points;
	}
}
