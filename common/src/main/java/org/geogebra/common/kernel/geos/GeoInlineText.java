package org.geogebra.common.kernel.geos;

import org.geogebra.common.euclidian.EuclidianViewInterfaceCommon;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.ValueType;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.plugin.GeoClass;

public class GeoInlineText extends GeoElement implements AbsoluteScreenLocateable {

	private static final int DEFAULT_WIDTH = 100;
	private static final int DEFAULT_HEIGHT = 30;

	private GeoPoint location;
	private int width;
	private int height;

	/**
	 * Creates new GeoElement for given construction
	 *
	 * @param c Construction
	 */
	public GeoInlineText(Construction c, GeoPoint location) {
		this(c, location, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public GeoInlineText(Construction c, GeoPoint location, int width, int height) {
		super(c);
		this.location = location;
		this.width = width;
		this.height = height;
	}

	public GeoPoint getLocation() {
		return location;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public GeoClass getGeoClassType() {
		return GeoClass.INLINE_TEXT;
	}

	@Override
	public GeoElement copy() {
		return new GeoInlineText(cons, location.copy());
	}

	@Override
	public void set(GeoElementND geo) {
		cons = geo.getConstruction();
		// ToDo
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void setUndefined() {
		//
	}

	@Override
	public String toValueString(StringTemplate tpl) {
		return null;
	}

	@Override
	public ValueType getValueType() {
		return ValueType.TEXT;
	}

	@Override
	public boolean showInAlgebraView() {
		return false;
	}

	@Override
	protected boolean showInEuclidianView() {
		return true;
	}

	@Override
	public boolean isEqual(GeoElementND geo) {
		return false;
	}

	@Override
	public HitType getLastHitType() {
		return HitType.ON_BOUNDARY;
	}

	@Override
	public void setAbsoluteScreenLoc(int x, int y) {
		System.out.println("Absolute");
	}

	@Override
	public int getAbsoluteScreenLocX() {
		return 0;
	}

	@Override
	public int getAbsoluteScreenLocY() {
		return 0;
	}

	@Override
	public void setRealWorldLoc(double x, double y) {
		System.out.println("Real");
	}

	@Override
	public double getRealWorldLocX() {
		return 0;
	}

	@Override
	public double getRealWorldLocY() {
		return 0;
	}

	@Override
	public void setAbsoluteScreenLocActive(boolean flag) {

	}

	@Override
	public boolean isAbsoluteScreenLocActive() {
		return false;
	}

	@Override
	public int getTotalHeight(EuclidianViewInterfaceCommon view) {
		return 0;
	}

	@Override
	public int getTotalWidth(EuclidianViewInterfaceCommon view) {
		return 0;
	}

	@Override
	public boolean isFurniture() {
		return true;
	}
}
