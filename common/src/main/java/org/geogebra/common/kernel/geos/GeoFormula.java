package org.geogebra.common.kernel.geos;

import org.geogebra.common.awt.GPoint2D;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.NumberValue;
import org.geogebra.common.kernel.arithmetic.ValueType;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.kernel.matrix.Coords;
import org.geogebra.common.plugin.GeoClass;
import org.geogebra.common.util.StringUtil;

public class GeoFormula extends GeoElement implements GeoInline, Translateable, PointRotateable {
	public static final String[] STRINGS = {"e^{\\pi i}+1=0", "E=mc^2", "\\lim_{\\pi\\to 3}5=7"};
	private GPoint2D position;
	private boolean defined = true;
	private String formula;
	private double width = 200;
	private double height = 200;
	private double angle = 0;

	/**
	 * Creates new GeoElement for given construction
	 *
	 * @param c Construction
	 * @param initPoint initial location in RW coordinates
	 */
	public GeoFormula(Construction c, GPoint2D initPoint) {
		super(c);
		formula = STRINGS[(int) (Math.random() * STRINGS.length)];
		this.position = initPoint;
	}

	@Override
	public GeoClass getGeoClassType() {
		return GeoClass.EQUATION;
	}

	@Override
	public GeoElement copy() {
		GeoFormula copy = new GeoFormula(cons, position);
		copy.set(this);
		return copy;
	}

	@Override
	public void set(GeoElementND geo) {
		if (geo instanceof GeoFormula) {
			this.formula = ((GeoFormula) geo).formula;
			this.defined = geo.isDefined();
		} else {
			setUndefined();
		}
	}

	@Override
	public boolean isDefined() {
		return defined;
	}

	@Override
	public void setUndefined() {
		defined = false;
	}

	@Override
	public String toValueString(StringTemplate tpl) {
		return formula;
	}

	@Override
	public ValueType getValueType() {
		return ValueType.TEXT;
	}

	@Override
	protected boolean showInEuclidianView() {
		return true;
	}

	@Override
	public void getXMLtags(StringBuilder sb) {
		super.getXMLtags(sb);
		sb.append("\t<content val=\"");
		StringUtil.encodeXML(sb, formula);
		sb.append("\"/>\n");
		XMLBuilder.appendPosition(sb, this);
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getAngle() {
		return angle;
	}

	@Override
	public GPoint2D getLocation() {
		return position;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public void setAngle(double angle) {
		this.angle = angle;
	}

	@Override
	public void setLocation(GPoint2D startPoint) {
		this.position = startPoint;
	}

	@Override
	public void setContent(String content) {
		formula = content;
	}

	@Override
	public double getMinHeight() {
		return 50;
	}

	@Override
	public boolean isTranslateable() {
		return true;
	}

	@Override
	public void translate(Coords v) {
		position.setLocation(position.getX() + v.getX(), position.getY() + v.getY());
	}

	@Override
	public void rotate(NumberValue r, GeoPointND S) {
		angle -= r.getDouble();
		GeoInlineText.rotate(position, r, S);
	}

	@Override
	public void rotate(NumberValue r) {
		angle -= r.getDouble();
	}
}
