package org.geogebra.common.geogebra3D.euclidian3D.draw;

import org.geogebra.common.geogebra3D.euclidian3D.openGL.PlotterBrush;
import org.geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;

class DrawWireframe {

	private boolean wireframeVisible = false;
	Corner[] wireframeBottomCorners;
	Corner[] wireframeRightCorners;
	int wireframeBottomCornersLength;
	int wireframeRightCornersLength;

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
