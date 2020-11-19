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
	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public void append(IntervalTupleList tuples) {
		List<IntervalTuple> tupleList = tuples.list;
		this.list.addAll(tupleList);
		this.list = list.subList(tupleList.size() - 1, list.size() - 1);
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
		for(IntervalTuple point: list) {
			sb.append("( x: ");
			sb.append(point.x().toShortString());
//			sb.append(" y:");
//			sb.append(point.y().toShortString());
			sb.append(")");
		}
		return sb.toString();
	}
}
