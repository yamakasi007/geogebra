package org.geogebra.common.euclidian.draw;

import java.util.Arrays;
import java.util.List;

import org.geogebra.common.awt.GAffineTransform;
import org.geogebra.common.awt.GPoint2D;
import org.geogebra.common.awt.GRectangle;
import org.geogebra.common.euclidian.EuclidianBoundingBoxHandler;
import org.geogebra.common.euclidian.EuclidianView;
import org.geogebra.common.factories.AwtFactory;
import org.geogebra.common.kernel.geos.GeoInline;
import org.geogebra.common.kernel.geos.GeoInlineText;
import org.geogebra.common.util.debug.Log;

public class TransformableRectangle {
	private final EuclidianView view;
	private final GeoInline text;
	protected GAffineTransform directTransform;
	private GAffineTransform inverseTransform;

	private GPoint2D corner0;
	private GPoint2D corner1;
	private GPoint2D corner2;
	private GPoint2D corner3;

	/**
	 * @param view view
	 * @param text text or formula
	 */
	TransformableRectangle(EuclidianView view, GeoInline text) {
		this.view = view;
		this.text = text;
	}

	/**
	 * Update transforms
	 */
	public void update() {
		GPoint2D point = text.getLocation();

		double angle = text.getAngle();
		double width = text.getWidth();
		double height = text.getHeight();

		directTransform = AwtFactory.getPrototype().newAffineTransform();
		directTransform.translate(view.toScreenCoordXd(point.getX()),
				view.toScreenCoordYd(point.getY()));
		directTransform.rotate(angle);

		try {
			inverseTransform = directTransform.createInverse();
		} catch (Exception e) {
			Log.error(e.getMessage());
		}

		corner0 = directTransform.transform(new GPoint2D(0, 0), null);
		corner1 = directTransform.transform(new GPoint2D(width, 0), null);
		corner2 = directTransform.transform(new GPoint2D(width, height), null);
		corner3 = directTransform.transform(new GPoint2D(0, height), null);
	}

	/**
	 * @return height (in screen coords)
	 */
	public int getHeight() {
		return (int) (Math.max(Math.max(corner0.getY(), corner1.getY()),
				Math.max(corner2.getY(), corner3.getY()))
				- Math.min(Math.min(corner0.getY(), corner1.getY()),
				Math.min(corner2.getY(), corner3.getY())));
	}

	/**
	 * @return width (in screen coords)
	 */
	public int getWidth() {
		return (int) (Math.max(Math.max(corner0.getX(), corner1.getX()),
				Math.max(corner2.getX(), corner3.getX()))
				- Math.min(Math.min(corner0.getX(), corner1.getX()),
				Math.min(corner2.getX(), corner3.getX())));
	}

	/**
	 * @return left (in screen coords)
	 */
	public int getLeft() {
		return (int) Math.min(Math.min(corner0.getX(), corner1.getX()),
				Math.min(corner2.getX(), corner3.getX()));
	}

	/**
	 * @return top (in screen coords)
	 */
	public int getTop() {
		return (int) Math.min(Math.min(corner0.getY(), corner1.getY()),
				Math.min(corner2.getY(), corner3.getY()));
	}

	public List<GPoint2D> getCorners() {
		return Arrays.asList(corner0, corner1, corner3);
	}

	/**
	 * Whether the rectangle was hit
	 */
	public boolean hit(int x, int y) {
		GPoint2D p = inverseTransform.transform(new GPoint2D(x, y), null);
		return 0 < p.getX() && p.getX() < text.getWidth()
				&& 0 < p.getY() && p.getY() < text.getHeight();
	}

	/**
	 * @return bounds on screen
	 */
	public GRectangle getBounds() {
		return AwtFactory.getPrototype().newRectangle(getLeft(), getTop(),
				getWidth(), getHeight());
	}

	public void updateByBoundingBoxResize(GPoint2D point, EuclidianBoundingBoxHandler handler) {
		GPoint2D transformed = inverseTransform.transform(point, null);

		double x = 0;
		double y = 0;
		double width = text.getWidth();
		double height = text.getHeight();

		if (handler.getDx() == 1) {
			width = transformed.getX();
		} else if (handler.getDx() == -1) {
			width = text.getWidth() - transformed.getX();
			x = transformed.getX();
		}

		if (handler.getDy() == 1) {
			height = transformed.getY();
		} else if (handler.getDy() == -1) {
			height = text.getHeight() - transformed.getY();
			y = transformed.getY();
		}

		if (height < text.getMinHeight() && width < text.getWidth()) {
			return;
		}

		if (height < text.getMinHeight()) {
			if (y != 0) {
				y = text.getHeight() - text.getMinHeight();
			}
			height = text.getMinHeight();
		}

		if (width < GeoInlineText.DEFAULT_WIDTH) {
			if (x != 0) {
				x = text.getWidth() - GeoInlineText.DEFAULT_WIDTH;
			}
			width = GeoInlineText.DEFAULT_WIDTH;
		}

		GPoint2D origin = directTransform.transform(new GPoint2D(x, y), null);

		text.setLocation(new GPoint2D(view.toRealWorldCoordX(origin.getX()),
				view.toRealWorldCoordY(origin.getY())));
		text.setWidth(width);
		text.setHeight(height);
		text.updateRepaint();
		update();
	}

	public GPoint2D getInversePoint(int x, int y) {
		return inverseTransform.transform(new GPoint2D(x , y ), null);
	}
}
