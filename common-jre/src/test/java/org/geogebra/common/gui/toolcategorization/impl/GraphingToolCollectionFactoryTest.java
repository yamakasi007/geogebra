package org.geogebra.common.gui.toolcategorization.impl;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.euclidian.EuclidianConstants;
import org.geogebra.common.gui.toolcategorization.ToolCollection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for GraphingTools.
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphingToolCollectionFactoryTest extends BaseUnitTest {

    @Mock
    private ToolCollection toolCollection;

    @Mock
    private List<Integer> notAllowedTools;

    @Before
    public void setupTest() {
        toolCollection = new GraphingToolCollectionFactory().createToolCollection();

        notAllowedTools = new ArrayList<>();
        notAllowedTools.add(EuclidianConstants.MODE_SEGMENT);
        notAllowedTools.add(EuclidianConstants.MODE_IMAGE);
        notAllowedTools.add(EuclidianConstants.MODE_ANGLE);
        notAllowedTools.add(EuclidianConstants.MODE_ANGLE_FIXED);
        notAllowedTools.add(EuclidianConstants.MODE_SLOPE);
        notAllowedTools.add(EuclidianConstants.MODE_MIDPOINT);
        notAllowedTools.add(EuclidianConstants.MODE_LINE_BISECTOR);
        notAllowedTools.add(EuclidianConstants.MODE_PARALLEL);
        notAllowedTools.add(EuclidianConstants.MODE_ANGULAR_BISECTOR);
        notAllowedTools.add(EuclidianConstants.MODE_TANGENTS);
        notAllowedTools.add(EuclidianConstants.MODE_LOCUS);
        notAllowedTools.add(EuclidianConstants.MODE_SEGMENT_FIXED);
        notAllowedTools.add(EuclidianConstants.MODE_POLAR_DIAMETER);
        notAllowedTools.add(EuclidianConstants.MODE_POLYLINE);
        notAllowedTools.add(EuclidianConstants.MODE_POLYGON);
        notAllowedTools.add(EuclidianConstants.MODE_REGULAR_POLYGON);
        notAllowedTools.add(EuclidianConstants.MODE_VECTOR_POLYGON);
        notAllowedTools.add(EuclidianConstants.MODE_RIGID_POLYGON);
        notAllowedTools.add(EuclidianConstants.MODE_SHAPE_CIRCLE);
        notAllowedTools.add(EuclidianConstants.MODE_COMPASSES);
        notAllowedTools.add(EuclidianConstants.MODE_SEMICIRCLE);
        notAllowedTools.add(EuclidianConstants.MODE_CIRCLE_POINT_RADIUS_DIRECTION);
        notAllowedTools.add(EuclidianConstants.MODE_CIRCLE_POINT_RADIUS);
        notAllowedTools.add(EuclidianConstants.MODE_CIRCLE_ARC_THREE_POINTS);
        notAllowedTools.add(EuclidianConstants.MODE_CIRCLE_THREE_POINTS);
        notAllowedTools.add(EuclidianConstants.MODE_CIRCLE_SECTOR_THREE_POINTS);
        notAllowedTools.add(EuclidianConstants.MODE_CIRCLE_TWO_POINTS);
        notAllowedTools.add(EuclidianConstants.MODE_CIRCLE_AXIS_POINT);
        notAllowedTools.add(EuclidianConstants.MODE_CIRCUMCIRCLE_ARC_THREE_POINTS);
        notAllowedTools.add(EuclidianConstants.MODE_CIRCUMCIRCLE_SECTOR_THREE_POINTS);
        notAllowedTools.add(EuclidianConstants.MODE_SHAPE_ELLIPSE);
        notAllowedTools.add(EuclidianConstants.MODE_CONIC_FIVE_POINTS);
        notAllowedTools.add(EuclidianConstants.MODE_PARABOLA);
        notAllowedTools.add(EuclidianConstants.MODE_HYPERBOLA_THREE_POINTS);
        notAllowedTools.add(EuclidianConstants.MODE_MIRROR_AT_LINE);
        notAllowedTools.add(EuclidianConstants.MODE_MIRROR_AT_POINT);
        notAllowedTools.add(EuclidianConstants.MODE_MOVE_ROTATE);
        notAllowedTools.add(EuclidianConstants.MODE_TRANSLATE_BY_VECTOR);
        notAllowedTools.add(EuclidianConstants.MODE_DILATE_FROM_POINT);
        notAllowedTools.add(EuclidianConstants.MODE_MIRROR_AT_CIRCLE);
        notAllowedTools.add(EuclidianConstants.MODE_RELATION);
    }

    @Test
    public void testGraphingTools() {
        List<String> categories = toolCollection.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            for (int j = 0; j < toolCollection.getTools(i).size(); j++) {
                Assert.assertFalse(isInToolSet(toolCollection.getTools(i).get(j)));
            }
        }
    }

    private boolean isInToolSet(int tool) {
        if (notAllowedTools.contains(tool)) {
            return true;
        }
        return false;
    }
}
