package org.geogebra.web.html5.main;

import com.google.gwt.core.client.JavaScriptObject;

import elemental2.promise.Promise;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

/**
 * Maps GeoGebra functions to exported JS api
 */
@JsType
public class ApiExporter {

	private GgbAPIW ggbAPI;

	public ApiExporter(GgbAPIW ggbAPI) {
		this.ggbAPI = ggbAPI;
	}

	public String getXML(String objName) {
		if (Js.isTruthy(objName)) {
			return ggbAPI.getXML(objName + "");
		} else {
			return ggbAPI.getXML();
		}
	}

	public String getAlgorithmXML(String objName) {
		return ggbAPI.getAlgorithmXML(objName + "");
	}

	public String getPerspectiveXML() {
		return ggbAPI.getPerspectiveXML();
	}

	public String getBase64(JavaScriptObject param1, JavaScriptObject param2) {
		if (Js.isTripleEqual(param2, false)) {
			return ggbAPI.getBase64(false);
		}
		if (Js.isTripleEqual(param2, true)) {
			return ggbAPI.getBase64(true);
		}
		if (Js.isTruthy(param2)) {
			ggbAPI.getBase64(Js.asBoolean(param1), param2);
		} else if (Js.isTruthy(param1)) {
			ggbAPI.getBase64(false, param1);
		} else {
			return ggbAPI.getBase64();
		}

		return null;
	}

	public void setBase64(String base64string, JavaScriptObject callback) {
		ggbAPI.setBase64(base64string + "", callback);
	}

	public void openFile(String filename, JavaScriptObject callback) {
		ggbAPI.openFile(filename + "", callback);
	};

	public void login(String token, Object ui) {
		ggbAPI.login(token  + "", Js.asBoolean(ui));
	}

	public void logout() {
		ggbAPI.logout();
	};

	public void setXML(String xml) {
		ggbAPI.setXML(xml + "");
	}

	public void evalXML(String xmlString) {
		ggbAPI.evalXML(xmlString + "");
	}

	public void setDisplayStyle(String objName, String style) {
		ggbAPI.setDisplayStyle(objName + "", style + "");
	}

	public boolean evalCommand(String cmdString) {
		return ggbAPI.evalCommandNoException(cmdString + "");
	}

	public String evalCommandGetLabels(String cmdString) {
		return ggbAPI.evalCommandGetLabelsNoException(cmdString + "");
	}

	public Promise<String> asyncEvalCommand(String cmdString) {
		return new Promise<>((resolve, reject) -> {
			ggbAPI.asyncEvalCommand(cmdString + "", resolve, reject);
		});
	};

	public Promise<String> asyncEvalCommandGetLabels(String cmdString) {
		return new Promise<>((resolve, reject) -> {
			ggbAPI.asyncEvalCommandGetLabels(cmdString + "", resolve, reject);
		});
	};

	public String evalCommandCAS(String cmdString) {
		return ggbAPI.evalCommandCAS(cmdString + "");
	}

	public String evalGeoGebraCAS(String cmdString) {
		return ggbAPI.evalGeoGebraCAS(cmdString + "");
	}

	public void setFixed(String objName, Object flag, Object selection) {
		if ("undefined".equals(Js.typeof(selection))) {
			ggbAPI.setFixed(objName + "", Js.asBoolean(flag));
		} else {
			ggbAPI.setFixed(objName + "", Js.asBoolean(flag), Js.asBoolean(selection));
		}
	};

	public void setOnTheFlyPointCreationActive(Object flag) {
		ggbAPI.setOnTheFlyPointCreationActive(Js.asBoolean(flag));
	}

	public void setUndoPoint() {
		ggbAPI.setUndoPoint();
	}

	public String setSaved () {
		ggbAPI.setSaved()();
	};

	public String initCAS () {
		ggbAPI.initCAS()();
	};

	public String uploadToGeoGebraTube () {
		ggbAPI.uploadToGeoGebraTube()();
	};

	public String setErrorDialogsActive (flag) {
		ggbAPI.setErrorDialogsActive(Z)(!!flag);
	};

	public String reset () {//TODO: implement this in Desktop and Web
		ggbAPI.reset()();
	};

	public String refreshViews () {
		ggbAPI.refreshViews()();
	};

	public String setVisible (objName, visible) {
		ggbAPI.setVisible(Ljava/lang/String;Z)(objName + "", !!visible);
	};

	public String getVisible (objName, view) {
		if (typeof view !== 'undefined') {
			return ggbAPI.getVisible(Ljava/lang/String;I)(objName + "",view);
		}
		return ggbAPI.getVisible(objName + "");
	};

	public String setLayer (objName, layer) {
		ggbAPI.setLayer(Ljava/lang/String;I)(objName + "",layer);
	};

	public String getLayer (objName) {
		return ggbAPI.getLayer(objName + "");
	};

	public String setLayerVisible (layer, visible) {
		ggbAPI.setLayerVisible(IZ)(layer, !!visible);
	};

	public String setTrace (objName, flag) {
		ggbAPI.setTrace(Ljava/lang/String;Z)(objName + "", !!flag);
	};

	public String isTracing (objName) {
		return ggbAPI.isTracing(objName + "");
	};

	public String setLabelVisible (objName, visible) {
		ggbAPI.setLabelVisible(Ljava/lang/String;Z)(objName + "", !!visible);
	};

	public String setLabelStyle (objName, style) {
		ggbAPI.setLabelStyle(Ljava/lang/String;I)(objName + "",style);
	};

	public String getLabelStyle (objName) {
		return ggbAPI.getLabelStyle(objName + "");
	};

	public String getLabelVisible (objName) {
		return ggbAPI.getLabelVisible(objName + "");
	};

	public String setColor (objName, red, green, blue) {
		ggbAPI.setColor(Ljava/lang/String;III)(objName + "",red,green,blue);
	};

	public String setCorner (objName, x, y, index) {
		if (!index) {
			index = 1;
		}
		ggbAPI.setCorner(Ljava/lang/String;DDI)(objName + "",x,y,index);
	};

	public String setLineStyle (objName, style) {
		ggbAPI.setLineStyle(Ljava/lang/String;I)(objName + "",style);
	};

	public String setLineThickness (objName, thickness) {
		ggbAPI.setLineThickness(Ljava/lang/String;I)(objName + "",thickness);
	};

	public String setPointStyle (objName, style) {
		ggbAPI.setPointStyle(Ljava/lang/String;I)(objName + "",style);
	};

	public String setPointSize (objName, style) {
		ggbAPI.setPointSize(Ljava/lang/String;I)(objName + "",style);
	};

	public String setFilling (objName, filling) {
		ggbAPI.setFilling(Ljava/lang/String;D)(objName + "",filling);
	};

	public String getColor (objName) {
		return ggbAPI.getColor(objName + "");
	};

	public String getPenColor () {
		return ggbAPI.getPenColor()();
	};

	public String getPenSize () {
		return ggbAPI.getPenSize()();
	};

	public String setPenSize (size) {
		ggbAPI.setPenSize(I)(size);
	};

	public String setPenColor (red, green, blue) {
		ggbAPI.setPenColor(III)(red,green,blue);
	};

	public String getFilling (objName) {
		return ggbAPI.getFilling(objName + "");
	};

	public String getImageFileName (objName) {
		return ggbAPI.getImageFileName(objName + "");
	};

	public String getLineStyle (objName) {
		return ggbAPI.getLineStyle(objName + "");
	};

	public String getLineThickness (objName) {
		return ggbAPI.getLineThickness(objName + "");
	};

	public String getPointStyle (objName) {
		return ggbAPI.getPointStyle(objName + "");
	};

	public String getPointSize (objName) {
		return ggbAPI.getPointSize(objName + "");
	};

	public String deleteObject (objName) {
		ggbAPI.deleteObject(objName + "");
	};

	public String setAnimating (objName, animate) {
		ggbAPI.setAnimating(Ljava/lang/String;Z)(objName + "", !!animate);
	};

	public String setAnimationSpeed (objName, speed) {
		ggbAPI.setAnimationSpeed(Ljava/lang/String;D)(objName + "",speed);
	};

	public String startAnimation () {
		ggbAPI.startAnimation()();
	};

	public String stopAnimation () {
		ggbAPI.stopAnimation()();
	};

	public String setAuxiliary (objName, auxiliary) {
		ggbAPI.setAuxiliary(Ljava/lang/String;Z)(objName + "", !!auxiliary);
	};

	public String hideCursorWhenDragging (hideCursorWhenDragging) {
		ggbAPI.hideCursorWhenDragging(Z)(!!hideCursorWhenDragging);
	};

	public String isAnimationRunning () {
		return ggbAPI.isAnimationRunning()();
	};

	public String getFrameRate () {
		return ggbAPI.getFrameRate()();
	};

	public String renameObject (oldName, newName, force) {
		return ggbAPI.renameObject(Ljava/lang/String;Ljava/lang/String;Z)(oldName + "",newName + "", !!force);
	};

	public String exists (objName) {
		return ggbAPI.exists(objName + "");
	};

	public String isDefined (objName) {
		return ggbAPI.isDefined(objName + "");
	};

	public String getValueString (objName, localized) {
		localized = Boolean(localized) || typeof localized === 'undefined'
		return ggbAPI.getValueString(Ljava/lang/String;Z)(objName + "", localized);
	};

	public String getListValue (objName, index) {
		return ggbAPI.getListValue(Ljava/lang/String;I)(objName + "", index);
	};

	public String getDefinitionString (objName, localize) {
		return ggbAPI.getDefinitionString(Ljava/lang/String;Z)(objName + "",  typeof localize === 'undefined' ? true : !!localize);
	};

	public String getLaTeXString (objName) {
		return ggbAPI.getLaTeXString(objName + "");
	};

	public String getLaTeXBase64 (objName, value) {
		return ggbAPI.getLaTeXBase64(Ljava/lang/String;Z)(objName + "", !!value);
	};

	public String getCommandString (objName, localize) {
		return ggbAPI.getCommandString(Ljava/lang/String;Z)(objName + "", typeof localize === 'undefined' ? true : !!localize);
	};

	public String getCaption (objName, subst) {
		return ggbAPI.getCaption(Ljava/lang/String;Z)(objName + "", !!subst);
	};

	public String setCaption (objName, caption) {
		ggbAPI.setCaption(Ljava/lang/String;Ljava/lang/String;)(objName + "", caption + "");
	};

	public String getXcoord (objName) {
		return ggbAPI.getXcoord(objName + "");
	};

	public String getYcoord (objName) {
		return ggbAPI.getYcoord(objName + "");
	};

	public String getZcoord (objName) {
		return ggbAPI.getZcoord(objName + "");
	};

	public String setCoords (objName, x, y, z) {
		if (typeof z === 'undefined') {
			ggbAPI.setCoords(Ljava/lang/String;DD)(objName + "",x,y);
		} else {
			ggbAPI.setCoords(Ljava/lang/String;DDD)(objName + "",x,y,z);
		}
	};

	public String getValue (objName) {
		return ggbAPI.getValue(objName + "");
	};

	public String getVersion () {
		return ggbAPI.getVersion()();
	};

	public String getScreenshotBase64 (callback) {
		ggbAPI.getScreenshotBase64(Lcom/google/gwt/core/client/JavaScriptObject;)(callback);
	};

	public String getThumbnailBase64 () {
		return ggbAPI.getThumbnailBase64()();
	};

	public String setValue (objName, x) {
		// #4035
		// need to support possible syntax error
		// eg setValue("a","3") rather than setValue("a",3)
		if (typeof x === "string") {
			if (x === "true") {
				x = true;
			} else if (x === "false") {
				x = false;
			} else {
				// force string -> number (might give NaN)
				x = x * 1;
			}
		}

		if (typeof x !== "number" && typeof x !== "boolean") {
			// avoid possible strange effects
			return;
		}
		ggbAPI.setValue(Ljava/lang/String;D)(objName + "",x);
	};

	public String setTextValue (objName, x) {

		x = x + "";

		if (typeof objName !== "string") {
			// avoid possible strange effects
			return;
		}
		ggbAPI.setTextValue(Ljava/lang/String;Ljava/lang/String;)(objName + "",x);
	};

	public String setListValue (objName, x, y) {
		// #4035
		// need to support possible syntax error
		if (typeof x === "string") {
			if (x === "true") {
				x = 1;
			} else if (x === "false") {
				x = 0;
			} else {
				// force string -> number (might give NaN)
				x = x * 1;
			}
		}

		if (typeof y === "string") {
			if (y === "true") {
				y = 1;
			} else if (y === "false") {
				y = 0;
			} else {
				// force string -> number (might give NaN)
				y = y * 1;
			}
		}

		if (typeof x !== "number" || typeof y !== "number") {
			// avoid possible strange effects
			return;
		}
		ggbAPI.setListValue(Ljava/lang/String;DD)(objName + "",x,y);
	};

	public String setRepaintingActive (flag) {
		ggbAPI.setRepaintingActive(Z)(!!flag);
	};

	public String setCoordSystem (xmin, xmax, ymin, ymax, zmin, zmax,
			verticalY) {
		if (typeof zmin !== "number") {
			ggbAPI.setCoordSystem(DDDD)(xmin,xmax,ymin,ymax);
		} else {
			ggbAPI.setCoordSystem(DDDDDDZ)(xmin,xmax,ymin,ymax,zmin,zmax, !!verticalY);
		}
	};

	public String setAxesVisible (arg1, arg2, arg3, arg4) {
		if (typeof arg3 === "undefined") {
			ggbAPI.setAxesVisible(ZZ)(!!arg1, !!arg2);
		} else {
			ggbAPI.setAxesVisible(IZZZ)(arg1, !!arg2, !!arg3, !!arg4);
		}
	};

	public String setAxisUnits (arg1, arg2, arg3, arg4) {
		ggbAPI.setAxisUnits(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)(arg1, arg2 + "", arg3 + "", arg4 + "");
	};

	public String setAxisLabels (arg1, arg2, arg3, arg4) {
		ggbAPI.setAxisLabels(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)(arg1, arg2 + "", arg3 + "", arg4 + "");
	};

	public String setAxisSteps (arg1, arg2, arg3, arg4) {
		ggbAPI.setAxisSteps(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)(arg1, arg2 + "",arg3 + "", arg4 + "");
	};

	api.getAxisUnits = $entry(function(arg1) {
		return []
				.concat(ggbAPI.getAxisUnits(I)(1 * arg1 || 1));
	});

	api.getAxisLabels = $entry(function(arg1) {
		return []
				.concat(ggbAPI.getAxisLabels(I)(1 * arg1 || 1));
	});

	public String setPointCapture (view, capture) {
		if (typeof capture === "undefined") {
			ggbAPI.setPointCapture(II)(1, view);
		} else {
			ggbAPI.setPointCapture(II)(view, capture);
		}
	};

	public String getGridVisible (view) {
		return ggbAPI.getGridVisible(I)(view || 1);
	};

	public String setGridVisible (arg1, arg2) {
		if (typeof arg2 === "undefined") {
			ggbAPI.setGridVisible(Z)(!!arg1);
		} else {
			ggbAPI.setGridVisible(IZ)(arg1, !!arg2);
		}
	};

	public String getAllObjectNames (objectType) {
		if (typeof objectType === "undefined") {
			return []
					.concat(ggbAPI.getAllObjectNames()());
		} else {
			return []
					.concat(ggbAPI.getAllObjectNames(objectType + ""));
		}

	};

	public String getObjectNumber () {
		return ggbAPI.getObjectNumber()();
	};

	public String getObjectName (i) {
		return ggbAPI.getObjectName(I)(i);
	};

	public String getObjectType (objName) {
		return ggbAPI.getObjectType(objName + "");
	};

	public String setMode (mode) {
		ggbAPI.setMode(I)(mode);
	};

	public String getMode () {
		return ggbAPI.getMode()();
	};

	public String getToolName (i) {
		return ggbAPI.getToolName(I)(i);
	};

	public String openMaterial (material) {
		ggbAPI.openMaterial(material + "");
	};

	public String undo (repaint) {
		ggbAPI.undo(Z)(!!repaint);
	};

	public String redo (repaint) {
		ggbAPI.redo(Z)(!!repaint);
	};

	public String newConstruction () {
		ggbAPI.newConstruction()();
	};

	public String debug (str) {
		ggbAPI.debug(str + "");
	};

	public String setWidth (width) {
		ggbAPI.setWidth(I)(width);
	};

	public String setHeight (height) {
		ggbAPI.setHeight(I)(height);
	};

	public String setSize (width, height) {
		ggbAPI.setSize(II)(width, height);
	};

	public String enableRightClick (enable) {
		ggbAPI.enableRightClick(Z)(!!enable);
	};

	public String enableLabelDrags (enable) {
		ggbAPI.enableLabelDrags(Z)(!!enable);
	};

	public String enableShiftDragZoom (enable) {
		ggbAPI.enableShiftDragZoom(Z)(!!enable);
	};

	public String showToolBar (show) {
		ggbAPI.showToolBar(Z)(!!show);
	};

	public String setCustomToolBar (toolbarDef) {
		ggbAPI.setCustomToolBar(toolbarDef + "");
	};

	public String showMenuBar (show) {
		ggbAPI.showMenuBar(Z)(!!show);
	};

	public String showAlgebraInput (show) {
		ggbAPI.showAlgebraInput(Z)(!!show);
	};

	public String showResetIcon (show) {
		ggbAPI.showResetIcon(Z)(!!show);
	};

	public String getViewProperties (show) {
		return ggbAPI.getViewProperties(I)(show);
	};

	public String setFont (label, size, bold, italic, serif) {
		ggbAPI.setFont(Ljava/lang/String;IZZZ)(label + "", size, !!bold, !!italic, !!serif);
	};

	public String insertImage (url, corner1, corner2, corner4) {
		return ggbAPI.insertImage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(url + "", corner1+"", corner2+"", corner4+"");
	};

	public String addImage (fileName, url) {
		ggbAPI.addImage(*)(fileName + "", url + "");
	};

	public String recalculateEnvironments () {
		ggbAPI.recalculateEnvironments()();
	};

	public String isIndependent (label) {
		return ggbAPI.isIndependent(label + "");
	};

	public String isMoveable (label) {
		return ggbAPI.isMoveable(label + "");
	};

	public String setPerspective (code) {
		ggbAPI.setPerspective(code + "");
	};

	public String enableCAS (enable) {
		ggbAPI.enableCAS(Z)(!!enable);
	};

	public String enable3D (enable) {
		ggbAPI.enable3D(Z)(!!enable);
	};

	public String getPNGBase64 (exportScale, transparent, dpi,
			copyToClipboard, greyscale) {
		return ggbAPI.getPNGBase64(DZDZZ)(exportScale, !!transparent, dpi, !!copyToClipboard, !!greyscale);
	};

	public String exportGIF (sliderLabel, scale, timeBetweenFrames, isLoop, filename, rotate) {
		return ggbAPI.exportGIF(Ljava/lang/String;DDZLjava/lang/String;D)(sliderLabel, scale, timeBetweenFrames | 500, !!isLoop, filename, rotate | 0);
	};

	public String getFileJSON (thumbnail) {
		return ggbAPI.getFileJSON(Z)(!!thumbnail);
	};

	public String setFileJSON (zip) {
		return ggbAPI.setFileJSON(Ljava/lang/Object;)(zip);
	};

	public String setLanguage (lang) {
		return ggbAPI.setLanguage(lang + "");
	};

	public String showTooltip (lang) {
		return ggbAPI.showTooltip(lang + "");
	};

	// APPS-646 deprecated, needs changing to getValue("correct")
	api.getExerciseFraction = function() {
		return ggbAPI.@org.geogebra.common.plugin.GgbAPI::getExerciseFraction()();
	};

	// APPS-646 Exercises no longer supported
	api.isExercise = function() {
		return false;
	};

	public String setExternalPath (path) {
		ggbAPI.setExternalPath(path + "");
	};

	public String checkSaved (path) {
		ggbAPI.checkSaved(Lcom/google/gwt/core/client/JavaScriptObject;)(path);
	};

	public String getCASObjectNumber () {
		return ggbAPI.getCASObjectNumber()();
	};

	public String writePNGtoFile (filename, exportScale, transparent, DPI, greyscale) {
		return ggbAPI.writePNGtoFile(Ljava/lang/String;DZDZ)(filename + "", exportScale, !!transparent, DPI, !!greyscale);
	};

	public String exportPGF (callback) {
		return ggbAPI.exportPGF(Lcom/google/gwt/core/client/JavaScriptObject;)(callback);
	};

	public String exportSVG (filename) {
		return ggbAPI.exportSVG(filename);
	};

	public String exportPDF (scale, filename, sliderLabel) {
		return ggbAPI.exportPDF(DLjava/lang/String;Ljava/lang/String;)(scale | 1, filename, sliderLabel);
	};

	public String exportPSTricks (callback) {
		return ggbAPI.exportPSTricks(Lcom/google/gwt/core/client/JavaScriptObject;)(callback);
	};

	public String exportAsymptote (callback) {
		return ggbAPI.exportAsymptote(Lcom/google/gwt/core/client/JavaScriptObject;)(callback);
	};

	public String setRounding (digits) {
		return ggbAPI.setRounding(digits + "");
	};

	public String getRounding () {
		return ggbAPI.getRounding()();
	};

	public String copyTextToClipboard (text) {
		return ggbAPI.copyTextToClipboard(text + "");
	};

	public String evalLaTeX (text,mode) {
		return ggbAPI.evalLaTeX(Ljava/lang/String;I)(text + "", mode);
	};

	public String evalMathML (text) {
		return ggbAPI.evalMathML(text + "");
	};

	public String getScreenReaderOutput (text) {
		return ggbAPI.getScreenReaderOutput(text + "");
	};

	public String getEditorState () {
		return ggbAPI.getEditorState()();
	};

	public String setEditorState (state, label) {
		var stateString = typeof state == "string" ? state : JSON.stringify(state);
		ggbAPI.setEditorState(Ljava/lang/String;Ljava/lang/String;)(stateString, label || "");
	};

	public String exportCollada (xmin, xmax, ymin, ymax, zmin, zmax,
				xyScale, xzScale, xTickDistance, yTickDistance, zTickDistance) {
		return ggbAPI.exportCollada(DDDDDDDDDDD)(
			xmin || -5, xmax || 5, ymin || -5, ymax || 5, zmin || -5, zmax || 5, xyScale || 1,
			xzScale || 1, xTickDistance || -1, yTickDistance || -1, zTickDistance || -1);
	};

	public String exportSimple3d (name, xmin, xmax, ymin, ymax, zmin, zmax,
				xyScale, xzScale, xTickDistance, yTickDistance, zTickDistance) {
		return ggbAPI.exportSimple3d(Ljava/lang/String;DDDDDDDDDDD)(
			name + "", xmin, xmax, ymin, ymax, zmin, zmax, xyScale,
			xzScale, xTickDistance, yTickDistance, zTickDistance);
	};

	public String translate (arg1, callback) {
		return ggbAPI.translate(arg1 + "", callback);
	};

	public String exportConstruction (flags) {
		return ggbAPI.exportConstruction(Lcom/google/gwt/core/client/JsArrayString;)(flags || ["color","name","definition","value"]);
	};

	public String getConstructionSteps (breakpoints) {
		return ggbAPI.getConstructionSteps(Z)(!!breakpoints);
	};

	public String setConstructionStep (n, breakpoints) {
		ggbAPI.setConstructionStep(DZ)(n, !!breakpoints);
	};

	public String previousConstructionStep () {
		ggbAPI.previousConstructionStep()();
	};

	public String nextConstructionStep () {
		ggbAPI.nextConstructionStep()();
	};

	public String getEmbeddedCalculators () {
		return ggbAPI.getEmbeddedCalculators()();
	};

	public String getFrame (){
		return ggbAPI.getFrame()();
	}

	public String enableFpsMeasurement () {
		ggbAPI.enableFpsMeasurement()();
	};

	public String disableFpsMeasurement () {
		ggbAPI.disableFpsMeasurement()();
	};

	public String testDraw () {
		ggbAPI.testDraw()();
	};

	public String startDrawRecording () {
		ggbAPI.startDrawRecording()();
	};

	public String endDrawRecordingAndLogResults () {
		ggbAPI.endDrawRecordingAndLogResults()();
	};
}

/**
 * @param api
 *            exported object
 * @param ggbAPI
 *            internal API
 * @param getId
 *            listener to ID mapping
 */
protected final native void addClientListener(JavaScriptObject api,
		GgbAPIW ggbAPI, JavaScriptObject getId) /*-{
	public String registerClientListener (JSFunctionName) {
		ggbAPI.registerClientListener(getId(JSFunctionName));
	};
}-*/;

/**
 * Adds client listeners for specific events (add, update, click, ...)
 */
private native void addSpecificListenerFunctionsNative(JavaScriptObject api,
		GgbAPIW ggbAPI, JavaScriptObject listenerMappingFunction) /*-{
	var getId = listenerMappingFunction;

	public String registerAddListener (JSFunctionName) {
		ggbAPI.registerAddListener(getId(JSFunctionName));
	};

	public String registerStoreUndoListener (JSFunctionName) {
		ggbAPI.registerStoreUndoListener(getId(JSFunctionName));
	};

	public String unregisterAddListener (JSFunctionName) {
		ggbAPI.unregisterAddListener(getId(JSFunctionName));
	};

	public String registerRemoveListener (JSFunctionName) {
		ggbAPI.registerRemoveListener(getId(JSFunctionName));
	};

	public String unregisterRemoveListener (JSFunctionName) {
		ggbAPI.unregisterRemoveListener(getId(JSFunctionName));
	};

	public String registerClearListener (JSFunctionName) {
		ggbAPI.registerClearListener(getId(JSFunctionName));
	};

	public String unregisterClearListener (JSFunctionName) {
		ggbAPI.unregisterClearListener(getId(JSFunctionName));
	};

	public String registerRenameListener (JSFunctionName) {
		ggbAPI.registerRenameListener(getId(JSFunctionName));
	};

	public String unregisterRenameListener (JSFunctionName) {
		ggbAPI.registerRenameListener(getId(JSFunctionName));
	};

	public String registerUpdateListener (JSFunctionName) {
		ggbAPI.registerUpdateListener(getId(JSFunctionName));
	};

	public String unregisterUpdateListener (JSFunctionName) {
		ggbAPI.unregisterUpdateListener(getId(JSFunctionName));
	};

	public String unregisterClientListener (JSFunctionName) {
		ggbAPI.unregisterClientListener(getId(JSFunctionName));
	};

	public String registerObjectUpdateListener (objname, JSFunctionName) {
		ggbAPI.registerObjectUpdateListener(Ljava/lang/String;Ljava/lang/String;)(objname + "", getId(JSFunctionName));
	};

	public String unregisterObjectUpdateListener (JSFunctionName) {
		ggbAPI.unregisterObjectUpdateListener(getId(JSFunctionName));
	};

	public String registerObjectClickListener (objname, JSFunctionName) {
		ggbAPI.registerObjectClickListener(Ljava/lang/String;Ljava/lang/String;)(objname + "", getId(JSFunctionName));
	};

	public String unregisterObjectClickListener (objname) {
		ggbAPI.unregisterObjectClickListener(objname + "");
	};

	public String registerClickListener (JSFunctionName) {
		ggbAPI.registerClickListener(getId(JSFunctionName));
	};

	public String unregisterClickListener (JSFunctionName) {
		ggbAPI.unregisterClickListener(getId(JSFunctionName));
	};

}-*/;
}
