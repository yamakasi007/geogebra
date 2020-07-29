package org.geogebra.common.geogebra3D.euclidian3D.draw;


import org.geogebra.common.geogebra3D.euclidian3D.openGL.PlotterSurface;
import org.geogebra.common.kernel.matrix.Coords3;

class CornerAndCenter {
	private DrawSurface3D.Corner corner;
	Coords3 center;
	Coords3 centerNormal;
	int id;
	private DrawSurface3D drawSurface3D;

	public CornerAndCenter(DrawSurface3D drawSurface3D, DrawSurface3D.Corner corner, int id) {
		center = CornerBuilder.newCoords3();
		centerNormal = CornerBuilder.newCoords3();
		setCorner(corner);
		this.id = id;
		this.drawSurface3D = drawSurface3D;
	}

	/**
	 * set the corner
	 *
	 * @param corner
	 *            corner
	 */
	public void setCorner(DrawSurface3D.Corner corner) {
		this.corner = corner;
	}

	/**
	 *
	 * @return corner
	 */
	public DrawSurface3D.Corner getCorner() {
		return corner;
	}

	/**
	 *
	 * @return center
	 */
	public Coords3 getCenter() {
		return center;
	}

	/**
	 *
	 * @return center normal
	 */
	public Coords3 getCenterNormal() {
		return centerNormal;
	}

	public void drawDebug(PlotterSurface surface) {

		surface.startTrianglesWireFrame();
		draw(surface);
		surface.endGeometryDirect();

		surface.startTrianglesWireFrameSurface();
		draw(surface);
		surface.endGeometryDirect();

	}

	public void draw(PlotterSurface surface) {
		DrawSurface3D.Corner p1, p2;

		// go left
		DrawSurface3D.Corner current = corner;
		// get first defined point on south (if exists)
		DrawSurface3D.Corner sw1 = current;
		DrawSurface3D.Corner sw2 = sw1;
		// draw south
		p1 = sw1;
		do {
			p2 = current.l;
			if (p2.p.isNotFinalUndefined()) {
				if (p1.p.isNotFinalUndefined()) {
					if (sw1.p.isFinalUndefined()) {
						sw1 = p1;
					}
					drawSurface3D.drawTriangle(surface, this, p2, p1);
				}
				p1 = p2;
				sw2 = p2;
			}
			current = current.l;
		} while (current.a == null);

		DrawSurface3D.Corner sw = current;

		// go above
		current = corner;
		// get first defined point on east (if exists)
		DrawSurface3D.Corner ne1 = current;
		DrawSurface3D.Corner ne2 = ne1;
		// draw east
		p1 = ne1;
		do {
			p2 = current.a;
			if (p2.p.isNotFinalUndefined()) {
				if (p1.p.isNotFinalUndefined()) {
					drawSurface3D.drawTriangle(surface, this, p1, p2);
					if (ne1.p.isFinalUndefined()) {
						ne1 = p1;
					}
				}
				p1 = p2;
				ne2 = p2;
			}
			current = current.a;
		} while (current.l == null);
		DrawSurface3D.Corner ne = current;

		// west side
		current = sw;
		p1 = sw2;
		if (sw1.p.isFinalUndefined()) {
			sw1 = p1;
		}
		do {
			p2 = current.a;
			if (p2.p.isNotFinalUndefined()) {
				if (p1.p.isNotFinalUndefined()) {
					drawSurface3D.drawTriangle(surface, this, p2, p1);
					if (sw1.p.isFinalUndefined()) {
						sw1 = p1;
					}
				}
				p1 = p2;
				sw2 = p2;
			}
			current = current.a;
		} while (current.isNotEnd);

		// north side
		current = ne;
		p1 = ne2;
		if (ne1.p.isFinalUndefined()) {
			ne1 = p1;
		}
		do {
			p2 = current.l;
			if (p2.p.isNotFinalUndefined()) {
				if (p1.p.isNotFinalUndefined()) {
					drawSurface3D.drawTriangle(surface, this, p1, p2);
					if (ne1.p.isFinalUndefined()) {
						ne1 = p1;
					}
				}
				p1 = p2;
				ne2 = p2;
			}
			current = current.l;
		} while (current.isNotEnd);

		// closure triangles if needed
		if (sw1 != ne1) {
			drawSurface3D.drawTriangleCheckCorners(surface, this, sw1, ne1);
		}
		if (sw2 != ne2) {
			drawSurface3D.drawTriangleCheckCorners(surface, this, ne2, sw2);
		}
		if (ne1.p.isFinalUndefined() && ne2.p.isFinalUndefined()) {
			drawSurface3D.drawTriangleCheckCorners(surface, this, sw2, sw1);
		}
		if (sw1.p.isFinalUndefined() && sw2.p.isFinalUndefined()) {
			drawSurface3D.drawTriangleCheckCorners(surface, this, ne1, ne2);
		}
	}

}

