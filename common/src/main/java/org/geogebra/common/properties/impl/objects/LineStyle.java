package org.geogebra.common.properties.impl.objects;

import org.geogebra.common.kernel.geos.GProperty;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.properties.EnumerableProperty;

/**
 * Line style
 */
public class LineStyle extends AbstractGeoElementProperty implements EnumerableProperty {

    public LineStyle(GeoElement geoElement) {
        super("Properties.Style", geoElement);
    }

    @Override
    public String[] getValues() {
        return new String[0];
    }

    @Override
    public int getIndex() {
        return getElement().getLineType();
    }

    @Override
    public void setIndex(int style) {
        GeoElement element = getElement();
        element.setLineType(style);
        element.updateVisualStyleRepaint(GProperty.LINE_STYLE);
        element.getApp().setPropertiesOccured();
    }
}
