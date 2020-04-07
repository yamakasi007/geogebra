package org.geogebra.common.euclidian.draw;

import org.geogebra.common.awt.GFont;
import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.awt.GPoint2D;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.Drawable;
import org.geogebra.common.euclidian.EuclidianBoundingBoxHandler;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.euclidian.TextBoundingBox;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFormula;

public class DrawFormula extends Drawable {

	private final TransformableRectangle rectangle;
	private GFont textFont;
	private TextBoundingBox boundingBox;

	/**
	 * @param ev view
	 * @param equation equation
	 */
	public DrawFormula(EuclidianView ev, GeoFormula equation) {
		super(ev, equation);
		textFont = ev.getFont();
		this.rectangle = new TransformableRectangle(view, equation);
		update();
	}

	@Override
	public void update() {
		updateStrokes(geo);
		labelDesc = geo.toValueString(StringTemplate.defaultTemplate);
		rectangle.update();
		updateBoundingBox();
	}

	private void updateBoundingBox() {
		if (boundingBox != null) {
			boundingBox.setRectangle(getBounds());
			boundingBox.setTransform(rectangle.directTransform);
		}
	}

	@Override
	public void draw(GGraphics2D g2) {
		g2.setPaint(geo.getObjectColor());
		g2.setFont(textFont);
		g2.setStroke(objStroke); // needed eg for \sqrt
		g2.saveTransform();
		g2.transform(rectangle.directTransform);
		drawMultilineLaTeX(g2, textFont, geo.getObjectColor(),
				view.getBackgroundCommon());
		g2.restoreTransform();
	}

	@Override
	public GRectangle getBounds() {
		return rectangle.getBounds();
	}

	@Override
	public boolean hit(int x, int y, int hitThreshold) {
		return rectangle.hit(x, y);
	}

	@Override
	public boolean isInside(GRectangle rect) {
		return false;
	}

	@Override
	public GeoElement getGeoElement() {
		return geo;
	}

	@Override
	public TextBoundingBox getBoundingBox() {
		if (boundingBox == null) {
			boundingBox = new TextBoundingBox();
			boundingBox.setRectangle(getBounds());
			boundingBox.setColor(view.getApplication().getPrimaryColor());
		}
		boundingBox.updateFrom(geo);
		return boundingBox;
	}

	@Override
	public void updateByBoundingBoxResize(GPoint2D point, EuclidianBoundingBoxHandler handler) {
		rectangle.updateByBoundingBoxResize(point, handler);
		updateBoundingBox();
	}
}
