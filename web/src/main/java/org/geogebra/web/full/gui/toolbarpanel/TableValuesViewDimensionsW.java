package org.geogebra.web.full.gui.toolbarpanel;

import org.geogebra.common.awt.GFontRenderContext;
import org.geogebra.common.factories.AwtFactory;
import org.geogebra.common.gui.view.table.TableValuesModel;
import org.geogebra.common.gui.view.table.dimensions.TableValuesViewDimensions;

/**
 * Web Implementation of TableValuesDimensions.
 */
public class TableValuesViewDimensionsW extends TableValuesViewDimensions {

	private static final int VIEW_TOP_PADDING = 8;
	private static final int VIEW_LEFT_PADDING = 16;
	private static final int STRICT_VALUE_COLUM_WIDTH = 95;
	private static final int STRICT_X_COLUMN_WIDTH = 72 + VIEW_LEFT_PADDING;
	private static final int STRICT_ROW_HEIGHT = 40;
	private static final int STRICT_HEADER_HEIGHT = STRICT_ROW_HEIGHT + VIEW_TOP_PADDING;

    /**
     * Construct a new TableValuesViewDimensions object.
     *
     * @param model table values model
     * @param factory awt factory
     * @param context font render context
     */
	public TableValuesViewDimensionsW(TableValuesModel model,
			AwtFactory factory, GFontRenderContext context) {
		super(model, factory, context);
    }

    @Override
    public int getHeaderHeight() {
		int height = super.getHeaderHeight();
		return Math.min(STRICT_HEADER_HEIGHT, height);
    }

	@Override
	public int getRowHeight(int row) {
		return STRICT_ROW_HEIGHT;
	}

    @Override
	protected int calculateExactColumnWidth(int column) {
		int maxWidth = super.calculateExactColumnWidth(column);
		if (column == 0) {
			return Math.max(maxWidth, STRICT_X_COLUMN_WIDTH);
		}

		return Math.max(maxWidth, STRICT_VALUE_COLUM_WIDTH);
	}
}