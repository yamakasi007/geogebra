package org.geogebra.common.euclidian;

import com.himamis.retex.editor.share.editor.MathFieldInternal;
import com.himamis.retex.editor.share.event.MathFieldListener;
import com.himamis.retex.editor.share.model.MathFormula;
import com.himamis.retex.editor.share.model.MathSequence;
import com.himamis.retex.editor.share.serializer.TeXSerializer;
import org.geogebra.common.awt.GPoint;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.draw.DrawInputBox;
import org.geogebra.common.kernel.geos.GeoInputBox;
import org.geogebra.common.main.App;
import org.geogebra.common.util.debug.Log;

/**
 * MathField-capable editor for input boxes on EuclidianView.
 */
public abstract class SymbolicEditor implements MathFieldListener {

	protected final App app;
	protected final EuclidianView view;

	protected GeoInputBox geoInputBox;
	protected DrawInputBox drawInputBox;

	protected TeXSerializer serializer;

	protected GRectangle bounds;

	protected MathFieldInternal mathField;

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


	@Override
	public void onCursorMove() {
		// nothing to do.
	}

	@Override
	public void onUpKeyPressed() {
		// nothing to do.
	}

	@Override
	public void onDownKeyPressed() {
		// nothing to do.
	}

	@Override
	public String serialize(MathSequence selectionText) {
		return null;
	}

	@Override
	public void onInsertString() {
		// nothing to do.
	}

	@Override
	public boolean onEscape() {
		hide();
		return true;
	}

	@Override
	public void onTab(boolean shiftDown) {
		applyChanges();
		hide();
		app.getGlobalKeyDispatcher().handleTab(false, shiftDown);
		app.getSelectionManager().nextFromInputBox();
	}

	public void applyChanges() {
		setTempUserDisplayInput();
		String editedText = mathField.getText();
		geoInputBox.updateLinkedGeo(editedText);
	}

	protected void setTempUserDisplayInput() {
		MathFormula formula = mathField.getFormula();
		String latex = serializer.serialize(formula);
		geoInputBox.setTempUserDisplayInput(latex);
	}

	protected void updateText() {
		mathField.parse(geoInputBox.getTextForEditor());
	}

	/**
	 * Hide the editor if it was attached.
	 */
	public abstract void hide();

	public abstract void attach(GRectangle bounds);
}
