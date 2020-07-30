package org.geogebra.common.geogebra3D.euclidian3D.draw;

import org.geogebra.common.geogebra3D.euclidian3D.openGL.PlotterBrush;
import org.geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;

class DrawWireframe {

	private boolean wireframeVisible = false;
	private Corner[] wireframeBottomCorners;
	private Corner[] wireframeRightCorners;
	private int wireframeBottomCornersLength;
	private int wireframeRightCornersLength;
	private SurfaceParameter uParam;
	private SurfaceParameter vParam;
	private CornerBuilder cornerBuilder;

	DrawWireframe(CornerBuilder cornerBuilder, SurfaceParameter uParam,
			SurfaceParameter vParam) {
		this.cornerBuilder = cornerBuilder;
		this.uParam = uParam;
		this.vParam = vParam;
	}

	Corner createRootMesh(boolean wireframeNeeded)
			throws Corner.NotEnoughCornersException {
		if (wireframeNeeded) {
			wireframeBottomCorners = new Corner[uParam.getCornerCount()];
			wireframeRightCorners = new Corner[vParam.getCornerCount()];
		}

		Corner bottomRight = cornerBuilder.newCorner(uParam.borderMax, vParam.borderMax);
		Corner first = bottomRight;

		wireframeBottomCornersLength = 0;
		wireframeRightCornersLength = 0;
		int wireFrameSetU = uParam.wireFrameStep,
				wireFrameSetV = vParam.wireFrameStep;
		if (wireframeNeeded) {
			if (uParam.wireframeUnique) {
				wireFrameSetU = 0;
			}
			if (uParam.wireframeBorder == 1) { // draw edges
				wireframeBottomCorners[0] = first;
				wireframeBottomCornersLength = 1;
				wireFrameSetU = 1;
			}
			if (vParam.wireframeUnique) {
				wireFrameSetV = 0;
			}
			if (vParam.wireframeBorder == 1) { // draw edges
				wireframeRightCorners[0] = first;
				wireframeRightCornersLength = 1;
				wireFrameSetV = 1;
			}
		}

		// first row
		Corner right = bottomRight;
		int uN = uParam.n;
		for (int i = 0; i < uN - 1; i++) {
			right = addLeftToMesh(right, uParam.max - (uParam.delta * i) / uN,
					vParam.borderMax);
			if (wireframeNeeded) {
				if (wireFrameSetU == uParam.wireFrameStep) { // set wireframe
					wireframeBottomCorners[wireframeBottomCornersLength] = right;
					wireframeBottomCornersLength++;
					if (uParam.wireframeUnique) {
						wireFrameSetU++;
					} else {
						wireFrameSetU = 1;
					}
				} else {
					wireFrameSetU++;
				}
			}
		}
		right = addLeftToMesh(right, uParam.borderMin, vParam.borderMax);
		if (wireframeNeeded) {
			if (uParam.wireframeBorder == 1) {
				wireframeBottomCorners[wireframeBottomCornersLength] = right;
				wireframeBottomCornersLength++;
			}
		}
		int vN = vParam.n;
		// all intermediate rows
		for (int j = 0; j < vN - 1; j++) {
			bottomRight = addRowAboveToMesh(bottomRight,
					vParam.max - (vParam.delta * j) / vN, uParam.borderMin,
					uParam.borderMax, uParam.max, uN);
			if (wireframeNeeded) {
				if (wireFrameSetV == vParam.wireFrameStep) { // set wireframe
					wireframeRightCorners[wireframeRightCornersLength] = bottomRight;
					wireframeRightCornersLength++;
					if (vParam.wireframeUnique) {
						wireFrameSetV++;
					} else {
						wireFrameSetV = 1;
					}
				} else {
					wireFrameSetV++;
				}
			}
		}

		// last row
		bottomRight = addRowAboveToMesh(bottomRight, vParam.borderMin,
				uParam.borderMin, uParam.borderMax, uParam.max, uN);
		if (wireframeNeeded) {
			if (vParam.wireframeBorder == 1) {
				wireframeRightCorners[wireframeRightCornersLength] = bottomRight;
				wireframeRightCornersLength++;
			}
		}

		return first;
	}

	static void splitRootMesh(Corner first)
			throws Corner.NotEnoughCornersException {

		Corner nextAbove, nextLeft;

		Corner current = first;
		while (current.a != null) {
			nextAbove = current.a;
			while (current.l != null) {
				nextLeft = current.l;
				if (nextLeft.a == null) { // already split by last row
					nextLeft = nextLeft.l;
				}
				// Log.debug(current.u + "," + current.v);
				current.split(false);
				current = nextLeft;
			}
			current = nextAbove;
		}

	}

	private Corner addRowAboveToMesh(Corner bottomRight, double v,
			double uBorderMin, double uBorderMax, double uMax, int uN)
			throws Corner.NotEnoughCornersException {
		Corner below = bottomRight;
		Corner right = cornerBuilder.newCorner(uBorderMax, v);
		below.a = right;
		for (int i = 0; i < uN - 1; i++) {
			right = addLeftToMesh(right, uMax - (uParam.delta * i) / uN, v);
			below = below.l;
			below.a = right;
		}
		right = addLeftToMesh(right, uBorderMin, v);
		below = below.l;
		below.a = right;

		return bottomRight.a;
	}

	private Corner addLeftToMesh(Corner right, double u, double v)
			throws Corner.NotEnoughCornersException {
		Corner left = cornerBuilder.newCorner(u, v);
		right.l = left;
		return left;
	}

	void drawWireframe(Drawable3D drawable, Renderer renderer, int oldThickness) {

		int thickness = drawable.getGeoElement().getLineThickness();
		if (thickness == 0) {
			setWireframeInvisible(drawable);
			return;
		}

		// wireframe is visible
		wireframeVisible = true;

		if (thickness == oldThickness) {
			// surface and thickness have not changed
			return;
		}

		PlotterBrush brush = renderer.getGeometryManager().getBrush();

		// point were already scaled
		renderer.getGeometryManager().setScalerIdentity();

		drawable.setPackCurve(true);
		brush.start(drawable.getReusableGeometryIndex());
		brush.setThickness(thickness, (float) renderer.getView().getScale());
		brush.setAffineTexture(0f, 0f);
		brush.setLength(1f);

		for (int i = 0; i < wireframeBottomCornersLength; i++) {
			Corner above = wireframeBottomCorners[i];
			boolean currentPointIsDefined = isDefinedForWireframe(above);
			if (currentPointIsDefined) {
				brush.moveTo(above.p.getXd(), above.p.getYd(), above.p.getZd());
			}
			boolean lastPointIsDefined = currentPointIsDefined;
			above = above.a;
			while (above != null) {
				currentPointIsDefined = isDefinedForWireframe(above);
				if (currentPointIsDefined) {
					if (lastPointIsDefined) {
						brush.drawTo(above.p.getXd(), above.p.getYd(),
								above.p.getZd(), true);
					} else {
						brush.moveTo(above.p.getXd(), above.p.getYd(),
								above.p.getZd());
					}
				}
				lastPointIsDefined = currentPointIsDefined;
				above = above.a;
			}
			brush.endPlot();
		}

		for (int i = 0; i < wireframeRightCornersLength; i++) {
			Corner left = wireframeRightCorners[i];
			boolean currentPointIsDefined = isDefinedForWireframe(left);
			if (currentPointIsDefined) {
				brush.moveTo(left.p.getXd(), left.p.getYd(), left.p.getZd());
			}
			boolean lastPointIsDefined = currentPointIsDefined;
			left = left.l;
			while (left != null) {
				currentPointIsDefined = isDefinedForWireframe(left);
				if (currentPointIsDefined) {
					if (lastPointIsDefined) {
						brush.drawTo(left.p.getXd(), left.p.getYd(),
								left.p.getZd(), true);
					} else {
						brush.moveTo(left.p.getXd(), left.p.getYd(),
								left.p.getZd());
					}
				}
				lastPointIsDefined = currentPointIsDefined;
				left = left.l;
			}
			brush.endPlot();
		}

		drawable.setGeometryIndex(brush.end());
		drawable.endPacking();

		// point were already scaled
		renderer.getGeometryManager().setScalerView();
	}

	void setWireframeInvisible() {
		this.wireframeVisible = false;
	}

	boolean isWireframeHidden() {
		return !wireframeVisible;
	}

	private void setWireframeInvisible(Drawable3D drawable3D) {
		setWireframeInvisible();
		if (drawable3D.shouldBePackedForManager()) {
			drawable3D.setGeometryIndexNotVisible();
		}
	}

	static private boolean isDefinedForWireframe(Corner corner) {
		if (corner.p.isFinalUndefined()) {
			return false;
		}

		return corner.p.isDefined();
	}
}
