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

	public void moveDomain(Interval domain) {
		this.domain = domain;
		double deltaX = oldDomain.getLow() - this.domain.getLow();
		if (deltaX < 0) {
			IntervalTupleList tuples = sampler.append(-deltaX);
			points.append(tuples);
			Log.debug(tuples);
		} else {
			IntervalTupleList tuples = sampler.prepend(deltaX);
			points.prepend(tuples);
		}
		info(deltaX);
		oldDomain = this.domain;
	}

	private void info(double deltaX) {
//		Log.debug("view domain: " + domain.toShortString());
//		Log.debug("oldw domain: " + oldDomain.toShortString());
//		Log.debug("deltaX: " + deltaX);
		Log.debug("pts  domain: " + points.domain() + " length: " + points.domain().getLength());
	}

	private void clipDomainLow() {
		double high = points.get(0).x().getHigh();
		while (high <= domain.getLow() && !points.isEmpty()) {
			points.remove(0);
			high = points.get(0).x().getHigh();
		}
	}

	private void clipDomainHigh() {
		double low = points.get(points.size() - 1).x().getLow();
		while (low >= domain.getHigh() && !points.isEmpty()) {
			points.remove(points.size() - 1);
			low = points.get(0).x().getHigh();
		}
	}
}
