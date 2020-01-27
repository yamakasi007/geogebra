package org.geogebra.web.full.gui.dialog;

import com.google.gwt.user.client.ui.FlowPanel;
import org.geogebra.common.euclidian.EuclidianConstants;
import org.geogebra.common.kernel.ModeSetter;
import org.geogebra.web.html5.main.AppW;

public class HyperlinkDialog extends OptionDialog {

	private MediaInputPanel textInputPanel;
	private MediaInputPanel linkInputPanel;

	public HyperlinkDialog(AppW app) {
		super(app.getPanel(), app);

		FlowPanel mainPanel = new FlowPanel();

		textInputPanel = new MediaInputPanel(app, this,
				app.getLocalization().getMenu("Text"));
		linkInputPanel = new MediaInputPanel(app, this,
				app.getLocalization().getMenu("Link"));

		updateButtonLabels("Ok");

		mainPanel.add(textInputPanel);
		mainPanel.add(linkInputPanel);
		mainPanel.add(getButtonPanel());
		add(mainPanel);

		addStyleName("GeoGebraPopup");
		addStyleName("mediaDialog");
		addStyleName("mebis");

		linkInputPanel.focusDeferred();
	}

	@Override
	protected void processInput() {
		//inputField.getTextComponent().setText(url);

		// TODO: do action ;)
	}

	@Override
	public void hide() {
		super.hide();
		app.getGuiManager().setMode(EuclidianConstants.MODE_SELECT_MOW,
				ModeSetter.TOOLBAR);
	}
}
