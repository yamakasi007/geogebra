package org.geogebra.web.full.gui.components.dropdown.grid;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

class GridDropdownView extends Grid {

	interface GridDropdownViewListener {

		void widgetClicked(Widget widget, int row, int column);
	}

	GridDropdownView(List<GridDropdown.GridItem> items, int selectedIndex, int columns) {
		super();
		setStyleName("gridDrowpdownView");

		setSize(items.size(), columns);
		addItems(items, selectedIndex, columns);
	}

	private void setSize(int itemCount, int columns) {
		int rows = (int) Math.ceil(itemCount / (double) columns);
		resize(rows, columns);
	}

	private void addItems(List<GridDropdown.GridItem> items, int selectedIndex, int columns) {
		for (int i = 0; i < items.size(); i++) {
			GridDropdownItem cell = new GridDropdownItem();
			GridDropdown.GridItem item = items.get(i);
			cell.setTitle(item.title);
			if (item.resource != null) {
				cell.setImage(item.resource);
			}
			int row = (int) Math.floor(i / columns);
			int column = i % columns;
			setWidget(row, column, cell);

			CellFormatter formatter = getCellFormatter();
			formatter.addStyleName(row, column, "cell");
			if (i == selectedIndex) {
				cell.addStyleName("activeItem");
			}
		}
	}
}
