package org.geogebra.desktop.euclidian;

import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.SymbolicEditor;
import org.geogebra.common.euclidian.draw.DrawInputBox;
import org.geogebra.common.kernel.geos.GeoInputBox;
import org.geogebra.common.main.App;

public class SymbolicEditorD extends SymbolicEditor {

	public SymbolicEditorD(App app, EuclidianView view) {
		super(app, view);

	}

	@Override
	public void hide() {

	}

	@Override
	public void attach(GeoInputBox geoInputBox, GRectangle bounds) {
		this.geoInputBox = geoInputBox;
		this.drawInputBox = (DrawInputBox) view.getDrawableFor(geoInputBox);
		this.bounds = bounds;

		resetChanges();
	}

	@Override
	public void onEnter() {

	}

	@Override
	public void onKeyTyped() {

	}
}
