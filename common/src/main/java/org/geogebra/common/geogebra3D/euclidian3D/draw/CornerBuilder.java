package org.geogebra.common.geogebra3D.euclidian3D.draw;

import org.geogebra.common.geogebra3D.euclidian3D.openGL.PlotterSurface;
import org.geogebra.common.kernel.matrix.Coords3;
import org.geogebra.common.kernel.matrix.CoordsDouble3;

class CornerBuilder {

	private int cornerListIndex;
	private int cornerListSize;
	private Corner[] cornerArray;
	private DrawSurface3D drawSurface3D;

	CornerBuilder(DrawSurface3D drawSurface3D) {
			this.drawSurface3D = drawSurface3D;
	}

	/**
	 *
	 * @return new coords 3
	 */
	static Coords3 newCoords3() {
		return new CoordsDouble3();
	}

	/**
	 *
	 * @param u
	 *            first parameter
	 * @param v
	 *            second parameter
	 * @throws Corner.NotEnoughCornersException
	 *             if no new corner left in array
	 * @return new corner calculated for parameters u, v
	 */
	Corner newCorner(double u, double v)
			throws Corner.NotEnoughCornersException {
		if (cornerListIndex >= cornerListSize) {
			throw new Corner.NotEnoughCornersException("Index " + cornerListIndex
					+ " is larger than size " + cornerListSize);
		}
		Corner c = cornerArray[cornerListIndex];
		if (c == null) {
			c = new Corner(u, v, cornerListIndex, drawSurface3D, this);
			cornerArray[cornerListIndex] = c;
		} else {
			c.set(u, v);
		}
		cornerListIndex++;
		return c;
	}

	/**
	 *
	 * @param u
	 *            first parameter
	 * @param v
	 *            second parameter
	 * @throws Corner.NotEnoughCornersException
	 *             if no new corner left in array
	 * @return new corner calculated for parameters u, v
	 */
	Corner newCorner(double u, double v, DrawSurface3D surface)
			throws Corner.NotEnoughCornersException {
		if (cornerListIndex >= cornerListSize) {
			throw new Corner.NotEnoughCornersException("Index " + cornerListIndex
					+ " is larger than size " + cornerListSize);
		}
		Corner c = cornerArray[cornerListIndex];
		if (c == null) {
			c = new Corner(u, v, cornerListIndex, surface, this);
			cornerArray[cornerListIndex] = c;
		} else {
			c.set(u, v);
		}
		cornerListIndex++;
		return c;
	}

	/**
	 * @return new corner
	 * @throws Corner.NotEnoughCornersException
	 *             if no new corner left in array
	 */
	Corner newCorner() throws Corner.NotEnoughCornersException {
		if (cornerListIndex >= cornerListSize) {
			throw new Corner.NotEnoughCornersException("Index " + cornerListIndex
					+ " is larger than size " + cornerListSize);
		}
		Corner c = cornerArray[cornerListIndex];
		if (c == null) {
			c = new Corner(cornerListIndex, drawSurface3D, drawSurface3D.getCornerBuilder());
			cornerArray[cornerListIndex] = c;
		}
		cornerListIndex++;
		return c;
	}

	void drawCornersAndCenters(PlotterSurface surface,
			int drawListIndex, CornerAndCenter[] drawList) {
		for (int i = 0; i < cornerListIndex; i++) {
			Corner c = cornerArray[i];
			surface.normalDirect(c.normal);
			surface.vertexDirect(c.p);
		}
		for (int i = 0; i < drawListIndex; i++) {
			CornerAndCenter cc = drawList[i];
			surface.normalDirect(cc.centerNormal);
			surface.vertexDirect(cc.center);
		}
	}

	void resetCornerListIndex() {
		cornerListIndex = 0;
	}

	int getCornerListIndex() {
		return cornerListIndex;
	}

	void setCornerArray(Corner[] cornerArray) {
		this.cornerArray = cornerArray;
	}

	void setCornerListSize(int cornerListSize) {
		this.cornerListSize = cornerListSize;
	}

	int getCornerListSize() {
		return cornerListSize;
	}
}
