package org.geogebra.common.kernel.geos;

import org.geogebra.common.awt.GPoint2D;
import org.geogebra.common.kernel.kernelND.GeoElementND;

public interface GeoInline extends GeoElementND {
	/**
	 * Get the height of the element.
	 *
	 * @return height
	 */
	double getHeight();

	/**
	 * Get the widht of the element.
	 *
	 * @return width
	 */
	double getWidth();

	double getAngle();

	/**
	 * Get the location of the text.
	 *
	 * @return location
	 */
	GPoint2D getLocation();

	void setWidth(double widthD);

	void setHeight(double heightD);

	void setAngle(double angleD);

	void setLocation(GPoint2D startPoint);

	void setContent(String content);

	double getMinHeight();
}
