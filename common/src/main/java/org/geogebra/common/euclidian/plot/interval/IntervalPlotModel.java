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
		range.set(view.domain(), view.getYInterval());
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

	public void moveDomain() {
		Interval domain = view.domain();
		double deltaX = oldDomain.getLow() - domain.getLow();
		if (deltaX < 0) {
			IntervalTupleList tuples = sampler.append(-deltaX);
			points.append(tuples);
		} else {
			IntervalTupleList tuples = sampler.prepend(deltaX);
			points.prepend(tuples);
		}
//		filter(points, deltaX);

		Log.debug("view domain: " + domain.toShortString());
		Log.debug("oldw domain: " + oldDomain.toShortString());
		Log.debug("deltaX: " + deltaX);
		oldDomain = domain;
	}

	private void filter(IntervalTupleList tuples, double threshold) {
		for (IntervalTuple tuple: tuples) {
			if (tuple.x().getHigh() < view.getXmin() - threshold
					|| tuple.x().getLow() > view.getXmax() + threshold) {
				tuples.remove(tuple);
//				Log.debug(tuple + " removed.");
			}
		}
	}
}
