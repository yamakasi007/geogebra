/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

package org.geogebra.common.euclidian;

import java.util.Comparator;
import java.util.TreeSet;

import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoPriorityComparator;

/**
 * List to store Drawable objects for fast drawing.
 */
public class DrawableList extends TreeSet<Drawable> {

	public DrawableList(final GeoPriorityComparator comparator) {
		super(new Comparator<Drawable>() {
			@Override
			public int compare(Drawable a, Drawable b) {
				return comparator.compare(a.geo, b.geo, false);
			}
		});
	}

	@Override
	public final boolean add(Drawable d) {
		if (d != null) {
			return super.add(d);
		}

		return false;
	}

	@Override
	public final boolean remove(Object o) {
		if (o != null) {
			return super.remove(o);
		}

		return false;
	}

	/**
	 * Draws all drawables in the list.
	 * 
	 * @param g2
	 *            Graphic to be used
	 */
	public final void drawAll(GGraphics2D g2) {
		for (Drawable d : this) {
			GeoElement geo = d.getGeoElement();
			if (geo.isDefined()
					&& !(geo.isGeoList() && ((GeoList) geo).drawAsComboBox())
					&& !(geo.isGeoInputBox()) && !geo.isMask()) {
				d.updateIfNeeded();
				d.draw(g2);
			}
		}
	}

	/**
	 * Updates all drawables in list
	 */
	public final void updateAll() {
		for (Drawable d : this) {
			d.update();
		}
	}

	/**
	 * Update all elements when view was updated
	 */
	public final void updateAllForView() {
		for (Drawable d : this) {
			d.updateForView();
		}
	}

	/**
	 * Updates fot size for all drawables in list
	 */
	public final void updateFontSizeAll() {
		for (Drawable d : this) {
			d.updateFontSize();
		}
	}
}
