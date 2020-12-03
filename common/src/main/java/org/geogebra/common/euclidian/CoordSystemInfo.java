package org.geogebra.common.euclidian;

public class CoordSystemInfo {
	private EuclidianView view;
	private CoordSystemAnimation axesRatioZoomer;
	private double xscale;
	private double yscale;
	private boolean axisZoom = false;

	public CoordSystemInfo(EuclidianView view) {
		this.view = view;
	}

	public double deltaX() {
		return view.xZero - view.xZeroOld;
	}

	public double deltaY() {
		return view.yZero - view.yZeroOld;
	}

	public void setAxesRatioZoomer(CoordSystemAnimation axesRatioZoomer) {
		this.axesRatioZoomer = axesRatioZoomer;
	}

	public boolean isAxisZoom() {
		return axisZoom;
	}

	public void setAxisZoom(boolean axisZoom) {
		this.axisZoom = axisZoom;
	}

	@Override
	public String toString() {
		return "CoordSystemInfo{" +
				"dx: " + deltaX() +
				", dy: " + deltaY() +
				", axisZoom: " + isAxisZoom() +
				'}';
	}
}
