package org.geogebra.common.main.saveLoad.group;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;
import com.himamis.retex.renderer.web.graphics.JLMContext2d;

import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.algos.AlgoJoinPoints;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.geos.groups.Group;
import org.geogebra.web.full.main.AppWFull;
import org.geogebra.web.html5.main.TestArticleElement;
import org.geogebra.web.test.AppMocker;
import org.geogebra.web.util.file.FileIO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(GwtMockitoTestRunner.class)
@WithClassesToStub({JLMContext2d.class, RootPanel.class})
public class GroupSaveTest {
    private static AppWFull app;
    private static Construction cons;

    @Test
    public void testSaveGroup() {
        TestArticleElement articleElement = new TestArticleElement("prerelease", "notes");
        app = AppMocker.mockApplet(articleElement);
        cons = app.getKernel().getConstruction();
        GeoPoint A = new GeoPoint(cons, "A", 0, 0, 1);
        GeoPoint B = new GeoPoint(cons, "B", 3, 0, 1);
        GeoPoint C = new GeoPoint(cons, "C", 3, 3, 1);
        AlgoJoinPoints line = new AlgoJoinPoints(cons, "g", B, C);
        ArrayList<GeoElement> geos = new ArrayList<>();
        geos.add(A);
        geos.add(line.getOutput(0));
        new Group(cons, geos);
        String pathString = "src/test/java/org/geogebra/common/main/saveLoad/group/groupXML" +
                ".txt";
        String fileContent = FileIO.load(pathString);
        StringBuilder sb = new StringBuilder();
        app.getKernel().getConstruction().getConstructionXML(sb, false);
        Assert.assertEquals(sb.toString(), fileContent);
    }
}
