package org.geogebra.common.euclidian.plot.interval;

import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.kernel.interval.Interval;
import org.geogebra.common.kernel.interval.IntervalFunctionSampler;
import org.geogebra.common.kernel.interval.IntervalTuple;
import org.geogebra.common.kernel.interval.IntervalTupleList;
import org.geogebra.common.util.debug.Log;

public class IntervalPlotModel {
	private final IntervalTuple range;
	private final IntervalFunctionSampler sampler;
	private IntervalTupleList points;
	private IntervalPath path;
	private final EuclidianView view;

	public IntervalPlotModel(IntervalTuple range,
			IntervalFunctionSampler sampler,
			EuclidianView view) {
		this.range = range;
		this.sampler = sampler;
		this.view = view;
	}

	public void setPath(IntervalPath path) {
		this.path = path;
	}

	public void updateAll() {
		updateRanges();
		updateSampler();
		updatePath();
	}

	private void updateRanges() {
		range.x().set(view.getXmin(), view.getXmax());
		range.y().set(view.getYmin(), view.getYmax());
	}

	void updateSampler() {
		sampler.update(range);
		points = sampler.result();
	}

	public boolean isEmpty() {
		return points.isEmpty();
	}

	public IntervalTupleList getPoints() {
		return points;
	}

	public void updatePath() {
		path.update();
	}

	public void moveXBy(double deltaX) {
		updateRanges();
		if (deltaX < 0) {
			IntervalTupleList tuples = sampler.append(-deltaX);
			points.append(tuples);
		} else {
			IntervalTupleList tuples = sampler.prepend(deltaX);
			points.prepend(tuples);
		}
		Interval viewX = new Interval(view.getXmin(), view.getXmax());
		Log.debug("range check: viewX:  " + viewX + " domain" + points.domain()	);
	}
}
