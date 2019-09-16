package org.geogebra.common.gui.dialog.options.model;

import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoInputBox;
import org.geogebra.common.kernel.geos.properties.TextAlignment;
import org.geogebra.common.main.App;

public class InputTextAlignModel extends NumberOptionsModel {

	private IComboListener listener;

	public InputTextAlignModel(App app) {
		super(app);
	}

	@Override
	protected void apply(int index, int value) {
		GeoElement inputBox = getGeoAt(index);
		((GeoInputBox)inputBox).setAlignment(TextAlignment.numberToAlignment(value));
	}

	@Override
	protected int getValueAt(int index) {
		return TextAlignment.alignmentToNumber(getAlignmentProperties(index));
	}

	@Override
	protected boolean isValidAt(int index) {
		GeoElement geo = getGeoAt(index);
		return geo instanceof GeoInputBox && !((GeoInputBox) geo).isSymbolicMode();
	}

	@Override
	public void updateProperties() {
		TextAlignment alignment = getAlignmentProperties(0);
		listener.setSelectedIndex(TextAlignment.alignmentToNumber(alignment));
	}

	@Override
	public PropertyListener getListener() {
		return listener;
	}

	private TextAlignment getAlignmentProperties(int index) {
		return (TextAlignment) getObjectAt(index);
	}

	public void setListener(IComboListener listener) {
		this.listener = listener;
	}

}
