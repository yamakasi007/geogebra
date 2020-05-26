package org.geogebra.web.full.gui.exam;

import org.geogebra.web.full.main.AppWFull;
import org.geogebra.web.shared.components.ComponentDialog;
import org.geogebra.web.shared.components.DialogData;

import com.google.gwt.user.client.ui.Label;

/**
 * @author csilla
 *
 * dialog to enter in graphing or cas calc exam mode
 */
public class ExamStartDialog extends ComponentDialog {

	/**
	 * @param app application
	 */
	public ExamStartDialog(AppWFull app, DialogData data, boolean autoHide, boolean hasScrim) {
		super(app, data, autoHide, hasScrim);
		addStyleName("examStartDialog");
		buildContent();
	}

	private void buildContent() {
		Label startText = new Label(app.getLocalization().getMenu("exam_start_dialog_text"));
		startText.addStyleName("examStartText");
		addDialogContent(startText);
	}
}