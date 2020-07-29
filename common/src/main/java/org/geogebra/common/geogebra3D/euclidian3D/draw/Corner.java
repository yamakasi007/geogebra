package org.geogebra.common.geogebra3D.euclidian3D.draw;


import static org.geogebra.common.geogebra3D.euclidian3D.draw.DrawSurface3D.*;

import org.geogebra.common.geogebra3D.euclidian3D.openGL.PlotterSurface;
import org.geogebra.common.kernel.matrix.Coords3;
import org.geogebra.common.kernel.matrix.CoordsDouble3;
import org.geogebra.common.util.DoubleUtil;

class Corner {
	Coords3 p;
	Coords3 normal;
	private double u;
	double v;
	boolean isNotEnd;
	Corner a; // above
	Corner l; // left
	int id;
	private DrawSurface3D drawSurface3D;

	// number of split for boundary
	private static final short BOUNDARY_SPLIT = 10;

	Corner(int id, DrawSurface3D drawSurface3D) {
		this.id = id;
		this.drawSurface3D = drawSurface3D;
	}

	Corner(double u, double v, int id, DrawSurface3D drawSurface3D) {
		this.id = id;
		this. drawSurface3D = drawSurface3D;
		set(u, v);
	}

	public void set(double u, double v) {
		this.u = u;
		this.v = v;
		p = drawSurface3D.evaluatePoint(u, v, p);
		if (p.isFinalUndefined()) {
			normal = Coords3.UNDEFINED;
		} else {
			normal = drawSurface3D.evaluateNormal(p, u, v, normal);
		}
		isNotEnd = true;
		a = null;
		l = null;
	}

	public void set(Corner c) {
		u = c.u;
		v = c.v;
		p = c.p;
		normal = c.normal;
		id = c.id;
	}

	public Corner(double u, double v, Coords3 p) {
		set(u, v, p);
	}

	public void set(double u, double v, Coords3 p) {
		this.u = u;
		this.v = v;
		this.p = p;
		normal = drawSurface3D.evaluateNormal(p, u, v, normal);

		isNotEnd = true;
		a = null;
		l = null;
	}

	/**
	 * draw this corner as part of "next to split" list
	 *
	 * @param surface
	 *            surface plotter
	 */
	void drawAsNextToSplit(PlotterSurface surface) {
		drawAsStillToSplit(surface);
	}

	/**
	 * draw this corner as part of "still to split" list
	 *
	 * @param surface
	 *            surface plotter
	 */
	void drawAsStillToSplit(PlotterSurface surface) {

		// prevent keeping old element id
		for (int i = 0; i < drawSurface3D.cornerToDrawStillToSplit.length; i++) {
			drawSurface3D.cornerToDrawStillToSplit[i].id = -1;
		}

		// create ring about corners
		int length;

		drawSurface3D.cornerForStillToSplit[0] = this;
		drawSurface3D.cornerForStillToSplit[1] = this.l;

		if (this.l.a == null) { // a split occurred
			drawSurface3D.cornerForStillToSplit[2] = this.l.l;
			drawSurface3D.cornerForStillToSplit[3] = this.l.l.a;
			length = 4;
		} else {
			drawSurface3D.cornerForStillToSplit[2] = this.l.a;
			length = 3;
		}

		if (this.a.l == null) { // a split occurred
			drawSurface3D.cornerForStillToSplit[length] = this.a.a;
			length++;
			drawSurface3D.cornerForStillToSplit[length] = this.a;
			length++;
		} else {
			drawSurface3D.cornerForStillToSplit[length] = this.a;
			length++;
		}

		// check defined and create intermediate corners if needed
		Corner previous = drawSurface3D.cornerForStillToSplit[length - 1];
		int index = 0;
		for (int i = 0; i < length; i++) {
			Corner current = drawSurface3D.cornerForStillToSplit[i];

			if (current.p.isNotFinalUndefined()) {
				if (previous.p.isFinalUndefined()) {
					// previous undefined -- current defined : create
					// intermediate
					if (DoubleUtil.isEqual(previous.u, current.u)) {
						findV(current, previous,
								drawSurface3D.cornerToDrawStillToSplit[index]);
					} else {
						findU(current, previous,
								drawSurface3D.cornerToDrawStillToSplit[index]);
					}
					index++;
				}
				// add current for drawing
				drawSurface3D.cornerToDrawStillToSplit[index].set(current);
				index++;
			} else {
				if (previous.p.isNotFinalUndefined()) {
					// previous defined -- current undefined : create
					// intermediate
					if (DoubleUtil.isEqual(previous.u, current.u)) {
						findV(previous, current,
								drawSurface3D.cornerToDrawStillToSplit[index]);
					} else {
						findU(previous, current,
								drawSurface3D.cornerToDrawStillToSplit[index]);
					}
					index++;
				}
			}

			previous = current;
		}

		if (index < 3) {
			// Log.debug("index = "+index);
			return;
		}

		Coords3 v0 = new CoordsDouble3(), n0 = new CoordsDouble3();
		setBarycenter(v0, n0, index, drawSurface3D.cornerToDrawStillToSplit);

		for (int i = 0; i < index; i++) {
			drawSurface3D.drawTriangle(surface, v0, n0,
					drawSurface3D.cornerToDrawStillToSplit[(i + 1) % index],
					drawSurface3D.cornerToDrawStillToSplit[i]);
		}

	}

	public void split(boolean draw) throws NotEnoughCornersException {

		Corner left, above, subLeft, subAbove;

		if (l.a == null) {
			left = l.l;
			subLeft = l;
		} else {
			left = l;
			subLeft = null;
		}

		if (a.l == null) {
			above = a.a;
			subAbove = a;
		} else {
			above = a;
			subAbove = null;
		}

		if (p.isFinalUndefined()) {
			if (left.p.isFinalUndefined()) {
				if (above.p.isFinalUndefined()) {
					if (left.a.p.isFinalUndefined()) {
						// all undefined: nothing to draw /0/
						drawSurface3D.notDrawn++;
					} else {
						// l.a is defined /1/
						// find defined between l.a and a
						Corner n = drawSurface3D.newCorner();
						findU(left.a, above, n);
						// find defined between l.a and l
						Corner w = drawSurface3D.newCorner();
						findV(left.a, left, w);
						boolean split;

						if (draw) { // time to draw
							split = false;
						} else if (n.p.isFinalUndefined()
								|| w.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else {
							// check distance
							double d = drawSurface3D.getDistance(left.a, n, w);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, left.a, n, w);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// new neighbors
							n.l = left.a;
							above.l = n;
							// new neighbors
							w.a = left.a;
							left.a = w;

							// draw
							addToDrawList(w.a, n, w, w.a);
						}
					}
				} else {
					if (left.a.p.isFinalUndefined()) {
						// a defined /1/
						// find defined between a and l.a
						Corner n = drawSurface3D.newCorner();
						findU(above, left.a, n);
						// find defined between a and this
						Corner e;
						if (subAbove != null) {
							e = subAbove;
						} else {
							e = drawSurface3D.newCorner();
							findV(above, this, e);
						}
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (n.p.isFinalUndefined()
								|| e.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else {
							double d = drawSurface3D.getDistance(above, n, e);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, above, n, e);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// new neighbors
							if (subAbove == null) {
								this.a = e;
								e.a = above;
							}
							n.l = left.a;
							above.l = n;

							// drawing
							addToDrawList(left.a, n, e, above);
						}
					} else {
						// a and l.a defined /2/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (subAbove != null
								&& subAbove.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else {
							double d = drawSurface3D.getDistance(above, left.a);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, above, left.a);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between a and this
							Corner e;
							if (subAbove != null) {
								e = subAbove;
							} else {
								e = drawSurface3D.newCorner();
								findV(above, this, e);
							}
							// find defined between l.a and left
							Corner w = drawSurface3D.newCorner();
							findV(left.a, left, w);

							if (!draw) {
								// check distances
								double d = drawSurface3D.getDistanceNoLoop(above, e, w,
										left.a);
								if (Double.isInfinite(d)) { // d >
									// maxRWDistance
									split = true;
								} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
									split = !isAngleOKNoLoop(drawSurface3D.maxBend, above, e,
													w, left.a);
								} else { // no need to check angle
									split = false;
								}
							}

							if (split) {
								split(subLeft, left, subAbove, above);
							} else {
								if (subAbove == null) {
									// new neighbors
									this.a = e;
									e.a = above;
								}
								// new neighbors
								w.a = left.a;
								left.a = w;

								// drawing
								addToDrawList(w.a, e, above, left.a, w);
							}
						}
					}
				}
			} else {
				if (above.p.isFinalUndefined()) {
					if (left.a.p.isFinalUndefined()) {
						// l defined /1/
						// find defined between l and this
						Corner s;
						if (subLeft != null) {
							s = subLeft;
						} else {
							s = drawSurface3D.newCorner();
							findU(left, this, s);
						}
						// find defined between l and l.a
						Corner w = drawSurface3D.newCorner();
						findV(left, left.a, w);
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (s.p.isFinalUndefined()
								|| w.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else {
							double d = drawSurface3D.getDistance(left, s, w);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, left, s, w);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// new neighbors
							if (subLeft == null) {
								this.l = s;
								s.l = left;
							}
							w.a = left.a;
							left.a = w;

							// drawing
							addToDrawList(w.a, s, w, left);

						}
					} else {
						// l and l.a defined /2/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (subLeft != null
								&& subLeft.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else { // check distance
							double d = drawSurface3D.getDistance(left.a, left);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, left.a, left);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between l and this
							Corner s;
							if (subLeft != null) {
								s = subLeft;
							} else {
								s = drawSurface3D.newCorner();
								findU(left, this, s);
							}
							// find defined between l.a and a
							Corner n = drawSurface3D.newCorner();
							findU(left.a, above, n);

							if (!draw) {
								// check distances
								double d = drawSurface3D.getDistanceNoLoop(left.a, n, s,
										left);
								if (Double.isInfinite(d)) { // d >
									// maxRWDistance
									split = true;
								} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
									split = !isAngleOKNoLoop(drawSurface3D.maxBend, left.a, n,
													s, left);
								} else { // no need to check angle
									split = false;
								}
							}

							if (split) {
								split(subLeft, left, subAbove, above);
							} else {
								if (subLeft == null) {
									// new neighbors
									this.l = s;
									s.l = left;
								}
								// new neighbors
								n.l = left.a;
								above.l = n;

								// drawing
								addToDrawList(left.a, s, n, left.a, left);
							}
						}
					}
				} else {
					if (left.a.p.isFinalUndefined()) {
						// l and a not undefined /2/diag/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else { // check distance
							double d = drawSurface3D.getDistance(left, above);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, left, above);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between l and this
							Corner s;
							if (subLeft == null) {
								s = drawSurface3D.newCorner();
								findU(left, this, s);
								// new neighbors
								this.l = s;
								s.l = left;
							}
							// find defined between a and this
							Corner e;
							if (subAbove == null) {
								e = drawSurface3D.newCorner();
								findV(above, this, e);
								// new neighbors
								this.a = e;
								e.a = above;
							}
							// find defined between l and l.a
							Corner w = drawSurface3D.newCorner();
							findV(left, left.a, w);
							w.a = left.a;
							left.a = w;
							// find defined between a and l.a
							Corner n = drawSurface3D.newCorner();
							findU(above, left.a, n);
							n.l = above.l;
							above.l = n;

							// drawing
							addToDrawList(w.a, left, above);

						}
					} else {
						// l, a and l.a not undefined /3/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (subLeft != null
								&& subLeft.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else if (subAbove != null
								&& subAbove.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else { // check distance
							double d = drawSurface3D.getDistance(left.a, left, above);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, left.a, left,
												above);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between l and this
							Corner s;
							if (subLeft == null) {
								s = drawSurface3D.newCorner();
								findU(left, this, s);
								// new neighbors
								this.l = s;
								s.l = left;
							}
							// find defined between a and this
							Corner e;
							if (subAbove == null) {
								e = drawSurface3D.newCorner();
								findV(above, this, e);
								// new neighbors
								this.a = e;
								e.a = above;
							}

							// drawing
							addToDrawList(left.a, left, above, left.a);
						}
					}
				}
			}
		} else {
			if (left.p.isFinalUndefined()) {
				if (above.p.isFinalUndefined()) {
					if (left.a.p.isFinalUndefined()) {
						// this defined /1/
						// find defined between this and l
						Corner s;
						if (subLeft != null) {
							s = subLeft;
						} else {
							s = drawSurface3D.newCorner();
							findU(this, left, s);
						}
						// find defined between this and a
						Corner e;
						if (subAbove != null) {
							e = subAbove;
						} else {
							e = drawSurface3D.newCorner();
							findV(this, above, e);
						}
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (s.p.isFinalUndefined()
								|| e.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else { // check distance
							double d = drawSurface3D.getDistance(this, s, e);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, this, s, e);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// new neighbors
							if (subLeft == null) {
								this.l = s;
								s.l = left;
							}
							if (subAbove == null) {
								this.a = e;
								e.a = above;
							}

							// drawing
							addToDrawList(left.a, s, e, this);
						}
					} else {
						// this and l.a not undefined /2/diag/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else { // check distance
							double d = drawSurface3D.getDistance(left.a, this);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, left.a, this);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between l and this
							Corner s;
							if (subLeft == null) {
								s = drawSurface3D.newCorner();
								findU(this, left, s);
								// new neighbors
								this.l = s;
								s.l = left;
							}
							// find defined between a and this
							Corner e;
							if (subAbove == null) {
								e = drawSurface3D.newCorner();
								findV(this, above, e);
								// new neighbors
								this.a = e;
								e.a = above;
							}
							// find defined between l and l.a
							Corner w = drawSurface3D.newCorner();
							findV(left.a, left, w);
							w.a = left.a;
							left.a = w;
							// find defined between a and l.a
							Corner n = drawSurface3D.newCorner();
							findU(left.a, above, n);
							n.l = above.l;
							above.l = n;

							// drawing
							addToDrawList(w.a, this, left.a);
						}
					}
				} else {
					if (left.a.p.isFinalUndefined()) {
						// this and a defined /2/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (subLeft != null
								&& subLeft.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else { // check distance
							double d = drawSurface3D.getDistance(this, above);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, this, above);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between this and l
							Corner s;
							if (subLeft != null) {
								s = subLeft;
							} else {
								s = drawSurface3D.newCorner();
								findU(this, left, s);
							}
							// find defined between a and l.a
							Corner n = drawSurface3D.newCorner();
							findU(above, left.a, n);

							if (!draw) {
								// check distances
								double d = drawSurface3D.getDistanceNoLoop(this, s, n,
										above);
								if (Double.isInfinite(d)) { // d >
									// maxRWDistance
									split = true;
								} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
									split = !isAngleOKNoLoop(drawSurface3D.maxBend, this, s, n,
													above);
								} else { // no need to check angle
									split = false;
								}
							}

							if (split) {
								split(subLeft, left, subAbove, above);
							} else {
								if (subLeft == null) {
									// new neighbors
									this.l = s;
									s.l = left;
								}
								// new neighbors
								n.l = left.a;
								above.l = n;

								// drawing
								addToDrawList(left.a, this, above, n, s);

							}
						}
					} else {
						// this, a and l.a defined /3/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (subLeft != null
								&& subLeft.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else { // check distance
							double d = drawSurface3D.getDistance(above, left.a, this);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, above, left.a,
												this);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between this and l
							Corner s;
							if (subLeft == null) {
								s = drawSurface3D.newCorner();
								findU(this, left, s);
								// new neighbors
								this.l = s;
								s.l = left;
							}
							// find defined between l.a and l
							Corner w = drawSurface3D.newCorner();
							findV(left.a, left, w);
							// new neighbors
							w.a = left.a;
							left.a = w;

							// drawing
							addToDrawList(w.a, above, left.a, this);
						}
					}
				}
			} else {
				if (above.p.isFinalUndefined()) {
					if (left.a.p.isFinalUndefined()) {
						// this and l defined /2/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (subAbove != null
								&& subAbove.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else { // check distance
							double d = drawSurface3D.getDistance(this, left);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, this, left);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between this and a
							Corner e;
							if (subAbove != null) {
								e = subAbove;
							} else {
								e = drawSurface3D.newCorner();
								findV(this, above, e);
							}
							// find defined between l and l.a
							Corner w = drawSurface3D.newCorner();
							findV(left, left.a, w);

							if (!draw) {
								// check distances
								double d = drawSurface3D.getDistanceNoLoop(this, e, w,
										left);
								if (Double.isInfinite(d)) { // d >
									// maxRWDistance
									split = true;
								} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
									split = !isAngleOKNoLoop(drawSurface3D.maxBend, this, e, w,
													left);
								} else { // no need to check angle
									split = false;
								}
							}

							if (split) {
								split(subLeft, left, subAbove, above);
							} else {
								if (subAbove == null) {
									// new neighbors
									this.a = e;
									e.a = above;
								}
								// new neighbors
								w.a = left.a;
								left.a = w;

								// drawing
								addToDrawList(w.a, this, e, w, left);
							}
						}
					} else {
						// this, l and l.a not undefined /3/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else if (subAbove != null
								&& subAbove.p.isFinalUndefined()) { // some
							// undefined
							// point:
							// force
							// split
							split = true;
						} else { // check distance
							double d = drawSurface3D.getDistance(left, left.a, this);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, left, left.a,
												this);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between l.a and a
							Corner n = drawSurface3D.newCorner();
							findU(left.a, above, n);
							// find defined between this and a
							Corner e;
							if (subAbove == null) {
								e = drawSurface3D.newCorner();
								findV(this, above, e);
								// new neighbors
								this.a = e;
								e.a = above;
							}
							// new neighbors
							n.l = left.a;
							above.l = n;

							// drawing
							addToDrawList(left.a, left, left.a, this);
						}
					}
				} else {
					if (left.a.p.isFinalUndefined()) {
						// this, l and a not undefined /3/
						boolean split;
						if (draw) { // time to draw
							split = false;
						} else { // check distance
							double d = drawSurface3D.getDistance(this, left, above);
							if (Double.isInfinite(d)) { // d > maxRWDistance
								split = true;
							} else if (d > drawSurface3D.maxRWDistanceNoAngleCheck) { // check
								split = !isAngleOK(drawSurface3D.maxBend, this, left, above);
							} else { // no need to check angle
								split = false;
							}
						}
						if (split) {
							split(subLeft, left, subAbove, above);
						} else {
							// find defined between a and l.a
							Corner n = drawSurface3D.newCorner();
							findU(above, left.a, n);
							// find defined between l and l.a
							Corner w = drawSurface3D.newCorner();
							findV(left, left.a, w);
							// new neighbors
							n.l = left.a;
							above.l = n;
							// new neighbors
							w.a = left.a;
							left.a = w;

							// drawing
							addToDrawList(w.a, this, left, above);

						}
					} else {
						// this, l, a and l.a defined /4/
						if (draw) {
							// drawing
							addToDrawList(left.a, this, left, above,
									left.a);
						} else {
							// check distances
							double d = drawSurface3D.getDistance(this, left, above,
									left.a);
							if (Double.isInfinite(d)
									|| (d > drawSurface3D.maxRWDistanceNoAngleCheck
									&& !isAngleOK(drawSurface3D.maxBend, this,
									left, above, left.a))) {
								split(subLeft, left, subAbove, above);
							} else {
								// drawing
								addToDrawList(left.a, this, left, above,
										left.a);
							}
						}
					}
				}
			}
		}

	}

	private void split(Corner subLeft, Corner left, Corner subAbove,
			Corner above) throws NotEnoughCornersException {
		// new corners
		double um = (u + left.u) / 2;
		double vm = (v + above.v) / 2;
		if (subLeft != null) {
			um = subLeft.u;
		}
		if (subAbove != null) {
			vm = subAbove.v;
		}
		Corner e;
		if (subAbove != null) {
			e = subAbove;
		} else {
			e = drawSurface3D.newCorner(u, vm);
			// new neighbors
			this.a = e;
			e.a = above;
		}
		Corner s;
		if (subLeft != null) {
			s = subLeft;
		} else {
			s = drawSurface3D.newCorner(um, v);
			// new neighbors
			this.l = s;
			s.l = left;
		}
		Corner m = drawSurface3D.newCorner(um, vm);
		s.a = m;
		e.l = m;
		Corner n = drawSurface3D.newCorner(um, above.v);
		n.l = above.l;
		above.l = n;
		m.a = n;
		Corner w = drawSurface3D.newCorner(left.u, vm);
		w.a = left.a;
		left.a = w;
		m.l = w;
		// next split
		drawSurface3D.addToNextSplit(this);
		drawSurface3D.addToNextSplit(s);
		drawSurface3D.addToNextSplit(e);
		drawSurface3D.addToNextSplit(m);

		drawSurface3D.loopSplitIndex += 4;
	}

	private void addToDrawList(Corner end, Corner... corners) {

		CornerAndCenter cc = drawSurface3D.drawList[drawSurface3D.drawListIndex];
		if (cc == null) {
			cc = new CornerAndCenter(drawSurface3D, this, drawSurface3D.drawListIndex);
			drawSurface3D.drawList[drawSurface3D.drawListIndex] = cc;
		} else {
			cc.setCorner(this);
		}
		drawSurface3D.drawListIndex++;
		drawSurface3D.loopSplitIndex++;

		setBarycenter(cc.getCenter(), cc.getCenterNormal(), corners);

		end.isNotEnd = false;
	}

	private void findU(Corner defined, Corner undefined,
			Corner corner) {
		findU(defined.p, defined.u, defined.u, undefined.u, defined.v,
				(int) Corner.BOUNDARY_SPLIT, corner, true);
	}

	private void findU(Coords3 lastDefined, double uLastDef, double uDef,
			double uUndef, double vRow, int depth, Corner corner,
			boolean lastDefinedIsFirst) {

		double uNew = (uDef + uUndef) / 2;
		Coords3 coords = drawSurface3D.evaluatePoint(uNew, vRow);

		if (depth == 0) { // no more split
			if (coords.isFinalUndefined()) {
				// return last defined point
				if (lastDefinedIsFirst) {
					corner.set(uLastDef, vRow, lastDefined.copyVector());
				} else {
					corner.set(uLastDef, vRow, lastDefined);
				}
			} else {
				corner.set(uNew, vRow, coords);
			}
		} else {
			if (coords.isFinalUndefined()) {
				findU(lastDefined, uLastDef, uDef, uNew, vRow, depth - 1,
						corner, lastDefinedIsFirst);
			} else {
				findU(coords, uNew, uNew, uUndef, vRow, depth - 1, corner,
						false);
			}
		}

	}

	private void findV(Corner defined, Corner undefined,
			Corner corner) {
		findV(defined.p, defined.v, defined.v, undefined.v, defined.u,
				(int) Corner.BOUNDARY_SPLIT, corner, true);
	}

	private void findV(Coords3 lastDefined, double vLastDef, double vDef,
			double vUndef, double uRow, int depth, Corner corner,
			boolean lastDefinedIsFirst) {

		double vNew = (vDef + vUndef) / 2;
		Coords3 coords = drawSurface3D.evaluatePoint(uRow, vNew);

		if (depth == 0) { // no more split
			if (coords.isFinalUndefined()) {
				// return last defined point
				if (lastDefinedIsFirst) {
					corner.set(uRow, vLastDef, lastDefined.copyVector());
				} else {
					corner.set(uRow, vLastDef, lastDefined);
				}
			} else {
				corner.set(uRow, vNew, coords);
			}
		} else {
			if (coords.isFinalUndefined()) {
				findV(lastDefined, vLastDef, vDef, vNew, uRow, depth - 1,
						corner, lastDefinedIsFirst);
			} else {
				findV(coords, vNew, vNew, vUndef, uRow, depth - 1, corner,
						false);
			}
		}

	}

}

