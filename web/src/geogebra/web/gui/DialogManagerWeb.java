package geogebra.web.gui;

import geogebra.common.awt.Point;
import geogebra.common.gui.dialog.DialogManager;
import geogebra.common.kernel.Construction;
import geogebra.common.kernel.Matrix.Coords;
import geogebra.common.kernel.arithmetic.NumberValue;
import geogebra.common.kernel.geos.GeoBoolean;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoFunction;
import geogebra.common.kernel.geos.GeoPoint2;
import geogebra.common.kernel.geos.GeoPolygon;
import geogebra.common.kernel.geos.GeoSegment;
import geogebra.common.kernel.geos.GeoText;
import geogebra.common.kernel.kernelND.GeoPointND;
import geogebra.common.main.AbstractApplication;
import geogebra.common.gui.dialog.handler.NumberInputHandler;
import geogebra.web.gui.dialog.ButtonDialog;
import geogebra.web.gui.dialog.InputDialogAngleFixed;
import geogebra.web.gui.dialog.InputDialogRotate;
import geogebra.web.gui.dialog.AngleInputDialog;
import geogebra.web.gui.dialog.SliderDialog;
import geogebra.web.main.Application;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;

public class DialogManagerWeb extends DialogManager {

	public DialogManagerWeb(AbstractApplication app) {
	    super(app);
    }

	@Override
    public boolean showFunctionInspector(GeoFunction geoFunction) {
	    // TODO Auto-generated method stub
	    return false;
    }

	@Override
    public void showPropertiesDialog(ArrayList<GeoElement> geos) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void showBooleanCheckboxCreationDialog(Point loc, GeoBoolean bool) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public NumberValue showNumberInputDialog(String title, String message,
            String initText) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Object[] showAngleInputDialog(String title, String message,
            String initText) {

		// avoid labeling of num
		Construction cons = app.getKernel().getConstruction();
		boolean oldVal = cons.isSuppressLabelsActive();
		cons.setSuppressLabelCreation(true);

		NumberInputHandler handler = new NumberInputHandler(app.getKernel()
				.getAlgebraProcessor());
		AngleInputDialog id = new AngleInputDialog(((Application) app), message, title,
				initText, false, handler, true);
		id.setVisible(true);

		cons.setSuppressLabelCreation(oldVal);
		Object[] ret = { handler.getNum(), id };
		return ret;
	}

	@Override
    public boolean showButtonCreationDialog(int x, int y, boolean textfield) {
		ButtonDialog dialog = new ButtonDialog(((Application) app), x, y, textfield);
		dialog.setVisible(true);
		return true;
    }

	@Override
    protected String prompt(String message, String def) {
	    return Window.prompt(message, def);
    }

	@Override
    protected boolean confirm(String string) {
	    return Window.confirm(string);
    }

	@Override
    public void closeAll() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void showRenameDialog(GeoElement geo, boolean b, String label,
            boolean c) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
	protected void showTextDialog(GeoText geo, GeoPointND startPoint) {
		
		String inputValue = prompt("Enter text", "");

		if ((inputValue != null) ? !"".equals(inputValue) : false) {
			
			if (inputValue.indexOf('\"') == -1) {
				inputValue = "\"" + inputValue + "\"";
			}

			GeoElement[] ret = app.getKernel().getAlgebraProcessor()
					.processAlgebraCommand(inputValue, false);
			if (ret != null && ret[0].isTextValue()) {
				GeoText t = (GeoText) ret[0];

				if (startPoint.isLabelSet()) {
					try {
						t.setStartPoint(startPoint);
					} catch (Exception e) {
					}
				} else {

					Coords coords = startPoint.getInhomCoordsInD(3);
					t.setRealWorldLoc(coords.getX(), coords.getY());
					t.setAbsoluteScreenLocActive(false);
				}

				// make sure (only) the output of the text tool is selected
				app.getActiveEuclidianView()
						.getEuclidianController()
						.memorizeJustCreatedGeos(ret);

				t.updateRepaint();
				app.storeUndoInfo();
			}
		}

	}

	@Override
    public void showOptionsDialog(int tabEuclidian) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void showPropertiesDialog() {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void showToolbarConfigDialog() {
	    // TODO Auto-generated method stub
	    
    }


	@Override
    public NumberValue showNumberInputDialog(String title, String message,
            String initText, boolean changingSign, String checkBoxText) {
	    // TODO Auto-generated method stub
	    return null;
    }

	/**
	 * Creates a new slider at given location (screen coords).
	 * 
	 * @return whether a new slider (number) was create or not
	 */
	@Override
    public boolean showSliderCreationDialog(int x, int y) {
		app.setWaitCursor();

		SliderDialog dialog = new SliderDialog(((Application) app), x, y);
		dialog.center();

		app.setDefaultCursor();

		return true;
	}

	@Override
	public void showNumberInputDialogRotate(String title, GeoPolygon[] polys,
			GeoPoint2[] points, GeoElement[] selGeos) {

		NumberInputHandler handler = new NumberInputHandler(app.getKernel()
				.getAlgebraProcessor());
		InputDialogRotate id = new InputDialogRotate(((Application) app), title, handler, polys,
				points, selGeos, app.getKernel());
		id.setVisible(true);

	}

	@Override
	public void showNumberInputDialogAngleFixed(String title,
			GeoSegment[] segments, GeoPoint2[] points, GeoElement[] selGeos) {

		NumberInputHandler handler = new NumberInputHandler(app.getKernel()
				.getAlgebraProcessor());
		InputDialogAngleFixed id = new InputDialogAngleFixed(((Application) app), title, handler,
				segments, points, selGeos, app.getKernel());
		id.setVisible(true);

	}
}
