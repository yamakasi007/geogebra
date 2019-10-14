package org.geogebra.common.gui.toolcategorization;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

/**
 * Test class for ToolCategorization.
 */
@RunWith(MockitoJUnitRunner.class)
public class ToolCategorizationTest extends ToolCategorizationTestBase {

    @Mock
    protected ToolCategorization toolCategorization;

    @Before
    public void setupTest() {
        getApp().getSettings().getToolbarSettings().setType(ToolCategorization.AppType.GRAPHING_CALCULATOR);
        toolCategorization = getApp().createToolCategorization();
        toolCategorization.resetTools();
    }

    @Test
    public void testGraphingTools() {
        ArrayList<ToolCategorization.Category> categories = toolCategorization.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            for (int j = 0; j < toolCategorization.getTools(i).size(); j++) {
                Assert.assertFalse(isInGraphingToolSet(toolCategorization.getTools(i).get(j)));
            }
        }
    }
}
