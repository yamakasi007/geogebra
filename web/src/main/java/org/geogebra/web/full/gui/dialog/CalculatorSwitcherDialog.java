package org.geogebra.web.full.gui.dialog;

import org.geogebra.web.full.gui.images.SvgPerspectiveResources;
import org.geogebra.web.html5.gui.GPopupPanel;
import org.geogebra.web.html5.gui.view.button.StandardButton;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.resources.SVGResource;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class CalculatorSwitcherDialog extends GPopupPanel {

	public CalculatorSwitcherDialog(AppW app) {
		super(true, app.getPanel(), app);
		setGlassEnabled(true);
		addStyleName("calcChooser");
		buildGUI();
	}

	private void buildGUI() {
		FlowPanel contentPanel = new FlowPanel();
		Label title = new Label(app.getLocalization().getMenu("ChooseCalculator"));
		title.addStyleName("title");
		contentPanel.add(title);

		SvgPerspectiveResources res = SvgPerspectiveResources.INSTANCE;
		StandardButton btnGraphing = buildCalcButton(res.menu_icon_algebra_transparent(),
				"GraphingCalculator.short");
		contentPanel.add(btnGraphing);

		StandardButton btn3D = buildCalcButton(res.menu_icon_graphics3D_transparent(),
				"GeoGebra3DGrapher.short");
		contentPanel.add(btn3D);

		StandardButton btnGeometry = buildCalcButton(res.menu_icon_geometry_transparent(),
				"Geometry");
		contentPanel.add(btnGeometry);

		StandardButton btnCAS = buildCalcButton(res.menu_icon_cas_transparent(),
				"CAS");
		contentPanel.add(btnCAS);

		add(contentPanel);
	}

	private StandardButton buildCalcButton(SVGResource icon, String appNameKey) {
		 StandardButton button =  new StandardButton (icon, app.getLocalization()
				.getMenu(appNameKey), 24, app);
		button.setStyleName("toolButton");
		return button;
	}

	@Override
	public void show() {
		super.show();
		super.center();
	}
}
