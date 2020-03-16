package org.geogebra.web.full.euclidian;

import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.SymbolicEditor;
import org.geogebra.common.euclidian.draw.DrawInputBox;
import org.geogebra.common.euclidian.draw.LaTeXTextRenderer;
import org.geogebra.common.kernel.geos.GeoInputBox;
import org.geogebra.common.main.App;
import org.geogebra.web.full.gui.components.MathFieldEditor;
import org.geogebra.web.html5.euclidian.EuclidianViewW;
import org.geogebra.web.html5.euclidian.HasMathKeyboardListener;
import org.geogebra.web.html5.gui.util.MathKeyboardListener;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;

/**
 * MathField-capable editor for EV, Web implementation.
 */
public class SymbolicEditorW extends SymbolicEditor implements HasMathKeyboardListener,
		BlurHandler, ChangeHandler {

	private MathFieldEditor editor;
	private final SymbolicEditorDecorator decorator;

	/**
	 * Constructor
	 *
	 * @param app
	 *            The application.
	 */
	public SymbolicEditorW(App app, EuclidianViewW view) {
		super(app, view);
		editor = new MathFieldEditor(app, this);
		editor.addBlurHandler(this);
		editor.getMathField().setChangeListener(this);
		editor.getMathField().setFixMargin(LaTeXTextRenderer.MARGIN);
		editor.getMathField().setMinHeight(DrawInputBox.MIN_HEIGHT);
		int baseFontSize = app.getSettings()
				.getFontSettings().getAppFontSize() + 3;

		decorator = new SymbolicEditorDecorator(editor, baseFontSize);

		mathField = editor.getMathField().getInternal();
	}

	@Override
	public void attach(GeoInputBox geoInputBox, GRectangle bounds) {
		this.geoInputBox = geoInputBox;
		this.drawInputBox = (DrawInputBox) view.getDrawableFor(geoInputBox);
		this.bounds = bounds;

		resetChanges();
		editor.attach(((EuclidianViewW) view).getAbsolutePanel());
	}

	@Override
	public MathKeyboardListener getKeyboardListener() {
		return editor.getKeyboardListener();
	}

	@Override
	protected void resetChanges() {
		super.resetChanges();

		editor.setVisible(true);
		decorator.update(bounds, geoInputBox);
		editor.setKeyboardVisibility(true);
		editor.setLabel(geoInputBox.getAuralText());
	}

	@Override
	public void hide() {
		if (!drawInputBox.isEditing()) {
			return;
		}

		applyChanges();
		drawInputBox.setEditing(false);
		AnimationScheduler.get()
				.requestAnimationFrame(new AnimationScheduler.AnimationCallback() {
			@Override
			public void execute(double timestamp) {
				((EuclidianViewW) view).doRepaint2();
				editor.setVisible(false);
				editor.setKeyboardVisibility(false);
			}
		});
	}

	@Override
	public void onEnter() {
		applyChanges();
	}

	@Override
	public void onKeyTyped() {
		decorator.update();
		geoInputBox.update();
		editor.scrollHorizontally();
		editor.updateAriaLabel();
	}

	@Override
	public void onBlur(BlurEvent event) {
		hide();
	}

	@Override
	public void onChange(ChangeEvent event) {
		decorator.update();
	}
}
