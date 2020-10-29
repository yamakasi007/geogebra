package org.geogebra.common.kernel.interval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IntervalTupleList implements Iterable<IntervalTuple> {
	private List<IntervalTuple> list;
	private double deltaX;

	public IntervalTupleList() {
		this.list = new ArrayList<>();
	}

	public void add(IntervalTuple tuple) {
		list.add(tuple);
	}

	public IntervalTuple get(int index) {
		return list.get(index);
	}

	@Override
	public Iterator<IntervalTuple> iterator() {
		return list.iterator();
	}

	public int size() {
		return list.size();
	}

	public double getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(double deltaX) {
		this.deltaX = deltaX;
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}
}
