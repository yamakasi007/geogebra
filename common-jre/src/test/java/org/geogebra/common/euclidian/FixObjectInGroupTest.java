package org.geogebra.common.euclidian;

import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;

import org.geogebra.common.factories.AwtFactoryCommon;
import org.geogebra.common.jre.headless.AppCommon;
import org.geogebra.common.jre.headless.LocalizationCommon;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.groups.Group;
import org.junit.Before;
import org.junit.Test;

public class FixObjectInGroupTest {

	private ArrayList<GeoElement> geos;
	private Construction construction;
	private Group group;
	private AppCommon app;

	@Before
	public void setup() {
		AwtFactoryCommon factoryCommon = new AwtFactoryCommon();
		app = new AppCommon(new LocalizationCommon(2), factoryCommon);
		construction = app.getKernel().getConstruction();
		withGroupSized(5);
	}

	private void withGroupSized(final int count) {
		geos = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			geos.add(LayerManagerTest.createDummyGeo(construction, i));
		}
		group = new Group(geos);
		construction.addGroupToGroupList(group);
	}

	@Test
	public void propertiesShouldChangeIndependently() {
		withGroupSized(3);
		group.setFixed(true);
		GeoElement selected = geos.get(1);
		app.getSelectionManager().setFocusedGroupElement(selected);
		selected.setLineThickness(20);
		assertNotEquals(20, geos.get(1).getLineThickness());
	}
}
