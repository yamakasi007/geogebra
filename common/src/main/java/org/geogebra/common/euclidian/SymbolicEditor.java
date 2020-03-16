package org.geogebra.common.euclidian;

import com.himamis.retex.editor.share.serializer.TeXSerializer;
import org.geogebra.common.awt.GPoint;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.draw.DrawInputBox;
import org.geogebra.common.kernel.geos.GeoInputBox;
import org.geogebra.common.main.App;

/**
 * MathField-capable editor for input boxes on EuclidianView.
 */
public abstract class SymbolicEditor {

	protected final App app;
	protected final EuclidianView view;

	protected GeoInputBox geoInputBox;
	protected DrawInputBox drawInputBox;

	protected TeXSerializer serializer;

	protected GRectangle bounds;

	/**
	 * Constructor
	 *
	 * @param app
	 *            The application.
	 */
	public SymbolicEditor(App app, EuclidianView view) {
		this.app = app;
		this.view = view;
		this.serializer = new TeXSerializer();
	}

	/**
	 * @param point
	 *            mouse coordinates
	 * @return if editor is clicked.
	 */
	public boolean isClicked(GPoint point) {
		return drawInputBox.isEditing() && bounds.contains(point.getX(), point.getY());
	}

	/**
	 * Hide the editor if it was attached.
	 */
	public abstract void hide();
}
