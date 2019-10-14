package org.geogebra.common.gui.toolcategorization.impl;

import org.geogebra.common.gui.toolcategorization.ToolCategorizationTestBase;
import org.geogebra.common.gui.toolcategorization.ToolCollection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

/**
 * Test class for GraphingTools.
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphingToolCollectionFactoryTest extends ToolCategorizationTestBase {

    @Mock
    private ToolCollection toolCollection;

    @Before
    public void setupTest() {
        toolCollection = new GraphingToolCollectionFactory().createToolCollection();
    }

    @Test
    public void testGraphingTools() {
        List<String> categories = toolCollection.getCategories();
        for (int i = 0; i < categories.size(); i++) {
            for (int j = 0; j < toolCollection.getTools(i).size(); j++) {
                Assert.assertFalse(isInGraphingToolSet(toolCollection.getTools(i).get(j)));
            }
        }
    }
}
