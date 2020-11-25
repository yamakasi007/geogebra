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
	private Interval oldDomain;
	private Interval domain;

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
		range.set(view.domain(), view.range());
		oldDomain = view.domain();
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

	public void moveDomain(Interval domain, double dx) {
		this.domain = domain;
		double deltaX = dx / view.getXscale();
		if (deltaX < 0) {
			Log.debug("append");
			IntervalTupleList tuples = sampler.append(-deltaX);
			points.append(tuples);
		} else {
			Log.debug("prepend");
			IntervalTupleList tuples = sampler.prepend(deltaX);
			points.prepend(tuples);
		}
		info(dx, deltaX);
		oldDomain = this.domain;
	}

	private void info(double dx, double deltaX) {
		Log.debug("view: " + view.domain() + " pts: " + points.domain());
		Log.debug("dx: " + dx + " rw " + deltaX + " cnt: " + points.count());
	}
}
