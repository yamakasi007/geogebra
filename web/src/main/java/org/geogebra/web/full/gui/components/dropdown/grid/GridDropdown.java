package org.geogebra.web.full.gui.components.dropdown.grid;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.SimplePanel;
import org.geogebra.web.html5.gui.GPopupPanel;
import org.geogebra.web.html5.main.AppW;

import java.util.ArrayList;
import java.util.List;

public class GridDropdown extends SimplePanel implements ClickHandler {

	public interface GridDropdownListener {

		void itemSelected(GridDropdown dropdown, int index);
	}

	static class GridItem {
		DataResource resource;
		String title;

		GridItem(DataResource resource, String title) {
			this.resource = resource;
			this.title = title;
		}
	}

	private static final int COLUMNS = 3;
	private static final int UNSELECTED_INDEX = -1;

	private GridDropdownListener listener;

	private AppW app;

	private Button button;
	private GridDropdownView view;
	private GPopupPanel popup;

	private List<GridItem> items = new ArrayList<>();
	private int selectedIndex = UNSELECTED_INDEX;

	public GridDropdown(AppW app) {
		this.app = app;
		setStyleName("gridDropdown");
		createButton();
	}

	private void createButton() {
		button = new Button();
		button.addStyleName("button");
		button.addClickHandler(this);
		add(button);
	}

	public void setEnabled(boolean enabled) {
		button.setEnabled(enabled);
	}

	public void setSelectedIndex(int index) {
		if (index < UNSELECTED_INDEX || index >= items.size()) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		selectedIndex = index;
		updateSelectedIndex();
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	private void updateSelectedIndex() {
		String title = "";
		if (selectedIndex >= 0) {
			title = items.get(selectedIndex).title;
		}
		button.setText(title);
	}

	public void addItem(String item, DataResource resource) {
		items.add(new GridItem(resource, item));
		if (items.size() == 1) {
			setSelectedIndex(0);
		}
	}

	@Override
	public void clear() {
		items.clear();
		setSelectedIndex(UNSELECTED_INDEX);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == button) {
			showGridPopup();
		} else {
			handleGridClick(event);
		}

	}

	private void showGridPopup() {
		view = new GridDropdownView(items, selectedIndex, COLUMNS);
		view.addClickHandler(this);
		popup = new GPopupPanel(true, true, app.getPanel(), app);
		popup.addStyleName("materialPopupPanel");
		popup.add(view);
		popup.showRelativeTo(button);
	}

	private void handleGridClick(ClickEvent event) {
		HTMLTable.Cell cell = view.getCellForEvent(event);
		if (cell != null) {
			handleCellClicked(cell);
		}
	}

	private void handleCellClicked(HTMLTable.Cell cell) {
		int column = cell.getCellIndex();
		int row = cell.getRowIndex();
		int itemIndex = row * COLUMNS + column;
		setSelectedIndex(itemIndex);
		if (listener != null) {
			listener.itemSelected(this, itemIndex);
		}
		popup.hide();
	}

	public void setListener(GridDropdownListener listener) {
		this.listener = listener;
	}
}
