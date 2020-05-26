package org.geogebra.web.full.gui.exam;

import org.geogebra.web.full.main.AppWFull;
import org.geogebra.web.html5.gui.FastClickHandler;
import org.geogebra.web.html5.gui.view.button.StandardButton;
import org.geogebra.web.shared.components.ComponentDialog;
import org.geogebra.web.shared.components.DialogData;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author csilla
 *
 * dialog to enter in graphing or cas calc exam mode
 */
public class ExamStartDialog extends ComponentDialog
		implements FastClickHandler {
	private Label startText;
	private StandardButton cancelBtn;
	private StandardButton startBtn;

	/**
	 * @param app application
	 */
	public ExamStartDialog(AppWFull app, DialogData data, boolean autoHide, boolean hasScrim) {
		super(app, data, autoHide, hasScrim);
		this.addStyleName("examStartDialog");
		buildGUI();
	}

	private void buildGUI() {
		startText = new Label("");
		startText.addStyleName("examStartText");
		startText.setText(
				app.getLocalization().getMenu("exam_start_dialog_text"));
		super.addDialogContent(startText);
	}

	@Override
	public void onClick(Widget source) {
		if (source == startBtn) {
			app.setNewExam();
			app.startExam();
		} else if (source == cancelBtn) {
			((AppWFull) app).getLAF().toggleFullscreen(false);
		}
		hide();
	}
}
