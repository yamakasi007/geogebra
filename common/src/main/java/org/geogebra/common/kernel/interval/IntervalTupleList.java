package org.geogebra.common.kernel.interval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * List to hold IntervalTuples
 *
 * @author laszlo
 */
public class IntervalTupleList implements Iterable<IntervalTuple> {
	private List<IntervalTuple> list;

	/**
	 * Constructor.
	 */
	public IntervalTupleList() {
		this.list = new ArrayList<>();
	}

	/**
	 *
	 * @param tuple to add
	 */
	public void add(IntervalTuple tuple) {
		list.add(tuple);
	}

	/**
	 *
	 * @param index of tuple to get.
	 * @return the tuple on the given index.
	 */
	public IntervalTuple get(int index) {
		return list.get(index);
	}

	@Override
	public Iterator<IntervalTuple> iterator() {
		return list.iterator();
	}

	/**
	 *
	 * @return the size of the list
	 */
	public int count() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * Adds points to the tail of the list and removes
	 * from the head as many as added to keep the size unchanged.
	 *
	 * @param newPoints to append
	 */
	public void appendKeepingSize(IntervalTupleList newPoints) {
		List<IntervalTuple> tupleList = newPoints.list;
		if (newPoints.isEmpty()) {
			return;
		}

		this.list.addAll(tupleList);
		list = list.subList(tupleList.size(), list.size());
	}

	/**
	 * Adds points to the head of the list and removes
	 * from the tail as many as added to keep size unchanged.
	 *
	 * @param newPoints to append
	 */
	public void prependKeepingSize(IntervalTupleList newPoints) {
		list.addAll(0, newPoints.list);
		int lastIndex = newPoints.count() < list.size() ? list.size() - newPoints.count() : 0;
		list = list.subList(0, lastIndex);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IntervalTupleList) {
			IntervalTupleList other = (IntervalTupleList) obj;
			return list.equals(other.list);
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (IntervalTuple point: list) {
			sb.append(point.toString());
		}
		return sb.toString();
	}

	public Interval domain() {
		return new Interval(list.get(0).x().getLow(), list.get(list.size() - 1).x().getHigh());
	}

	public void remove(IntervalTuple tuple) {
		list.remove(tuple);
	}

	public void remove(int index) {
		list.remove(index);
	}
}
