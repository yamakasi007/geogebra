package org.geogebra.common.euclidian.draw;

import org.geogebra.common.awt.GColor;
import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.awt.GPoint2D;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.BoundingBox;
import org.geogebra.common.euclidian.Drawable;
import org.geogebra.common.euclidian.EuclidianBoundingBoxHandler;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.RemoveNeeded;
import org.geogebra.common.euclidian.text.InlineTextController;
import org.geogebra.common.factories.AwtFactory;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoInlineText;
import org.geogebra.common.kernel.geos.GeoPoint;

/**
 * Class that handles drawing inline text elements.
 */
public class DrawInlineText extends Drawable implements RemoveNeeded, DrawWidget {

	private int padding = 8;
	private GeoInlineText text;
	private double[] coords = new double[2];
	private InlineTextController textController;
	private BoundingBox boundingBox;

	/**
	 * Create a new DrawInlineText instance.
	 *
	 * @param view view
	 * @param text geo element
	 */
	public DrawInlineText(EuclidianView view, GeoInlineText text) {
		super(view, text);
		this.text = text;
		this.textController = view.createInlineTextController();
		createEditor();
		update();
	}

	private void createEditor() {
		if (textController != null) {
			textController.create();
		}
	}

	@Override
	public void update() {
		GeoPoint point = text.getLocation();
		point.getInhomCoords(coords);
		view.toScreenCoords(coords);

		if (textController != null) {
			textController.setLocation((int) Math.round(coords[0]) + padding,
					(int) Math.round(coords[1]) + padding);
			textController.setHeight(text.getHeight());
			textController.setWidth(text.getWidth());
		}

		getBoundingBox().setRectangle(getBounds());
	}

	@Override
	public GRectangle getBounds() {
		return AwtFactory.getPrototype().newRectangle(getLeft(), getTop(), getWidth(), getHeight());
	}

	@Override
	public void draw(GGraphics2D g2) {
		g2.setStroke(AwtFactory.getPrototype().newBasicStroke(1));
		g2.setColor(GColor.DARK_GRAY);
		g2.drawRect(getLeft(), getTop(), getWidth(), getHeight());
	}

	@Override
	public boolean hit(int x, int y, int hitThreshold) {
		return getBounds().contains(x, y);
	}

	@Override
	public boolean isInside(GRectangle rect) {
		return rect.contains(getBounds());
	}

	@Override
	public GeoElement getGeoElement() {
		return geo;
	}

	@Override
	public BoundingBox getBoundingBox() {
		if (boundingBox == null) {
			boundingBox = createBoundingBox(false, false);
			boundingBox.setNrHandlers(0);
			boundingBox.setRectangle(getBounds());
		}
		boundingBox.updateFrom(geo);
		return boundingBox;
	}

	@Override
	public void remove() {
		if (textController != null) {
			textController.discard();
		}
	}

	@Override
	public void setWidth(int newWidth) {
		text.setWidth(newWidth);
		if (textController != null) {
			textController.setWidth(newWidth);
		}
	}

	@Override
	public void setHeight(int newHeight) {
		text.setHeight(newHeight);
		if (textController != null) {
			textController.setHeight(newHeight);
		}
	}

	@Override
	public int getLeft() {
		GeoPoint point = text.getLocation();
		return view.toScreenCoordX(point.getX());
	}

	@Override
	public int getTop() {
		GeoPoint point = text.getLocation();
		return view.toScreenCoordY(point.getY());
	}

	@Override
	public void setAbsoluteScreenLoc(int x, int y) {
		// Not implemented
	}

	@Override
	public double getOriginalRatio() {
		return 0;
	}

	@Override
	public int getWidth() {
		return text.getWidth() + 2 * padding;
	}

	@Override
	public int getHeight() {
		return text.getHeight() + 2 * padding;
	}

	@Override
	public void resetRatio() {
		// Not implemented
	}

	@Override
	public boolean isFixedRatio() {
		return false;
	}

	@Override
	public void updateByBoundingBoxResize(GPoint2D point, EuclidianBoundingBoxHandler handler) {
		// Not implemented
	}
}
