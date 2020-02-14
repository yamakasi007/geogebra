package org.geogebra.common.kernel.geos.groups;

import java.util.ArrayList;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.geos.GeoElement;

/**
 *  model for group of selected geos
 */
public class Group {
    private ArrayList<GeoElement> geosGroup = new ArrayList();
    private boolean isFixed;

    /**
     * Constructor for group
     * @param cons - construction, see {@link Construction}
     * @param selectedGeos - geos selected for group
     */
    public Group(Construction cons, ArrayList<GeoElement> selectedGeos) {
        setFixed(selectedGeos.get(0).isLocked());
        for (GeoElement geo : selectedGeos) {
            geosGroup.add(geo);
            geo.setParentGroup(this);
        }
        cons.addGroupToGroupList(this);
    }

    /**
     * @return list of geos in this group
     */
    public ArrayList<GeoElement> getGeosGroup() {
        return geosGroup;
    }

    /**
     * set as group the geos given
     * @param geos list of selected geos
     */
    public void setGeosGroup(ArrayList<GeoElement> geos) {
        geosGroup = geos;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    public boolean isGroupFixed() {
        return isFixed;
    }

    /**
     * xml representation of group for saving/loading
     * <group l1="A" l2="B" ...></group>
     * @param sb - xml string builder
     */
    public void getXML(StringBuilder sb) {
        sb.append("<group ");
        for (int i = 0; i < getGeosGroup().size(); i++) {
            sb.append("l");
            sb.append(i);
            sb.append("=\"");
            sb.append(getGeosGroup().get(i).getLabelSimple());
            sb.append("\" ");
        }
        sb.append(">\n");
        sb.append("</group>\n");
    }
}
