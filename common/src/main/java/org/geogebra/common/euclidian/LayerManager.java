package org.geogebra.common.euclidian;

import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.main.App;

import java.util.ArrayList;
import java.util.List;

public class LayerManager {

	private App app;

	private final List<GeoElement> drawingOrder = new ArrayList<>();

	public LayerManager(App app) {
		this.app = app;
	}

	private int getNextOrder() {
		return drawingOrder.size();
	}

	public void addGeo(GeoElement geo) {
		geo.setOrdering(getNextOrder());
		drawingOrder.add(geo);
	}

	public void removeGeo(GeoElement geo) {
		drawingOrder.remove(geo);
		for (int i = 0; i < drawingOrder.size(); i++) {
			drawingOrder.get(i).setOrdering(i);
		}

		app.getActiveEuclidianView().invalidateDrawableLists();
	}
}
