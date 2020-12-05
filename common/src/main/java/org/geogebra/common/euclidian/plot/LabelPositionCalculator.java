package org.geogebra.common.euclidian.plot;

import org.geogebra.common.awt.GPoint;
import org.geogebra.common.euclidian.EuclidianView;

public class LabelPositionCalculator {

	private final EuclidianView view;

	public LabelPositionCalculator(EuclidianView view) {
		this.view = view;
	}

	public GPoint calculate(double x, double y) {
		double xLabel = view.toScreenCoordXd(x)	+ 10;
		if (xLabel < 20) {
			xLabel = 5;
		}

		if (xLabel > view.getWidth() - 30) {
			xLabel = view.getWidth() - 15;
		}

		double yLabel = view.toScreenCoordYd(y)	+ 15;
		if (yLabel < 40) {
			yLabel = 15;
		} else if (yLabel > view.getHeight() - 30) {
			yLabel = view.getHeight() - 5;
		}

		return new GPoint((int) xLabel, (int) yLabel);
	}
}