package org.geogebra.desktop.euclidian;

import com.himamis.retex.editor.desktop.MathFieldD;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.SymbolicEditor;
import org.geogebra.common.euclidian.draw.DrawInputBox;
import org.geogebra.common.main.App;
import org.geogebra.common.util.debug.Log;
import org.geogebra.desktop.awt.GRectangleD;

import javax.swing.*;

public class SymbolicEditorD extends SymbolicEditor {

	private MathFieldD editor;
	private Box box;

	private JLabel testLabel;

	public SymbolicEditorD(App app, EuclidianView view, DrawInputBox drawInputBox) {
		super(app, view, drawInputBox);
		editor = new MathFieldD();

		mathField = editor.getInternal();
		mathField.setFieldListener(this);
	}

	@Override
	public void hide() {
		//box.setVisible(false);
		drawInputBox.setEditing(false);
		Log.debug("hiding");
	}

	@Override
	public void attach(GRectangle bounds) {

		Log.debug("attach called");
		box = Box.createHorizontalBox();
		testLabel = new JLabel("alma");
		testLabel.setBounds((int) bounds.getX(), (int) bounds.getY() + 50, (int) bounds.getWidth(), (int) bounds.getHeight());
		((EuclidianViewD) view).add(box);
		testLabel.setVisible(true);
		box.setVisible(true);

		drawInputBox.setEditing(true);
		editor.setText(geoInputBox.getTextForEditor());
	}

	@Override
	public void onEnter() {

	}

	@Override
	public void onKeyTyped() {

	}
}
