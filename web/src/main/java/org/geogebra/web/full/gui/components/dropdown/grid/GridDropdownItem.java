package org.geogebra.web.full.gui.components.dropdown.grid;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import org.geogebra.web.resources.SVGResource;

class GridDropdownItem extends FlowPanel {

	private Image image;
	private Label title;

	GridDropdownItem() {
		super();
		setStyleName("item");
		setupView();
	}

	private void setupView() {
		image = new Image();
		image.addStyleName("image");
		add(image);

		title = new Label();
		title.addStyleName("title");
		add(title);
	}

	public void setTitle(String text) {
		title.setText(text);
	}

	public void setImage(DataResource resource) {
		if (resource instanceof SVGResource) {
			String url = //"data:image/svg+xml;charset=utf-8," +
					 resource.getSafeUri().asString();
			image.setUrl(url);
		} else if (resource instanceof ImageResource) {
			image.setResource((ImageResource) resource);
		}
	}
}
